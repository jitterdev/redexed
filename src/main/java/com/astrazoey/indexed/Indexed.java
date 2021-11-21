package com.astrazoey.indexed;

import com.astrazoey.indexed.enchantments.*;
import com.astrazoey.indexed.registry.IndexedItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.loot.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;




public class Indexed implements ModInitializer {

    public static final String MOD_ID = "indexed";

    //Enchantments
    public static Enchantment SLOW_BURN = Registry.register(
            Registry.ENCHANTMENT,
            new Identifier("indexed", "slow_burn"),
            new SlowBurnEnchantment()
    );

    public static Enchantment QUICK_FLIGHT = Registry.register(
            Registry.ENCHANTMENT,
            new Identifier("indexed", "quick_flight"),
            new SlowBurnEnchantment()
    );

    public static Enchantment FORGERY = Registry.register(
            Registry.ENCHANTMENT,
            new Identifier("indexed", "forgery"),
            new ForgeryEnchantment()
    );

    public static Enchantment MYSTERY_CURSE = Registry.register(
            Registry.ENCHANTMENT,
            new Identifier("indexed", "mystery_curse"),
            new MysteryCurseEnchantment()
    );

    public static Enchantment HIDDEN_CURSE = Registry.register(
            Registry.ENCHANTMENT,
            new Identifier("indexed", "hidden_curse"),
            new HiddenCurseEnchantment()
    );

    public static Enchantment KEEPING = Registry.register(
            Registry.ENCHANTMENT,
            new Identifier("indexed", "keeping"),
            new KeepingEnchantment()
    );

    public static Enchantment ESSENCE = Registry.register(
            Registry.ENCHANTMENT,
            new Identifier("indexed", "essence"),
            new EssenceEnchantment()
    );


    //Loot Tables
    public static Identifier INDEXED_LOOT = new Identifier("indexed", "indexed_items");






    @Override
    public void onInitialize() {

        IndexedItems.registerItems();


        //Add Items to Chests
        LootTableLoadingCallback.EVENT.register(((resourceManager, manager, id, supplier, setter) -> {
            if(LootTables.END_CITY_TREASURE_CHEST.equals(id) ||
                    LootTables.ABANDONED_MINESHAFT_CHEST.equals(id) ||
                    LootTables.STRONGHOLD_LIBRARY_CHEST.equals(id) ||
                    LootTables.BASTION_TREASURE_CHEST.equals(id) ||
                    LootTables.WOODLAND_MANSION_CHEST.equals(id) ||
                    LootTables.RUINED_PORTAL_CHEST.equals(id)) {
                supplier.copyFrom(manager.getTable(INDEXED_LOOT));

            }
        }));


        //Registers Config
        Identifier identifier = new Identifier(MOD_ID);
        ServerLifecycleEvents.SERVER_STARTING.register(identifier, callbacks -> {
            System.out.println("INDEXED: Starting starting. Loading config.");
            initializeConfig();
        });

        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register(identifier, (server, serverResourceManager) -> {
            System.out.println("INDEXED: Server data pack reload. Loading config.");
            initializeConfig();
        });

    }

    public static void initializeConfig() {
        ConfigMain.load(false);
        boolean modOutOfDate = ConfigMain.isOutOfDate();
        Config.loadConfig(modOutOfDate);
        ConfigMain.load(modOutOfDate);
    }
}
