package com.astrazoey.indexed.archive;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.SilkTouchEnchantment;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
/*
@Mixin(SilkTouchEnchantment.class)
public class SilkTouchEnchantmentMixin extends Enchantment {
    protected SilkTouchEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(Rarity.RARE, type, slotTypes);
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }
}
*/