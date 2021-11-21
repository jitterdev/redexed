package com.astrazoey.indexed;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;

public interface SetMaxEnchantmentLevel {
    static void set(Enchantment enchantment, int maxLevel) {
        ((SetMaxEnchantmentLevel) enchantment).setMaxEnchantmentLevel(maxLevel);
    }

    void setMaxEnchantmentLevel(int maxLevel);
}
