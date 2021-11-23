package com.astrazoey.indexed.mixins;

import com.astrazoey.indexed.EnchantingAcceptability;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

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
