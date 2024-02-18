package com.astrazoey.indexed;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Properties;

public class Config {

    //private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("indexed_modded_items.properties");

    private static final File configFile = FabricLoader.getInstance().getConfigDir().resolve("indexed_modded_items.json").toFile();
    private static final Path configFilePath = FabricLoader.getInstance().getConfigDir().resolve("indexed_modded_items.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static LinkedHashMap<String, EnchantabilityConfig> configList;



    public static void save() {
        Properties props = new Properties();
        read(props);

        /*
        if (!Files.exists(CONFIG_PATH)) {
            try {
                Files.createFile(CONFIG_PATH);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }*/

        /*
        try (OutputStream out = Files.newOutputStream(CONFIG_PATH)) {
            props.store(out, "Indexed Configuration");
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
    }

    private static boolean generateNewConfigFile() {
        try {
            Files.createFile(configFilePath);
            save();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean readConfigList() {
        try(FileReader reader = new FileReader(Config.configFile)) {
            Config.configList = gson.fromJson(reader, new TypeToken<LinkedHashMap<String, EnchantabilityConfig>>(){}.getType());

            if(configList == null) {
                Config.configFile.delete();
                System.out.println("INDEXED: Config file is null and deleted. Generating new config.");
                if(!generateNewConfigFile()) {
                    return false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void registerConfigListItems() {
        for(int i = 0; i < configList.size(); i++) {
            Object configEntry = configList.keySet().toArray()[i];
            EnchantabilityConfig enchantConfig = configList.get(configEntry);
            String itemName = (String) configEntry;
            int maxSlots = enchantConfig.maxEnchantingSlots;
            float repairScale = enchantConfig.repairScaling;

            //System.out.println("Found entry at " + i + ". It is called \"" + itemName + "\" and has " + maxSlots + " max slots and repair scaling of " + repairScale);

            Identifier itemIdentifier = new Identifier(itemName);
            ThreadLocal<Identifier> localItemIdentifier = new ThreadLocal<Identifier>();
            localItemIdentifier.set(itemIdentifier);

            Item registerItem = Registries.ITEM.get(localItemIdentifier.get());


            MaxEnchantingSlots.setEnchantType(registerItem, new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(maxSlots).repairScaling(repairScale)));

        }
    }


    public static void loadConfig(boolean modOutOfDate) {

        if(modOutOfDate && Files.exists(configFilePath)) {
            try {
                System.out.println("mod is out of date! resetting file!");
                Files.delete(configFilePath);
                configList = null;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to delete config file.");
                return;
            }
        }

        if(Config.configFile.exists()) {
            if(!readConfigList()) {
                System.out.println("Failed to read config list.");
                return;
            }
        } else {
            try {
                Files.createFile(configFilePath);
                save();
            } catch (Exception e) {
                if(!generateNewConfigFile()) {
                    System.out.println("Failed to generate new config file");
                    return;
                }
            }
        }


        if(configList == null) {
            if(!readConfigList()) {
                System.out.println("INDEXED: Failed to read config.");
                return;
            }
        }

        registerConfigListItems();
    }

    public static void read(Properties props) {
        LinkedHashMap<String, EnchantabilityConfig> defaultConfig = new LinkedHashMap<>();

        /*
        The following are some default values for vanilla items.
         */

        //Netherite
        defaultConfig.put("minecraft:netherite_sword", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:netherite_pickaxe", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:netherite_axe", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:netherite_hoe", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:netherite_shovel", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:netherite_helmet", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:netherite_chestplate", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:netherite_leggings", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:netherite_boots", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());

        //Diamond
        defaultConfig.put("minecraft:diamond_sword", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:diamond_pickaxe", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:diamond_axe", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:diamond_hoe", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:diamond_shovel", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:diamond_helmet", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:diamond_chestplate", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:diamond_leggings", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:diamond_boots", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());

        //Gold
        defaultConfig.put("minecraft:golden_sword", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:golden_pickaxe", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:golden_axe", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:golden_hoe", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:golden_shovel", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:golden_helmet", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:golden_chestplate", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:golden_leggings", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:golden_boots", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());

        //Iron
        defaultConfig.put("minecraft:iron_sword", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:iron_pickaxe", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:iron_axe", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:iron_hoe", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:iron_shovel", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:iron_helmet", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:iron_chestplate", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:iron_leggings", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:iron_boots", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());

        //Stone
        defaultConfig.put("minecraft:stone_sword", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:stone_pickaxe", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:stone_axe", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:stone_hoe", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:stone_shovel", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());

        //Wood
        defaultConfig.put("minecraft:wooden_sword", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:wooden_pickaxe", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:wooden_axe", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:wooden_hoe", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:wooden_shovel", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());

        //Chainmail
        defaultConfig.put("minecraft:chainmail_helmet", EnchantingTypes.CHAINMAIL_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:chainmail_chestplate", EnchantingTypes.CHAINMAIL_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:chainmail_leggings", EnchantingTypes.CHAINMAIL_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:chainmail_boots", EnchantingTypes.CHAINMAIL_TIER.getEnchantabilityConfig());

        //Leather
        defaultConfig.put("minecraft:leather_helmet", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:leather_chestplate", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:leather_leggings", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("minecraft:leather_boots", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());

        //Misc
        defaultConfig.put("minecraft:fishing_rod", EnchantingTypes.FISHING_ROD.getEnchantabilityConfig());
        defaultConfig.put("minecraft:crossbow", EnchantingTypes.CROSSBOW.getEnchantabilityConfig());
        defaultConfig.put("minecraft:bow", EnchantingTypes.BOW.getEnchantabilityConfig());
        defaultConfig.put("minecraft:trident", EnchantingTypes.TRIDENT.getEnchantabilityConfig());
        defaultConfig.put("minecraft:turtle_helmet", EnchantingTypes.TURTLE_HELMET.getEnchantabilityConfig());

        //Items that only have Unbreaking, Mending, and Forgery
        defaultConfig.put("minecraft:elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("minecraft:shears", EnchantingTypes.GENERIC.getEnchantabilityConfig());
        defaultConfig.put("minecraft:flint_and_steel", EnchantingTypes.GENERIC.getEnchantabilityConfig());
        defaultConfig.put("minecraft:shield", EnchantingTypes.GENERIC.getEnchantabilityConfig());
        defaultConfig.put("minecraft:carrot_on_a_stick", EnchantingTypes.GENERIC.getEnchantabilityConfig());
        defaultConfig.put("minecraft:warped_fungus_on_a_stick", EnchantingTypes.GENERIC.getEnchantabilityConfig());



        /*
        The following is a default auto-generated config for mods that are supported by default.
         */


        //Mattock
        EnchantabilityConfig mattockConfig = new EnchantabilityConfig(5, 2.0f);
        defaultConfig.put("unitool:mattock", mattockConfig);

        //Carve Your Pumpkin
        defaultConfig.put("carvepump:wooden_carver", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("carvepump:stone_carver", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("carvepump:iron_carver", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("carvepump:gold_carver", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("carvepump:diamond_carver", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("carvepump:netherite_carver", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());

        //Outvoted
        defaultConfig.put("outvoted:wildfire_shield", EnchantingTypes.SHIELD.getEnchantabilityConfig());

        //Better End
        defaultConfig.put("betterend:elytra_crystalite", EnchantingTypes.ELYTRA_MODIFIED.getEnchantabilityConfig());
        defaultConfig.put("betterend:elytra_armored", EnchantingTypes.ELYTRA_MODIFIED.getEnchantabilityConfig());

        defaultConfig.put("betterend:iron_hammer", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("betterend:golden_hammer", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("betterend:diamond_hammer", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("betterend:netherite_hammer", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());

        defaultConfig.put("betterend:aeternium_sword", EnchantingTypes.AETERNIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:aeternium_pickaxe", EnchantingTypes.AETERNIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:aeternium_axe", EnchantingTypes.AETERNIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:aeternium_hoe", EnchantingTypes.AETERNIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:aeternium_hammer", EnchantingTypes.AETERNIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:aeternium_shovel", EnchantingTypes.AETERNIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:aeternium_helmet", EnchantingTypes.AETERNIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:aeternium_chestplate", EnchantingTypes.AETERNIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:aeternium_leggings", EnchantingTypes.AETERNIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:aeternium_boots", EnchantingTypes.AETERNIUM.getEnchantabilityConfig());

        defaultConfig.put("betterend:terminite_sword", EnchantingTypes.TERMINITE.getEnchantabilityConfig());
        defaultConfig.put("betterend:terminite_pickaxe", EnchantingTypes.TERMINITE.getEnchantabilityConfig());
        defaultConfig.put("betterend:terminite_axe", EnchantingTypes.TERMINITE.getEnchantabilityConfig());
        defaultConfig.put("betterend:terminite_hoe", EnchantingTypes.TERMINITE.getEnchantabilityConfig());
        defaultConfig.put("betterend:terminite_hammer", EnchantingTypes.TERMINITE.getEnchantabilityConfig());
        defaultConfig.put("betterend:terminite_shovel", EnchantingTypes.TERMINITE.getEnchantabilityConfig());
        defaultConfig.put("betterend:terminite_helmet", EnchantingTypes.TERMINITE.getEnchantabilityConfig());
        defaultConfig.put("betterend:terminite_chestplate", EnchantingTypes.TERMINITE.getEnchantabilityConfig());
        defaultConfig.put("betterend:terminite_leggings", EnchantingTypes.TERMINITE.getEnchantabilityConfig());
        defaultConfig.put("betterend:terminite_boots", EnchantingTypes.TERMINITE.getEnchantabilityConfig());

        defaultConfig.put("betterend:thallasium_sword", EnchantingTypes.THALLASIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:thallasium_pickaxe", EnchantingTypes.THALLASIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:thallasium_axe", EnchantingTypes.THALLASIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:thallasium_hoe", EnchantingTypes.THALLASIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:thallasium_hammer", EnchantingTypes.THALLASIUM.getEnchantabilityConfig());
        defaultConfig.put("betterend:thallasium_shovel", EnchantingTypes.THALLASIUM.getEnchantabilityConfig());

        defaultConfig.put("betterend:crystalite_helmet", EnchantingTypes.CRYSTALITE.getEnchantabilityConfig());
        defaultConfig.put("betterend:crystalite_chestplate", EnchantingTypes.CRYSTALITE.getEnchantabilityConfig());
        defaultConfig.put("betterend:crystalite_leggings", EnchantingTypes.CRYSTALITE.getEnchantabilityConfig());
        defaultConfig.put("betterend:crystalite_boots", EnchantingTypes.CRYSTALITE.getEnchantabilityConfig());


        //Better Nether
        defaultConfig.put("betternether:cincinnasite_shears", EnchantingTypes.SHEARS.getEnchantabilityConfig());

        defaultConfig.put("betternether:cincinnasite_sword", EnchantingTypes.CINCINNASITE.getEnchantabilityConfig());
        defaultConfig.put("betternether:cincinnasite_axe", EnchantingTypes.CINCINNASITE.getEnchantabilityConfig());
        defaultConfig.put("betternether:cincinnasite_shovel", EnchantingTypes.CINCINNASITE.getEnchantabilityConfig());
        defaultConfig.put("betternether:cincinnasite_hoe", EnchantingTypes.CINCINNASITE.getEnchantabilityConfig());
        defaultConfig.put("betternether:cincinnasite_pickaxe", EnchantingTypes.CINCINNASITE.getEnchantabilityConfig());
        defaultConfig.put("betternether:cincinnasite_helmet", EnchantingTypes.CINCINNASITE.getEnchantabilityConfig());
        defaultConfig.put("betternether:cincinnasite_chestplate", EnchantingTypes.CINCINNASITE.getEnchantabilityConfig());
        defaultConfig.put("betternether:cincinnasite_leggings", EnchantingTypes.CINCINNASITE.getEnchantabilityConfig());
        defaultConfig.put("betternether:cincinnasite_boots", EnchantingTypes.CINCINNASITE.getEnchantabilityConfig());

        defaultConfig.put("betternether:cincinnasite_sword_diamond", EnchantingTypes.CINCINNASITE_DIAMOND.getEnchantabilityConfig());
        defaultConfig.put("betternether:cincinnasite_axe_diamond", EnchantingTypes.CINCINNASITE_DIAMOND.getEnchantabilityConfig());
        defaultConfig.put("betternether:cincinnasite_shovel_diamond", EnchantingTypes.CINCINNASITE_DIAMOND.getEnchantabilityConfig());
        defaultConfig.put("betternether:cincinnasite_hoe_diamond", EnchantingTypes.CINCINNASITE_DIAMOND.getEnchantabilityConfig());
        defaultConfig.put("betternether:cincinnasite_pickaxe_diamond", EnchantingTypes.CINCINNASITE_DIAMOND.getEnchantabilityConfig());

        defaultConfig.put("betternether:nether_ruby_sword", EnchantingTypes.NETHER_RUBY.getEnchantabilityConfig());
        defaultConfig.put("betternether:nether_ruby_axe", EnchantingTypes.NETHER_RUBY.getEnchantabilityConfig());
        defaultConfig.put("betternether:nether_ruby_shovel", EnchantingTypes.NETHER_RUBY.getEnchantabilityConfig());
        defaultConfig.put("betternether:nether_ruby_hoe", EnchantingTypes.NETHER_RUBY.getEnchantabilityConfig());
        defaultConfig.put("betternether:nether_ruby_pickaxe", EnchantingTypes.NETHER_RUBY.getEnchantabilityConfig());
        defaultConfig.put("betternether:nether_ruby_helmet", EnchantingTypes.NETHER_RUBY.getEnchantabilityConfig());
        defaultConfig.put("betternether:nether_ruby_chestplate", EnchantingTypes.NETHER_RUBY.getEnchantabilityConfig());
        defaultConfig.put("betternether:nether_ruby_leggings", EnchantingTypes.NETHER_RUBY.getEnchantabilityConfig());
        defaultConfig.put("betternether:nether_ruby_boots", EnchantingTypes.NETHER_RUBY.getEnchantabilityConfig());


        //AdventureZ
        defaultConfig.put("adventurez:stone_golem_helmet", EnchantingTypes.NETHERITE_GILDED.getEnchantabilityConfig());
        defaultConfig.put("adventurez:stone_golem_chestplate", EnchantingTypes.NETHERITE_GILDED.getEnchantabilityConfig());
        defaultConfig.put("adventurez:stone_golem_leggings", EnchantingTypes.NETHERITE_GILDED.getEnchantabilityConfig());
        defaultConfig.put("adventurez:stone_golem_boots", EnchantingTypes.NETHERITE_GILDED.getEnchantabilityConfig());

        defaultConfig.put("adventurez:ender_flute", EnchantingTypes.GENERIC.getEnchantabilityConfig());
        defaultConfig.put("adventurez:chorus_fruit_on_a_stick", EnchantingTypes.GENERIC.getEnchantabilityConfig());

        //Wolf Armor
        defaultConfig.put("wolveswitharmor:leather_wolf_armor", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("wolveswitharmor:iron_wolf_armor", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("wolveswitharmor:golden_wolf_armor", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("wolveswitharmor:diamond_wolf_armor", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("wolveswitharmor:netherite_wolf_armor", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());

        //Gate of Babylon
        defaultConfig.put("gateofbabylon:netherite_dagger", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:netherite_spear", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:netherite_broadsword", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:netherite_rapier", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:netherite_haladie", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:netherite_waraxe", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:netherite_katana", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:netherite_bow", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:netherite_shield", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:netherite_yoyo", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:netherite_boomerang", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());

        defaultConfig.put("gateofbabylon:diamond_dagger", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:diamond_spear", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:diamond_broadsword", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:diamond_rapier", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:diamond_haladie", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:diamond_waraxe", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:diamond_katana", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:diamond_bow", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:diamond_shield", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:diamond_yoyo", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:diamond_boomerang", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());

        defaultConfig.put("gateofbabylon:golden_dagger", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:golden_spear", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:golden_broadsword", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:golden_rapier", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:golden_haladie", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:golden_waraxe", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:golden_katana", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:golden_bow", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:golden_shield", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:golden_yoyo", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:golden_boomerang", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());

        defaultConfig.put("gateofbabylon:iron_dagger", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:iron_spear", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:iron_broadsword", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:iron_rapier", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:iron_haladie", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:iron_waraxe", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:iron_katana", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:iron_bow", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:iron_shield", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:iron_yoyo", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:iron_boomerang", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());

        defaultConfig.put("gateofbabylon:stone_dagger", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:stone_spear", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:stone_broadsword", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:stone_rapier", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:stone_haladie", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:stone_waraxe", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:stone_katana", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:stone_bow", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:stone_shield", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:stone_yoyo", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:stone_boomerang", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());

        defaultConfig.put("gateofbabylon:wooden_dagger", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:wooden_spear", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:wooden_broadsword", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:wooden_rapier", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:wooden_haladie", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:wooden_waraxe", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:wooden_katana", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:wooden_bow", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:wooden_shield", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:wooden_yoyo", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("gateofbabylon:wooden_boomerang", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());

        //Dragon Loot
        defaultConfig.put("dragonloot:dragon_helmet", EnchantingTypes.DRAGON.getEnchantabilityConfig());
        defaultConfig.put("dragonloot:dragon_chestplate", EnchantingTypes.DRAGON.getEnchantabilityConfig());
        defaultConfig.put("dragonloot:dragon_leggings", EnchantingTypes.DRAGON.getEnchantabilityConfig());
        defaultConfig.put("dragonloot:dragon_boots", EnchantingTypes.DRAGON.getEnchantabilityConfig());
        defaultConfig.put("dragonloot:dragon_pickaxe", EnchantingTypes.DRAGON.getEnchantabilityConfig());
        defaultConfig.put("dragonloot:dragon_shovel", EnchantingTypes.DRAGON.getEnchantabilityConfig());
        defaultConfig.put("dragonloot:dragon_axe", EnchantingTypes.DRAGON.getEnchantabilityConfig());
        defaultConfig.put("dragonloot:dragon_hoe", EnchantingTypes.DRAGON.getEnchantabilityConfig());
        defaultConfig.put("dragonloot:dragon_sword", EnchantingTypes.DRAGON.getEnchantabilityConfig());

        EnchantabilityConfig dragonExtraConfig = new EnchantabilityConfig(6, 1.0f);
        defaultConfig.put("dragonloot:dragon_crossbow", dragonExtraConfig);
        defaultConfig.put("dragonloot:dragon_bow", dragonExtraConfig);
        defaultConfig.put("dragonloot:dragon_trident", dragonExtraConfig);

        //Rat Mischeif
        defaultConfig.put("ratsmischief:rat_mask", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());

        //Farmer's Delight
        defaultConfig.put("farmersdelight:flint_knife", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("farmersdelight:iron_knife", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("farmersdelight:golden_knife", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("farmersdelight:diamond_knife", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("farmersdelight:netherite_knife", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());

        // Nether's Delight
        defaultConfig.put("nethersdelight:iron_machete", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("nethersdelight:golden_machete", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("nethersdelight:diamond_machete", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("nethersdelight:netherite_machete", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());

        //Consistency+
        defaultConfig.put("consistency_plus:turtle_chestplate", EnchantingTypes.TURTLE_HELMET.getEnchantabilityConfig());
        defaultConfig.put("consistency_plus:turtle_leggings", EnchantingTypes.TURTLE_HELMET.getEnchantabilityConfig());
        defaultConfig.put("consistency_plus:turtle_boots", EnchantingTypes.TURTLE_HELMET.getEnchantabilityConfig());

        //ValleyCraft
        defaultConfig.put("valley:rg_helmet", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:rg_chestplate", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:rg_leggings", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:rg_boots", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:rg_sword", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:rg_axe", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:rg_shovel", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:rg_hoe", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:rg_pickaxe", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());

        defaultConfig.put("valley:wood_knife", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:stone_knife", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:iron_knife", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:golden_knife", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:diamond_knife", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:netherite_knife", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:rg_knife", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:bone_knife", EnchantingTypes.BONE_TIER.getEnchantabilityConfig());

        defaultConfig.put("valley:wood_sickle", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:stone_sickle", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:iron_sickle", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:golden_sickle", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:diamond_sickle", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:netherite_sickle", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:rg_sickle", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());

        defaultConfig.put("valley:wood_hatchet", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:stone_hatchet", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:iron_hatchet", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:golden_hatchet", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:diamond_hatchet", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:netherite_hatchet", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:rg_hatchet", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());

        defaultConfig.put("valley:turtle_chestplate", EnchantingTypes.TURTLE_HELMET.getEnchantabilityConfig());
        defaultConfig.put("valley:turtle_leggings", EnchantingTypes.TURTLE_HELMET.getEnchantabilityConfig());
        defaultConfig.put("valley:turtle_boots", EnchantingTypes.TURTLE_HELMET.getEnchantabilityConfig());

        defaultConfig.put("valley:tongs", EnchantingTypes.GENERIC.getEnchantabilityConfig());
        defaultConfig.put("valley:lumber_axe", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("valley:fur_chestplate", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());

        // Impaled
        defaultConfig.put("impaled:pitchfork", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("impaled:atlan", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("impaled:elder_trident", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("impaled:hellfork", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());

        // Harvest Scythes
        defaultConfig.put("harvest_scythes:wooden_scythe", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("harvest_scythes:stone_scythe", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("harvest_scythes:iron_scythe", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("harvest_scythes:golden_scythe", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("harvest_scythes:diamond_scythe", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("harvest_scythes:netherite_scythe", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());

        defaultConfig.put("harvest_scythes:wooden_machete", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("harvest_scythes:stone_machete", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("harvest_scythes:iron_machete", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("harvest_scythes:golden_machete", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("harvest_scythes:diamond_machete", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("harvest_scythes:netherite_machete", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());

        // Go Fish
        defaultConfig.put("gofish:skeletal_rod", EnchantingTypes.BONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gofish:blaze_rod", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gofish:soul_rod", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("gofish:diamond_reinforced_rod", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gofish:ender_rod", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gofish:matrix_rod", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("gofish:frosted_rod", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("gofish:slime_rod", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("gofish:celestial_rod", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());

        // Additional Additions
        defaultConfig.put("additionaladditions:rose_gold_helmet", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:rose_gold_chestplate", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:rose_gold_leggings", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:rose_gold_boots", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:rose_gold_sword", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:rose_gold_axe", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:rose_gold_shovel", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:rose_gold_hoe", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:rose_gold_pickaxe", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());

        defaultConfig.put("additionaladditions:gilded_netherite_helmet", EnchantingTypes.NETHERITE_GILDED.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:gilded_netherite_chestplate", EnchantingTypes.NETHERITE_GILDED.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:gilded_netherite_leggings", EnchantingTypes.NETHERITE_GILDED.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:gilded_netherite_boots", EnchantingTypes.NETHERITE_GILDED.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:gilded_netherite_sword", EnchantingTypes.NETHERITE_GILDED.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:gilded_netherite_axe", EnchantingTypes.NETHERITE_GILDED.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:gilded_netherite_shovel", EnchantingTypes.NETHERITE_GILDED.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:gilded_netherite_hoe", EnchantingTypes.NETHERITE_GILDED.getEnchantabilityConfig());
        defaultConfig.put("additionaladditions:gilded_netherite_pickaxe", EnchantingTypes.NETHERITE_GILDED.getEnchantabilityConfig());

        defaultConfig.put("additionaladditions:crossbow_with_spyglass", EnchantingTypes.CROSSBOW.getEnchantabilityConfig());

        // Oxidized
        defaultConfig.put("oxidized:rose_gold_sword", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("oxidized:rose_gold_axe", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("oxidized:rose_gold_shovel", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("oxidized:rose_gold_hoe", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("oxidized:rose_gold_pickaxe", EnchantingTypes.ROSE_GOLD_TIER.getEnchantabilityConfig());

        // Conjuring
        defaultConfig.put("conjuring:soul_alloy_sword", EnchantingTypes.SOUL_ALLOY.getEnchantabilityConfig());
        defaultConfig.put("conjuring:soul_alloy_shovel", EnchantingTypes.SOUL_ALLOY.getEnchantabilityConfig());
        defaultConfig.put("conjuring:soul_alloy_hatchet", EnchantingTypes.SOUL_ALLOY.getEnchantabilityConfig());
        defaultConfig.put("conjuring:soul_alloy_pickaxe", EnchantingTypes.SOUL_ALLOY.getEnchantabilityConfig());

        // Mythic Metals
        defaultConfig.put("mythicmetals:adamantite_sword", EnchantingTypes.ADAMANTITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:adamantite_axe", EnchantingTypes.ADAMANTITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:adamantite_shovel", EnchantingTypes.ADAMANTITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:adamantite_hoe", EnchantingTypes.ADAMANTITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:adamantite_pickaxe", EnchantingTypes.ADAMANTITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:adamantite_helmet", EnchantingTypes.ADAMANTITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:adamantite_chestplate", EnchantingTypes.ADAMANTITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:adamantite_leggings", EnchantingTypes.ADAMANTITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:adamantite_boots", EnchantingTypes.ADAMANTITE.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:aquarium_sword", EnchantingTypes.AQUARIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:aquarium_axe", EnchantingTypes.AQUARIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:aquarium_shovel", EnchantingTypes.AQUARIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:aquarium_hoe", EnchantingTypes.AQUARIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:aquarium_pickaxe", EnchantingTypes.AQUARIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:aquarium_helmet", EnchantingTypes.AQUARIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:aquarium_chestplate", EnchantingTypes.AQUARIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:aquarium_leggings", EnchantingTypes.AQUARIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:aquarium_boots", EnchantingTypes.AQUARIUM.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:tidesinger_sword", EnchantingTypes.TIDESINGER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:tidesinger_axe", EnchantingTypes.TIDESINGER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:tidesinger_shovel", EnchantingTypes.TIDESINGER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:tidesinger_hoe", EnchantingTypes.TIDESINGER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:tidesinger_pickaxe", EnchantingTypes.TIDESINGER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:tidesinger_helmet", EnchantingTypes.TIDESINGER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:tidesinger_chestplate", EnchantingTypes.TIDESINGER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:tidesinger_leggings", EnchantingTypes.TIDESINGER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:tidesinger_boots", EnchantingTypes.TIDESINGER.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:banglum_sword", EnchantingTypes.BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:banglum_axe", EnchantingTypes.BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:banglum_shovel", EnchantingTypes.BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:banglum_hoe", EnchantingTypes.BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:banglum_pickaxe", EnchantingTypes.BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:banglum_helmet", EnchantingTypes.BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:banglum_chestplate", EnchantingTypes.BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:banglum_leggings", EnchantingTypes.BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:banglum_boots", EnchantingTypes.BANGLUM.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:bronze_sword", EnchantingTypes.BRONZE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:bronze_axe", EnchantingTypes.BRONZE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:bronze_shovel", EnchantingTypes.BRONZE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:bronze_hoe", EnchantingTypes.BRONZE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:bronze_pickaxe", EnchantingTypes.BRONZE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:bronze_helmet", EnchantingTypes.BRONZE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:bronze_chestplate", EnchantingTypes.BRONZE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:bronze_leggings", EnchantingTypes.BRONZE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:bronze_boots", EnchantingTypes.BRONZE.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:carmot_sword", EnchantingTypes.CARMOT.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:carmot_axe", EnchantingTypes.CARMOT.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:carmot_shovel", EnchantingTypes.CARMOT.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:carmot_hoe", EnchantingTypes.CARMOT.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:carmot_pickaxe", EnchantingTypes.CARMOT.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:carmot_helmet", EnchantingTypes.CARMOT.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:carmot_chestplate", EnchantingTypes.CARMOT.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:carmot_leggings", EnchantingTypes.CARMOT.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:carmot_boots", EnchantingTypes.CARMOT.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:celestium_sword", EnchantingTypes.CELESTIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:celestium_axe", EnchantingTypes.CELESTIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:celestium_shovel", EnchantingTypes.CELESTIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:celestium_hoe", EnchantingTypes.CELESTIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:celestium_pickaxe", EnchantingTypes.CELESTIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:celestium_helmet", EnchantingTypes.CELESTIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:celestium_chestplate", EnchantingTypes.CELESTIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:celestium_leggings", EnchantingTypes.CELESTIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:celestium_boots", EnchantingTypes.CELESTIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:celestium_elytra", EnchantingTypes.ELYTRA_MODIFIED.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:copper_sword", EnchantingTypes.COPPER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:copper_axe", EnchantingTypes.COPPER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:copper_shovel", EnchantingTypes.COPPER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:copper_hoe", EnchantingTypes.COPPER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:copper_pickaxe", EnchantingTypes.COPPER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:copper_helmet", EnchantingTypes.COPPER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:copper_chestplate", EnchantingTypes.COPPER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:copper_leggings", EnchantingTypes.COPPER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:copper_boots", EnchantingTypes.COPPER.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:durasteel_sword", EnchantingTypes.DURASTEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:durasteel_axe", EnchantingTypes.DURASTEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:durasteel_shovel", EnchantingTypes.DURASTEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:durasteel_hoe", EnchantingTypes.DURASTEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:durasteel_pickaxe", EnchantingTypes.DURASTEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:durasteel_helmet", EnchantingTypes.DURASTEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:durasteel_chestplate", EnchantingTypes.DURASTEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:durasteel_leggings", EnchantingTypes.DURASTEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:durasteel_boots", EnchantingTypes.DURASTEEL.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:hallowed_sword", EnchantingTypes.HALLOWED.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:hallowed_axe", EnchantingTypes.HALLOWED.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:hallowed_shovel", EnchantingTypes.HALLOWED.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:hallowed_hoe", EnchantingTypes.HALLOWED.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:hallowed_pickaxe", EnchantingTypes.HALLOWED.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:hallowed_helmet", EnchantingTypes.HALLOWED.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:hallowed_chestplate", EnchantingTypes.HALLOWED.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:hallowed_leggings", EnchantingTypes.HALLOWED.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:hallowed_boots", EnchantingTypes.HALLOWED.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:kyber_sword", EnchantingTypes.KYBER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:kyber_axe", EnchantingTypes.KYBER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:kyber_shovel", EnchantingTypes.KYBER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:kyber_hoe", EnchantingTypes.KYBER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:kyber_pickaxe", EnchantingTypes.KYBER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:kyber_helmet", EnchantingTypes.KYBER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:kyber_chestplate", EnchantingTypes.KYBER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:kyber_leggings", EnchantingTypes.KYBER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:kyber_boots", EnchantingTypes.KYBER.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:legendary_banglum_sword", EnchantingTypes.LEGENDARY_BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:legendary_banglum_axe", EnchantingTypes.LEGENDARY_BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:legendary_banglum_shovel", EnchantingTypes.LEGENDARY_BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:legendary_banglum_hoe", EnchantingTypes.LEGENDARY_BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:legendary_banglum_pickaxe", EnchantingTypes.LEGENDARY_BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:legendary_banglum_helmet", EnchantingTypes.LEGENDARY_BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:legendary_banglum_chestplate", EnchantingTypes.LEGENDARY_BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:legendary_banglum_leggings", EnchantingTypes.LEGENDARY_BANGLUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:legendary_banglum_boots", EnchantingTypes.LEGENDARY_BANGLUM.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:metallurgium_sword", EnchantingTypes.METALLURGIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:metallurgium_axe", EnchantingTypes.METALLURGIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:metallurgium_shovel", EnchantingTypes.METALLURGIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:metallurgium_hoe", EnchantingTypes.METALLURGIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:metallurgium_pickaxe", EnchantingTypes.METALLURGIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:metallurgium_helmet", EnchantingTypes.METALLURGIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:metallurgium_chestplate", EnchantingTypes.METALLURGIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:metallurgium_leggings", EnchantingTypes.METALLURGIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:metallurgium_boots", EnchantingTypes.METALLURGIUM.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:midas_gold_sword", EnchantingTypes.MIDAS_GOLD.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:midas_gold_axe", EnchantingTypes.MIDAS_GOLD.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:midas_gold_shovel", EnchantingTypes.MIDAS_GOLD.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:midas_gold_hoe", EnchantingTypes.MIDAS_GOLD.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:midas_gold_pickaxe", EnchantingTypes.MIDAS_GOLD.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:midas_gold_helmet", EnchantingTypes.MIDAS_GOLD.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:midas_gold_chestplate", EnchantingTypes.MIDAS_GOLD.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:midas_gold_leggings", EnchantingTypes.MIDAS_GOLD.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:midas_gold_boots", EnchantingTypes.MIDAS_GOLD.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:gilded_midas_gold_sword", EnchantingTypes.GILDED_MIDAS.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:royal_midas_gold_sword", EnchantingTypes.ROYAL_MIDAS.getEnchantabilityConfig());


        defaultConfig.put("mythicmetals:mythril_sword", EnchantingTypes.MYTHRIL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:mythril_axe", EnchantingTypes.MYTHRIL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:mythril_shovel", EnchantingTypes.MYTHRIL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:mythril_hoe", EnchantingTypes.MYTHRIL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:mythril_pickaxe", EnchantingTypes.MYTHRIL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:mythril_helmet", EnchantingTypes.MYTHRIL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:mythril_chestplate", EnchantingTypes.MYTHRIL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:mythril_leggings", EnchantingTypes.MYTHRIL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:mythril_boots", EnchantingTypes.MYTHRIL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:mythril_drill", EnchantingTypes.MYTHRIL_DRILL.getEnchantabilityConfig());


        defaultConfig.put("mythicmetals:orichalcum_sword", EnchantingTypes.ORICHALCUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:orichalcum_axe", EnchantingTypes.ORICHALCUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:orichalcum_shovel", EnchantingTypes.ORICHALCUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:orichalcum_hoe", EnchantingTypes.ORICHALCUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:orichalcum_pickaxe", EnchantingTypes.ORICHALCUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:orichalcum_helmet", EnchantingTypes.ORICHALCUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:orichalcum_chestplate", EnchantingTypes.ORICHALCUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:orichalcum_leggings", EnchantingTypes.ORICHALCUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:orichalcum_boots", EnchantingTypes.ORICHALCUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:orichalcum_hammer", EnchantingTypes.ORICHALCUM_HAMMER.getEnchantabilityConfig());


        defaultConfig.put("mythicmetals:osmium_sword", EnchantingTypes.OSMIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:osmium_axe", EnchantingTypes.OSMIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:osmium_shovel", EnchantingTypes.OSMIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:osmium_hoe", EnchantingTypes.OSMIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:osmium_pickaxe", EnchantingTypes.OSMIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:osmium_helmet", EnchantingTypes.OSMIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:osmium_chestplate", EnchantingTypes.OSMIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:osmium_leggings", EnchantingTypes.OSMIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:osmium_boots", EnchantingTypes.OSMIUM.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:osmium_chainmail_helmet", EnchantingTypes.OSMIUM_CHAINMAIL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:osmium_chainmail_chestplate", EnchantingTypes.OSMIUM_CHAINMAIL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:osmium_chainmail_leggings", EnchantingTypes.OSMIUM_CHAINMAIL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:osmium_chainmail_boots", EnchantingTypes.OSMIUM_CHAINMAIL.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:palladium_sword", EnchantingTypes.PALLADIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:palladium_axe", EnchantingTypes.PALLADIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:palladium_shovel", EnchantingTypes.PALLADIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:palladium_hoe", EnchantingTypes.PALLADIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:palladium_pickaxe", EnchantingTypes.PALLADIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:palladium_helmet", EnchantingTypes.PALLADIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:palladium_chestplate", EnchantingTypes.PALLADIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:palladium_leggings", EnchantingTypes.PALLADIUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:palladium_boots", EnchantingTypes.PALLADIUM.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:prometheum_sword", EnchantingTypes.PROMETHEUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:prometheum_axe", EnchantingTypes.PROMETHEUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:prometheum_shovel", EnchantingTypes.PROMETHEUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:prometheum_hoe", EnchantingTypes.PROMETHEUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:prometheum_pickaxe", EnchantingTypes.PROMETHEUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:prometheum_helmet", EnchantingTypes.PROMETHEUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:prometheum_chestplate", EnchantingTypes.PROMETHEUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:prometheum_leggings", EnchantingTypes.PROMETHEUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:prometheum_boots", EnchantingTypes.PROMETHEUM.getEnchantabilityConfig());

        EnchantabilityConfig quadrillumConfig = new EnchantabilityConfig(5, 2.0f);
        defaultConfig.put("mythicmetals:quadrillum_sword", quadrillumConfig);
        defaultConfig.put("mythicmetals:quadrillum_axe", quadrillumConfig);
        defaultConfig.put("mythicmetals:quadrillum_shovel", quadrillumConfig);
        defaultConfig.put("mythicmetals:quadrillum_hoe", quadrillumConfig);
        defaultConfig.put("mythicmetals:quadrillum_pickaxe", quadrillumConfig);

        defaultConfig.put("mythicmetals:runite_sword", EnchantingTypes.RUNITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:runite_axe", EnchantingTypes.RUNITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:runite_shovel", EnchantingTypes.RUNITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:runite_hoe", EnchantingTypes.RUNITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:runite_pickaxe", EnchantingTypes.RUNITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:runite_helmet", EnchantingTypes.RUNITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:runite_chestplate", EnchantingTypes.RUNITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:runite_leggings", EnchantingTypes.RUNITE.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:runite_boots", EnchantingTypes.RUNITE.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:silver_helmet", EnchantingTypes.SILVER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:silver_chestplate", EnchantingTypes.SILVER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:silver_leggings", EnchantingTypes.SILVER.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:silver_boots", EnchantingTypes.SILVER.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:star_platinum_sword", EnchantingTypes.STAR_PLATINUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:star_platinum_axe", EnchantingTypes.STAR_PLATINUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:star_platinum_shovel", EnchantingTypes.STAR_PLATINUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:star_platinum_hoe", EnchantingTypes.STAR_PLATINUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:star_platinum_pickaxe", EnchantingTypes.STAR_PLATINUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:star_platinum_helmet", EnchantingTypes.STAR_PLATINUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:star_platinum_chestplate", EnchantingTypes.STAR_PLATINUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:star_platinum_leggings", EnchantingTypes.STAR_PLATINUM.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:star_platinum_boots", EnchantingTypes.STAR_PLATINUM.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:steel_sword", EnchantingTypes.STEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:steel_axe", EnchantingTypes.STEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:steel_shovel", EnchantingTypes.STEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:steel_hoe", EnchantingTypes.STEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:steel_pickaxe", EnchantingTypes.STEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:steel_helmet", EnchantingTypes.STEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:steel_chestplate", EnchantingTypes.STEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:steel_leggings", EnchantingTypes.STEEL.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:steel_boots", EnchantingTypes.STEEL.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:stormyx_sword", EnchantingTypes.STORMYX.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:stormyx_axe", EnchantingTypes.STORMYX.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:stormyx_shovel", EnchantingTypes.STORMYX.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:stormyx_hoe", EnchantingTypes.STORMYX.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:stormyx_pickaxe", EnchantingTypes.STORMYX.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:stormyx_helmet", EnchantingTypes.STORMYX.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:stormyx_chestplate", EnchantingTypes.STORMYX.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:stormyx_leggings", EnchantingTypes.STORMYX.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:stormyx_boots", EnchantingTypes.STORMYX.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:stormyx_shield", EnchantingTypes.STORMYX.getEnchantabilityConfig());

        defaultConfig.put("mythicmetals:red_aegis_sword", EnchantingTypes.RED_AEGIS_SWORD.getEnchantabilityConfig());
        defaultConfig.put("mythicmetals:white_aegis_sword", EnchantingTypes.WHITE_AEGIS_SWORD.getEnchantabilityConfig());


        // mythic upgrades
        defaultConfig.put("mythicupgrades:jade_sword", EnchantingTypes.JADE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:jade_axe", EnchantingTypes.JADE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:jade_helmet", EnchantingTypes.JADE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:jade_chestplate", EnchantingTypes.JADE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:jade_leggings", EnchantingTypes.JADE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:jade_boots", EnchantingTypes.JADE.getEnchantabilityConfig());

        defaultConfig.put("mythicupgrades:topaz_sword", EnchantingTypes.TOPAZ.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:topaz_axe", EnchantingTypes.TOPAZ.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:topaz_shovel", EnchantingTypes.TOPAZ.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:topaz_hoe", EnchantingTypes.TOPAZ.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:topaz_pickaxe", EnchantingTypes.TOPAZ.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:topaz_helmet", EnchantingTypes.TOPAZ.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:topaz_chestplate", EnchantingTypes.TOPAZ.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:topaz_leggings", EnchantingTypes.TOPAZ.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:topaz_boots", EnchantingTypes.TOPAZ.getEnchantabilityConfig());

        defaultConfig.put("mythicupgrades:aquamarine_sword", EnchantingTypes.AQUAMARINE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:aquamarine_axe", EnchantingTypes.AQUAMARINE.getEnchantabilityConfig());

        defaultConfig.put("mythicupgrades:sapphire_sword", EnchantingTypes.SAPPHIRE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:sapphire_axe", EnchantingTypes.SAPPHIRE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:sapphire_helmet", EnchantingTypes.SAPPHIRE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:sapphire_chestplate", EnchantingTypes.SAPPHIRE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:sapphire_leggings", EnchantingTypes.SAPPHIRE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:sapphire_boots", EnchantingTypes.SAPPHIRE.getEnchantabilityConfig());

        defaultConfig.put("mythicupgrades:ruby_sword", EnchantingTypes.RUBY.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:ruby_axe", EnchantingTypes.RUBY.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:ruby_shovel", EnchantingTypes.RUBY.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:ruby_hoe", EnchantingTypes.RUBY.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:ruby_pickaxe", EnchantingTypes.RUBY.getEnchantabilityConfig());

        defaultConfig.put("mythicupgrades:ametrine_sword", EnchantingTypes.AMETRINE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:ametrine_axe", EnchantingTypes.AMETRINE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:ametrine_helmet", EnchantingTypes.AMETRINE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:ametrine_chestplate", EnchantingTypes.AMETRINE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:ametrine_leggings", EnchantingTypes.AMETRINE.getEnchantabilityConfig());
        defaultConfig.put("mythicupgrades:ametrine_boots", EnchantingTypes.AMETRINE.getEnchantabilityConfig());


        // aquamirae support
        defaultConfig.put("aquamirae:terrible_sword", EnchantingTypes.TERRIBLE.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:fin_cutter", EnchantingTypes.TERRIBLE.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:terrible_helmet", EnchantingTypes.TERRIBLE.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:terrible_chestplate", EnchantingTypes.TERRIBLE.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:terrible_leggings", EnchantingTypes.TERRIBLE.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:terrible_boots", EnchantingTypes.TERRIBLE.getEnchantabilityConfig());

        defaultConfig.put("aquamirae:divider", EnchantingTypes.BLINDING_ABYSS.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:whisper_of_the_abyss", EnchantingTypes.BLINDING_ABYSS.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:abyssal_heaume", EnchantingTypes.BLINDING_ABYSS.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:abyssal_brigantine", EnchantingTypes.BLINDING_ABYSS.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:abyssal_leggings", EnchantingTypes.BLINDING_ABYSS.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:abyssal_boots", EnchantingTypes.BLINDING_ABYSS.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:abyssal_tiara", EnchantingTypes.BLINDING_ABYSS.getEnchantabilityConfig());

        defaultConfig.put("aquamirae:remnants_saber", EnchantingTypes.THREE_BOLT.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:poisoned_blade", EnchantingTypes.THREE_BOLT.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:three_bolt_helmet", EnchantingTypes.THREE_BOLT.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:three_bolt_suit", EnchantingTypes.THREE_BOLT.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:three_bolt_leggings", EnchantingTypes.THREE_BOLT.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:three_bolt_boots", EnchantingTypes.THREE_BOLT.getEnchantabilityConfig());

        defaultConfig.put("aquamirae:coral_lance", EnchantingTypes.CORAL_LANCE.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:sweet_lance", EnchantingTypes.SWEET_LANCE.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:poisoned_chakra", EnchantingTypes.POISONED_CHAKRAM.getEnchantabilityConfig());
        defaultConfig.put("aquamirae:maze_rose", EnchantingTypes.MAZE_ROSE.getEnchantabilityConfig());


        // misc mods
        defaultConfig.put("amarite:amarite_longsword", EnchantingTypes.AMARITE_LONGSWORD.getEnchantabilityConfig());


        defaultConfig.put("winterly:cryomarble_sword", EnchantingTypes.CRYOMARBLE.getEnchantabilityConfig());
        defaultConfig.put("winterly:cryomarble_axe", EnchantingTypes.CRYOMARBLE.getEnchantabilityConfig());
        defaultConfig.put("winterly:cryomarble_shovel", EnchantingTypes.CRYOMARBLE.getEnchantabilityConfig());
        defaultConfig.put("winterly:cryomarble_hoe", EnchantingTypes.CRYOMARBLE.getEnchantabilityConfig());
        defaultConfig.put("winterly:cryomarble_pickaxe", EnchantingTypes.CRYOMARBLE.getEnchantabilityConfig());


        defaultConfig.put("biomemakeover:cladded_helmet", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("biomemakeover:cladded_chestplate", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("biomemakeover:cladded_leggings", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("biomemakeover:cladded_boots", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());


        defaultConfig.put("galosphere:sterling_helmet", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("galosphere:sterling_chestplate", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("galosphere:sterling_leggings", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("galosphere:sterling_boots", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());


        defaultConfig.put("clutter:white_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:light_gray_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:gray_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:black_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:brown_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:red_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:orange_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:yellow_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:lime_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:green_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:cyan_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:light_blue_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:blue_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:purple_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:magenta_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:pink_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:crimson_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:warped_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());
        defaultConfig.put("clutter:soul_butterfly_elytra", EnchantingTypes.ELYTRA.getEnchantabilityConfig());

        defaultConfig.put("clutter:silver_helmet", EnchantingTypes.CHAINMAIL_TIER.getEnchantabilityConfig());
        defaultConfig.put("clutter:silver_chestplate", EnchantingTypes.CHAINMAIL_TIER.getEnchantabilityConfig());
        defaultConfig.put("clutter:silver_leggings", EnchantingTypes.CHAINMAIL_TIER.getEnchantabilityConfig());
        defaultConfig.put("clutter:silver_boots", EnchantingTypes.CHAINMAIL_TIER.getEnchantabilityConfig());


        defaultConfig.put("frostiful:fur_helmet", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("frostiful:fur_chestplate", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("frostiful:fur_leggings", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("frostiful:fur_boots", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());
        defaultConfig.put("frostiful:ice_skates", EnchantingTypes.LEATHER_TIER.getEnchantabilityConfig());


        defaultConfig.put("frostiful:fur_padded_chainmail_helmet", EnchantingTypes.CHAINMAIL_TIER.getEnchantabilityConfig());
        defaultConfig.put("frostiful:fur_padded_chainmail_chestplate", EnchantingTypes.CHAINMAIL_TIER.getEnchantabilityConfig());
        defaultConfig.put("frostiful:fur_padded_chainmail_leggings", EnchantingTypes.CHAINMAIL_TIER.getEnchantabilityConfig());
        defaultConfig.put("frostiful:fur_padded_chainmail_boots", EnchantingTypes.CHAINMAIL_TIER.getEnchantabilityConfig());
        defaultConfig.put("frostiful:armored_ice_skates", EnchantingTypes.CHAINMAIL_TIER.getEnchantabilityConfig());


        defaultConfig.put("create:netherite_diving_helmet", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("create:netherite_diving_boots", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());

        defaultConfig.put("create:copper_diving_helmet", EnchantingTypes.COPPER.getEnchantabilityConfig());
        defaultConfig.put("create:copper_diving_boots", EnchantingTypes.COPPER.getEnchantabilityConfig());


        defaultConfig.put("garnished:wooden_hatchet", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("garnished:stone_hatchet", EnchantingTypes.STONE_TIER.getEnchantabilityConfig());
        defaultConfig.put("garnished:iron_hatchet", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());
        defaultConfig.put("garnished:golden_hatchet", EnchantingTypes.GOLD_TIER.getEnchantabilityConfig());
        defaultConfig.put("garnished:diamond_hatchet", EnchantingTypes.DIAMOND_TIER.getEnchantabilityConfig());
        defaultConfig.put("garnished:netherite_hatchet", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("garnished:jade_hatchet", EnchantingTypes.JADE.getEnchantabilityConfig());
        defaultConfig.put("garnished:topaz_hatchet", EnchantingTypes.TOPAZ.getEnchantabilityConfig());
        defaultConfig.put("garnished:sapphire_hatchet", EnchantingTypes.SAPPHIRE.getEnchantabilityConfig());
        defaultConfig.put("garnished:ruby_hatchet", EnchantingTypes.RUBY.getEnchantabilityConfig());
        defaultConfig.put("garnished:ametrine_hatchet", EnchantingTypes.AMETRINE.getEnchantabilityConfig());
        defaultConfig.put("garnished:aquamarine_hatchet", EnchantingTypes.AQUAMARINE.getEnchantabilityConfig());


        defaultConfig.put("illagerinvasion:platinum_infused_hatchet", EnchantingTypes.IRON_TIER.getEnchantabilityConfig());


        defaultConfig.put("guarding:netherite_shield", EnchantingTypes.SHIELD.getEnchantabilityConfig());


        defaultConfig.put("crossbow_expansion:hand_crossbow", EnchantingTypes.CROSSBOW.getEnchantabilityConfig());
        defaultConfig.put("crossbow_expansion:rage_crossbow", EnchantingTypes.CROSSBOW.getEnchantabilityConfig());
        defaultConfig.put("crossbow_expansion:cluster_crossbow", EnchantingTypes.CROSSBOW.getEnchantabilityConfig());
        defaultConfig.put("crossbow_expansion:pouch_crossbow", EnchantingTypes.CROSSBOW.getEnchantabilityConfig());
        defaultConfig.put("crossbow_expansion:warden_crossbow", EnchantingTypes.CROSSBOW.getEnchantabilityConfig());

        defaultConfig.put("simplyswords:brimstone_claymore", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:watcher_claymore", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:storms_edge", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:stormbringer", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:sword_on_a_stick", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:bramblethorn", EnchantingTypes.WOOD_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:watching_warglaive", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:toxic_longsword", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:emberblade", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:hearthflame", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:soulkeeper", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:twisted_blade", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:soulstealer", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:soulrender", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:soulpyre", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:frostfall", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:molten_edge", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:livyatan", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:icewhisper", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:arcanethyst", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:thunderbrand", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:mjolnir", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:slumbering_lichblade", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:waking_lichblade", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:awakened_lichblade", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:shadowsting", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:dormant_relic", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:righteous_relic", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:tainted_relic", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:sunfire", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:harbinger", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:whisperwind", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:emberlash", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:waxweaver", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:hiveheart", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:stars_edge", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:wickpiercer", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());
        defaultConfig.put("simplyswords:tempest", EnchantingTypes.NETHERITE_TIER.getEnchantabilityConfig());





        File directory = configFile.getParentFile();
        if(directory.exists()) {
            try (FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(defaultConfig, writer);
            } catch (IOException e) {
                System.out.println("INDEXED: Could not generate new config file!");
                throw new RuntimeException("Could not save config file", e);
            }
        } else {
            System.out.println("INDEXED: Directory does not exist!");
        }

    }
}
