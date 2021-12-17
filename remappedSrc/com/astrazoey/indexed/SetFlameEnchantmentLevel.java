package com.astrazoey.indexed;

import net.minecraft.entity.Entity;

public interface SetFlameEnchantmentLevel {

    static void set(Entity entity, int level) {
        ((SetFlameEnchantmentLevel) entity).setFlameEnchantmentLevel(level);
    }

    void setFlameEnchantmentLevel(int flameLevel);

}
