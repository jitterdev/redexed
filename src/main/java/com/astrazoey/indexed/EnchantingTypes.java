package com.astrazoey.indexed;

public class EnchantingTypes {
    public static final EnchantingType GENERIC;

    public static final EnchantingType NETHERITE_TIER;
    public static final EnchantingType DIAMOND_TIER;
    public static final EnchantingType IRON_TIER;
    public static final EnchantingType GOLD_TIER;
    public static final EnchantingType STONE_TIER;
    public static final EnchantingType WOOD_TIER;
    public static final EnchantingType LEATHER_TIER;
    public static final EnchantingType CHAINMAIL_TIER;

    public static final EnchantingType FISHING_ROD;
    public static final EnchantingType CROSSBOW;
    public static final EnchantingType BOW;
    public static final EnchantingType TRIDENT;
    public static final EnchantingType TURTLE_HELMET;

    public static final EnchantingType ELYTRA;
    public static final EnchantingType SHEARS;
    public static final EnchantingType FLINT_AND_STEEL;
    public static final EnchantingType SHIELD;

    //Modded Types
    public static final EnchantingType ELYTRA_MODIFIED;
    public static final EnchantingType ROSE_GOLD_TIER;
    public static final EnchantingType NETHERITE_GILDED;
    public static final EnchantingType BONE_TIER;

    //Better End Types
    public static final EnchantingType TERMINITE;
    public static final EnchantingType THALLASIUM;
    public static final EnchantingType AETERNIUM;
    public static final EnchantingType CRYSTALITE;

    //Better Nether Types
    public static final EnchantingType NETHER_RUBY;
    public static final EnchantingType CINCINNASITE;
    public static final EnchantingType CINCINNASITE_DIAMOND;

    //Dragon Loot Types
    public static final EnchantingType DRAGON;

    //Conjuring Types
    public static final EnchantingType SOUL_ALLOY;

    // MM Types (Armor)
    public static final EnchantingType ADAMANTITE;
    public static final EnchantingType AQUARIUM;
    public static final EnchantingType TIDESINGER;
    public static final EnchantingType BANGLUM;
    public static final EnchantingType BRONZE;
    public static final EnchantingType CARMOT;
    public static final EnchantingType CELESTIUM;
    public static final EnchantingType COPPER;
    public static final EnchantingType DURASTEEL;
    public static final EnchantingType HALLOWED;
    public static final EnchantingType KYBER;
    public static final EnchantingType LEGENDARY_BANGLUM;
    public static final EnchantingType METALLURGIUM;
    public static final EnchantingType MIDAS_GOLD;
    public static final EnchantingType MYTHRIL;
    public static final EnchantingType ORICHALCUM;
    public static final EnchantingType OSMIUM;
    public static final EnchantingType OSMIUM_CHAINMAIL;
    public static final EnchantingType PALLADIUM;
    public static final EnchantingType PROMETHEUM;
    public static final EnchantingType RUNITE;
    public static final EnchantingType SILVER;
    public static final EnchantingType STAR_PLATINUM;
    public static final EnchantingType STEEL;
    public static final EnchantingType STORMYX;
    // MM Types (Special)
    public static final EnchantingType RED_AEGIS_SWORD;
    public static final EnchantingType WHITE_AEGIS_SWORD;
    public static final EnchantingType ORICHALCUM_HAMMER;
    public static final EnchantingType MYTHRIL_DRILL;
    public static final EnchantingType GILDED_MIDAS;
    public static final EnchantingType ROYAL_MIDAS;

    // Mythic Upgrades Types
    public static final EnchantingType JADE;
    public static final EnchantingType TOPAZ;
    public static final EnchantingType AQUAMARINE;
    public static final EnchantingType SAPPHIRE;
    public static final EnchantingType RUBY;
    public static final EnchantingType AMETRINE;

    // Amarite Types
    public static final EnchantingType AMARITE_LONGSWORD;

    // Winterly Types
    public static final EnchantingType CRYOMARBLE;

    // Aquamirae Types
    public static final EnchantingType TERRIBLE;
    public static final EnchantingType BLINDING_ABYSS;
    public static final EnchantingType THREE_BOLT;
    public static final EnchantingType CORAL_LANCE;
    public static final EnchantingType SWEET_LANCE;
    public static final EnchantingType POISONED_CHAKRAM;
    public static final EnchantingType MAZE_ROSE;



    static {
        GENERIC = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(5).repairScaling(1f));

