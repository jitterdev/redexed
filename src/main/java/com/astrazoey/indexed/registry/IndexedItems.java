package com.astrazoey.indexed.registry;

import com.astrazoey.indexed.Indexed;
import com.astrazoey.indexed.items.GoldBoundBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class IndexedItems {

    public static final GoldBoundBookItem GOLD_BOUND_BOOK = new GoldBoundBookItem(new Item.Settings());

    public static void registerItems() {
        Registry.register(Registries.ITEM, new Identifier(Indexed.MOD_ID, "gold_bound_book"), GOLD_BOUND_BOOK);
    }

}
