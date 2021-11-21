package com.astrazoey.indexed.mixins;

import com.astrazoey.indexed.EnchantingType;
import com.astrazoey.indexed.MaxEnchantingSlots;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


import java.util.List;
import java.util.Map;

@Mixin(Item.class)
public abstract class ItemMixin implements MaxEnchantingSlots {
    @Shadow public abstract Item asItem();

    public int usedEnchantingSlots = 0;
    public EnchantingType enchantingType;

    @Override
    public EnchantingType getEnchantingType(ItemStack stack) {
        return enchantingType;
    }

    @Override
    public void setEnchantingType(EnchantingType enchantingType) {
        this.enchantingType = enchantingType;
    }


    @Override
    public int getEnchantingSlots(ItemStack itemStack) {
        Map<Enchantment, Integer> enchantingMap = EnchantmentHelper.get(itemStack);

        int totalLevels = 0;

        for(int enchantmentLevel : enchantingMap.values()) {
            totalLevels += enchantmentLevel;
        }

        usedEnchantingSlots = totalLevels;

        return totalLevels;
    }

    /**
     * @author Astrazoey
     */
    @Overwrite
    public int getEnchantability() {
        if(this.asItem() == Items.ELYTRA || this.asItem() == Items.SHEARS || this.asItem() == Items.SHIELD || this.asItem() == Items.FLINT_AND_STEEL) {
            return 5;
        } else {
            return 0;
        }
    }
}

@Mixin(ItemStack.class)
class ItemStackMixin {

    @Shadow
    public static void appendEnchantments(List<Text> tooltip, NbtList enchantments) {}

    @Inject(method="getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void appendTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List list, int i) {
        //if not an enchanted book
        if(((ItemStack) (Object) this).getItem() != Items.ENCHANTED_BOOK) {
            //If enchantable, add text.
            EnchantingType enchantingType = MaxEnchantingSlots.getEnchantType(((ItemStack) (Object) this));
            if(enchantingType != null) {
                MutableText mutableText;
                Formatting formatting;

                if(MaxEnchantingSlots.getCurrent(((ItemStack) (Object) this)) <= enchantingType.getMaxEnchantingSlots()) {
                    formatting = Formatting.BLUE;
                } else {
                    formatting = Formatting.RED;
                }

                mutableText = (new TranslatableText("item.indexed.enchantment_tooltip", MaxEnchantingSlots.getCurrent((ItemStack) (Object) this), MaxEnchantingSlots.getEnchantType((ItemStack) (Object) this).getMaxEnchantingSlots())).formatted(formatting);

                list.add(mutableText);
            }
        }
    }
}