        NETHERITE_TIER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(10).repairScaling(1f));
        DIAMOND_TIER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(10).repairScaling(0.5f));
        IRON_TIER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(12).repairScaling(2f));
        GOLD_TIER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(18).repairScaling(1f));
        STONE_TIER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(14).repairScaling(2f));
        CHAINMAIL_TIER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(14).repairScaling(1f));
        WOOD_TIER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(16).repairScaling(2f));
        LEATHER_TIER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(16).repairScaling(1f));


        FISHING_ROD = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(8).repairScaling(4f));
        CROSSBOW = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(7).repairScaling(4f));
        BOW = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(8).repairScaling(4f));
        TRIDENT = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(8).repairScaling(0.5f));
        TURTLE_HELMET = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(15).repairScaling(0.5f));

        ELYTRA = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(6).repairScaling(1f));
        SHEARS = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(5).repairScaling(1f));
        FLINT_AND_STEEL = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(5).repairScaling(1f));
        SHIELD = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(5).repairScaling(1f));

        //Modded Support
        ELYTRA_MODIFIED = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(5).repairScaling(1f));
        ROSE_GOLD_TIER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(18).repairScaling(0.5f));
        BONE_TIER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(5).repairScaling(1f));
        NETHERITE_GILDED = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(12).repairScaling(0.3f));

        //Better End Support
        AETERNIUM = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(8).repairScaling(1f));
        CRYSTALITE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(6).repairScaling(1f));
        TERMINITE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(12).repairScaling(0.5f));
        THALLASIUM = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(12).repairScaling(1f));

        //Better Nether Support
        NETHER_RUBY = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(11).repairScaling(1f));
        CINCINNASITE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(8).repairScaling(1f));
        CINCINNASITE_DIAMOND = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(8).repairScaling(2f));

        //Dragon Loot Support
        DRAGON = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(8).repairScaling(1f));

        //Conjuring Support
        SOUL_ALLOY = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(15).repairScaling(1f));

        //Mythic Metals Support
        ADAMANTITE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(10).repairScaling(1f));
        AQUARIUM = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(15).repairScaling(2f));
        TIDESINGER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(17).repairScaling(3f));
        BANGLUM = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(5).repairScaling(4f));
        BRONZE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(8).repairScaling(1f));
        CARMOT = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(16).repairScaling(1.5f));
        CELESTIUM = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(18).repairScaling(0.5f));
        COPPER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(12).repairScaling(1f));
        DURASTEEL = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(8).repairScaling(1f));
        HALLOWED = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(15).repairScaling(2f));
        KYBER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(16).repairScaling(1f));
        LEGENDARY_BANGLUM = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(10).repairScaling(4f));
        METALLURGIUM = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(16).repairScaling(2f));
        MIDAS_GOLD = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(24).repairScaling(3f));
        MYTHRIL = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(10).repairScaling(1f));
        ORICHALCUM = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(10).repairScaling(1f));
        OSMIUM = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(12).repairScaling(1f));
        OSMIUM_CHAINMAIL = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(15).repairScaling(1f));
        PALLADIUM = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(20).repairScaling(2f));
        PROMETHEUM = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(16).repairScaling(5f));
        RUNITE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(15).repairScaling(1f));
        SILVER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(9).repairScaling(1f));
        STAR_PLATINUM = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(22).repairScaling(3f));
        STEEL = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(8).repairScaling(1.5f));
        STORMYX = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(24).repairScaling(1f));

        RED_AEGIS_SWORD = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(20).repairScaling(2f));
        WHITE_AEGIS_SWORD = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(20).repairScaling(2f));
        ORICHALCUM_HAMMER = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(12).repairScaling(0.5f));
        MYTHRIL_DRILL = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(15).repairScaling(0.5f));
        GILDED_MIDAS = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(26).repairScaling(3f));
        ROYAL_MIDAS = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(28).repairScaling(3f));

        //Mythic Upgrades Support
        JADE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(20).repairScaling(3f));
        TOPAZ = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(20).repairScaling(3f));
        AQUAMARINE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(20).repairScaling(3f));
        SAPPHIRE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(20).repairScaling(3f));
        RUBY = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(20).repairScaling(3f));
        AMETRINE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(20).repairScaling(3f));

        //Amarite Support
        AMARITE_LONGSWORD = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(20).repairScaling(2f));

        //Winterly Support
        CRYOMARBLE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(14).repairScaling(1.5f));

        //Aquamirae Support
        TERRIBLE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(8).repairScaling(1f));
        BLINDING_ABYSS = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(16).repairScaling(1f));
        THREE_BOLT = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(10).repairScaling(1f));
        CORAL_LANCE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(20).repairScaling(1f));
        SWEET_LANCE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(22).repairScaling(1f));
        POISONED_CHAKRAM = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(10).repairScaling(1f));
        MAZE_ROSE = new EnchantingType(new EnchantingType.Settings().maxEnchantingSlots(15).repairScaling(1f));
    }

}
