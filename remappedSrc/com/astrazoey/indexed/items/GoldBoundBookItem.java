package com.astrazoey.indexed.items;

import net.minecraft.item.BookItem;
import net.minecraft.item.ItemStack;

public class GoldBoundBookItem extends BookItem {

    public GoldBoundBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getEnchantability() {
        return 100;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }
}
