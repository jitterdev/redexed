package com.astrazoey.indexed.mixins;

import com.astrazoey.indexed.EnchantingAcceptability;
import com.astrazoey.indexed.Indexed;
import com.astrazoey.indexed.MaxEnchantingSlots;
import com.astrazoey.indexed.registry.IndexedItems;
import net.minecraft.advancement.criterion.EnchantedItemCriterion;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    //Prevents enchantments such as slow burn from being applied to armor in loot

    private static ThreadLocal<ItemStack> itemType = new ThreadLocal<ItemStack>();

    @Inject(method = "getPossibleEntries", at = @At(value="INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private static void getItem(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        itemType.set(stack);
    }

    @Redirect(method = "getPossibleEntries", at = @At(value="INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;isAvailableForRandomSelection()Z"))
    private static boolean denyIncompatibleEntries(Enchantment enchantment) {
        EnchantingAcceptability acceptabilityTest = new EnchantingAcceptability();
        return acceptabilityTest.checkAcceptability(enchantment, itemType);
    }

}


@Mixin(EnchantmentScreenHandler.class)
class TakeEnchantment {

    //Grant Gold Book Enchantment
    @Inject(method = "method_17410", at = @At(value="INVOKE", target = "Lnet/minecraft/advancement/criterion/EnchantedItemCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/item/ItemStack;I)V"))
    public void grantGoldBookAdvancement(ItemStack itemStack, int i, PlayerEntity playerEntity, int j, ItemStack itemStack2, World world, BlockPos pos, CallbackInfo ci) {
        if(itemStack.isOf(IndexedItems.GOLD_BOUND_BOOK)) {
            Indexed.ENCHANT_GOLD_BOOK.trigger((ServerPlayerEntity) playerEntity);
        }
    }

    //Grant Overcharged Advancement
    @Redirect(method = "method_17410", at = @At(value="INVOKE", target = "Lnet/minecraft/advancement/criterion/EnchantedItemCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/item/ItemStack;I)V"))
    public void grantOverchargedAdvancement(EnchantedItemCriterion instance, ServerPlayerEntity player, ItemStack stack, int levels) {
        System.out.println("Redirect activated");

        if(MaxEnchantingSlots.getEnchantType(stack) != null) {
            if(MaxEnchantingSlots.getCurrent(stack) > MaxEnchantingSlots.getEnchantType(stack).getMaxEnchantingSlots()) {
                System.out.println("Advancement granted");
                Indexed.OVERCHARGE_ITEM.trigger(player);
            }
        }

        instance.trigger(player, stack, levels);

    }
}