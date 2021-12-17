package com.astrazoey.indexed.archive;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/*
@Mixin(DamageEnchantment.class)
public class DamageEnchantmentMixin extends Enchantment {

    protected DamageEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        if(((Enchantment) (Object) this) == other) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }

    @ModifyConstant(method = "getAttackDamage", constant = @Constant(floatValue = 0.5f, ordinal = 0))
    public float modifySharpnessDamage(float damageMultiplier) {
        return 0.4f;
    }
}
*/