package com.astrazoey.indexed.archive;

import net.minecraft.enchantment.EfficiencyEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
/*
@Mixin(UnbreakingEnchantment.class)
public class UnbreakingEnchantmentMixin extends Enchantment {
    protected UnbreakingEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    /**
     * @author Astrazoey

    @Overwrite
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }
}

@Mixin(EfficiencyEnchantment.class)
class EfficiencyEnchantmentMixin2 extends Enchantment {
    protected EfficiencyEnchantmentMixin2(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    /**
     * @author Astrazoey

    @Overwrite
    public int getMaxLevel() {
        return 6;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }
}
*/