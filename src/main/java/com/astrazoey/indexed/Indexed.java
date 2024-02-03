package com.astrazoey.indexed;

import com.astrazoey.indexed.blocks.CrystalGlobeBlock;
import com.astrazoey.indexed.criterion.*;
import com.astrazoey.indexed.enchantments.*;
import com.astrazoey.indexed.registry.IndexedItems;
import com.astrazoey.indexed.status_effects.EnchantedStatusEffect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;


public class Indexed implements ModInitializer {

    public static final Identifier CONFIG_PACKET = new Identifier("indexed", "config");

    public static final String MOD_ID = "indexed";

    //Enchantments
    public static Enchantment SLOW_BURN = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier("indexed", "slow_burn"),
            new SlowBurnEnchantment()
    );

    public static Enchantment QUICK_FLIGHT = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier("indexed", "quick_flight"),
            new SlowBurnEnchantment()
    );

    public static Enchantment FORGERY = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier("indexed", "forgery"),
            new ForgeryEnchantment()
    );

    public static Enchantment MYSTERY_CURSE = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier("indexed", "mystery_curse"),
            new MysteryCurseEnchantment()
    );

    public static Enchantment HIDDEN_CURSE = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier("indexed", "hidden_curse"),
            new HiddenCurseEnchantment()
    );

    public static Enchantment KEEPING = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier("indexed", "keeping"),
            new KeepingEnchantment()
    );

    public static Enchantment ESSENCE = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier("indexed", "essence"),
            new EssenceEnchantment()
    );


    //Blocks
    public static final Block CRYSTAL_GLOBE = new CrystalGlobeBlock(FabricBlockSettings.create()
            .strength(1.5f)
            .hardness(1.5f)
            .luminance(CrystalGlobeBlock.STATE_TO_LUMINANCE)
            .sounds(BlockSoundGroup.AMETHYST_BLOCK)
//          .breakByHand(true)
            .nonOpaque()
    );

    //Sounds
    public static final Identifier CRYSTAL_USE_SOUND = new Identifier("indexed","use_crystal_globe");
    public static SoundEvent CRYSTAL_USE_SOUND_EVENT = SoundEvent.of(CRYSTAL_USE_SOUND);
    public static final Identifier CRYSTAL_HARVEST_SOUND = new Identifier("indexed","harvest_crystal_globe");
    public static SoundEvent CRYSTAL_HARVEST_SOUND_EVENT = SoundEvent.of(CRYSTAL_HARVEST_SOUND);
    public static final Identifier CRYSTAL_AMBIENT_SOUND = new Identifier("indexed","crystal_globe_ambient");
    public static SoundEvent CRYSTAL_AMBIENT_SOUND_EVENT = SoundEvent.of(CRYSTAL_AMBIENT_SOUND);

    //Particles
    public static final DefaultParticleType CRYSTAL_HARVEST = FabricParticleTypes.simple();
    public static final DefaultParticleType CRYSTAL_BREAK = FabricParticleTypes.simple();

    //Status Effects
    public static final StatusEffect ENCHANTED_STATUS_EFFECT = new EnchantedStatusEffect(StatusEffectCategory.BENEFICIAL, 0xD400FF);

    //Loot Tables
    public static Identifier INDEXED_LOOT = new Identifier("indexed", "indexed_items");
    public static Identifier INDEXED_NETHER_BRIDGE = new Identifier("indexed", "indexed_nether_bridge");
    public static Identifier INDEXED_OUTPOST = new Identifier("indexed", "indexed_outpost");
    public static Identifier INDEXED_MANSION = new Identifier("indexed", "indexed_mansion");
    public static Identifier INDEXED_MINESHAFT = new Identifier("indexed", "indexed_mineshaft");
    public static Identifier INDEXED_SHIPWRECK = new Identifier("indexed", "indexed_shipwreck");
    public static Identifier INDEXED_TEMPLE = new Identifier("indexed", "indexed_temple");
    public static Identifier INDEXED_BURIED_TREASURE = new Identifier("indexed", "indexed_buried_treasure");
    public static Identifier INDEXED_WATER_RUIN = new Identifier("indexed", "indexed_water_ruin");



    //Criterion
    public static OverchargeItemCriterion OVERCHARGE_ITEM = new OverchargeItemCriterion();
    public static EnchantGoldBookCriterion ENCHANT_GOLD_BOOK = new EnchantGoldBookCriterion();
    public static RepairItemCriterion REPAIR_ITEM = new RepairItemCriterion();
    public static GrindEssenceCriterion GRIND_ESSENCE = new GrindEssenceCriterion();
    public static MultishotCrossbowCriterion MULTISHOT_CROSSBOW = new MultishotCrossbowCriterion();
    public static MaxGoldCriterion MAX_GOLD = new MaxGoldCriterion();
    public static MaxKnockbackCriterion MAX_KNOCKBACK = new MaxKnockbackCriterion();
    public static UseCrystalGlobeCriterion USE_CRYSTAL_GLOBE = new UseCrystalGlobeCriterion();
    public static FillCrystalGlobeCriterion FILL_CRYSTAL_GLOBE = new FillCrystalGlobeCriterion();
    public static EnchantedCriterion ENCHANTED_ADVANCEMENT = new EnchantedCriterion();



    @Override
    public void onInitialize() {

        IndexedItems.registerItems();

        //Blocks
        Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "crystal_globe"), CRYSTAL_GLOBE);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "crystal_globe"), new BlockItem(CRYSTAL_GLOBE, new FabricItemSettings()));


        //Sounds
        Registry.register(Registries.SOUND_EVENT, CRYSTAL_USE_SOUND, CRYSTAL_USE_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, CRYSTAL_HARVEST_SOUND, CRYSTAL_HARVEST_SOUND_EVENT);
        Registry.register(Registries.SOUND_EVENT, CRYSTAL_AMBIENT_SOUND, CRYSTAL_AMBIENT_SOUND_EVENT);

        //Particles
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "crystal_harvest"), CRYSTAL_HARVEST);
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(MOD_ID, "crystal_break"), CRYSTAL_BREAK);


        //Status Effects
        Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "enchanted"), ENCHANTED_STATUS_EFFECT);

        //Criterion Registration
        Criteria.register(OVERCHARGE_ITEM);
        Criteria.register(ENCHANT_GOLD_BOOK);
        Criteria.register(REPAIR_ITEM);
        Criteria.register(GRIND_ESSENCE);
        Criteria.register(MULTISHOT_CROSSBOW);
        Criteria.register(MAX_GOLD);
        Criteria.register(MAX_KNOCKBACK);
        Criteria.register(USE_CRYSTAL_GLOBE);
        Criteria.register(FILL_CRYSTAL_GLOBE);
        Criteria.register(ENCHANTED_ADVANCEMENT);


        //Ores Drop Experience
        SetOreExperience.set(Blocks.COPPER_ORE, UniformIntProvider.create(1,3));
        SetOreExperience.set(Blocks.DEEPSLATE_COAL_ORE, UniformIntProvider.create(1,3));
        SetOreExperience.set(Blocks.IRON_ORE, UniformIntProvider.create(1,3));
        SetOreExperience.set(Blocks.DEEPSLATE_IRON_ORE, UniformIntProvider.create(1,3));
        SetOreExperience.set(Blocks.GOLD_ORE, UniformIntProvider.create(2,4));
        SetOreExperience.set(Blocks.DEEPSLATE_GOLD_ORE, UniformIntProvider.create(2,4));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
            content.add(IndexedItems.GOLD_BOUND_BOOK);
            content.add(CRYSTAL_GLOBE.asItem());

        });

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            NbtCompound nbtCompound = stack.getOrCreateNbt();
            if (nbtCompound.getBoolean("Unbreakable") && MaxEnchantingSlots.getCurrent(stack) > MaxEnchantingSlots.getEnchantType(stack).getMaxEnchantingSlots()) {
                stack.addHideFlag(ItemStack.TooltipSection.UNBREAKABLE);
                lines.add(Text.translatable("item.unbreakable").formatted(Formatting.RED).formatted(Formatting.STRIKETHROUGH));
            } else if (nbtCompound.getBoolean("Unbreakable") && nbtCompound.contains("HideFlags", NbtElement.NUMBER_TYPE) && MaxEnchantingSlots.getCurrent(stack) <= MaxEnchantingSlots.getEnchantType(stack).getMaxEnchantingSlots() && lines.contains(Text.translatable("item.unbreakable").formatted(Formatting.RED).formatted(Formatting.STRIKETHROUGH))) {
                int flags = nbtCompound.getInt("HideFlags");
                nbtCompound.putInt("HideFlags", flags & ~ItemStack.TooltipSection.UNBREAKABLE.getFlag());
                lines.remove(Text.translatable("item.unbreakable").formatted(Formatting.RED).formatted(Formatting.STRIKETHROUGH));
            }
        });

        //Add Items to Chests
//        LootTableLoadingCallback.EVENT.register(((resourceManager, manager, id, supplier, setter) -> {
//            if(LootTables.END_CITY_TREASURE_CHEST.equals(id) ||
//                    LootTables.ABANDONED_MINESHAFT_CHEST.equals(id) ||
//                    LootTables.STRONGHOLD_LIBRARY_CHEST.equals(id) ||
//                    LootTables.BASTION_TREASURE_CHEST.equals(id) ||
//                    LootTables.WOODLAND_MANSION_CHEST.equals(id) ||
//                    LootTables.NETHER_BRIDGE_CHEST.equals(id) ||
//                    LootTables.PILLAGER_OUTPOST_CHEST.equals(id) ||
//                    LootTables.RUINED_PORTAL_CHEST.equals(id)) {
//                supplier.copyFrom(manager.getLootTable(INDEXED_LOOT));
//
//            }
//        }));
//
//        LootTableEvents.MODIFY.register((resourceManager, manager, id, tableBuilder, source) -> {
//            if(LootTables.NETHER_BRIDGE_CHEST.equals(id)) {
//                LootTable table = manager.getLootTable(INDEXED_NETHER_BRIDGE);
//                for (LootPool pool : table.pools) {
//                    tableBuilder.pool(pool);
//                }
//                return FabricLootTableBuilder.copyOf(manager.getLootTable(INDEXED_NETHER_BRIDGE)).build();
//            }
//            return null;
//        });
//
//        LootTableEvents.REPLACE.register((resourceManager, manager, id, original, source) -> {
//            if(LootTables.NETHER_BRIDGE_CHEST.equals(id)) {
//                return FabricLootTableBuilder.copyOf(manager.getLootTable(INDEXED_NETHER_BRIDGE)).build();
//            }
//            return null;
//        });
//
//        LootTableEvents.REPLACE.register((resourceManager, manager, id, original, source) -> {
//            if(LootTables.PILLAGER_OUTPOST_CHEST.equals(id)) {
//                return FabricLootTableBuilder.copyOf(manager.getLootTable(INDEXED_OUTPOST)).build();
//            }
//            return null;
//        });
//
//        LootTableEvents.REPLACE.register((resourceManager, manager, id, original, source) -> {
//            if(LootTables.WOODLAND_MANSION_CHEST.equals(id)) {
//                return FabricLootTableBuilder.copyOf(manager.getLootTable(INDEXED_MANSION)).build();
//            }
//            return null;
//        });
//
//        LootTableEvents.REPLACE.register((resourceManager, manager, id, original, source) -> {
//            if(LootTables.ABANDONED_MINESHAFT_CHEST.equals(id)) {
//                return FabricLootTableBuilder.copyOf(manager.getLootTable(INDEXED_MINESHAFT)).build();
//            }
//            return null;
//        });
//
//        LootTableEvents.REPLACE.register((resourceManager, manager, id, original, source) -> {
//            if(LootTables.SHIPWRECK_TREASURE_CHEST.equals(id)) {
//                return FabricLootTableBuilder.copyOf(manager.getLootTable(INDEXED_SHIPWRECK)).build();
//            }
//            return null;
//        });
//
//        LootTableEvents.REPLACE.register((resourceManager, manager, id, original, source) -> {
//            if(LootTables.WOODLAND_MANSION_CHEST.equals(id)) {
//                return FabricLootTableBuilder.copyOf(manager.getLootTable(INDEXED_MANSION)).build();
//            }
//            return null;
//        });
//
//        LootTableEvents.REPLACE.register((resourceManager, manager, id, original, source) -> {
//            if(LootTables.BURIED_TREASURE_CHEST.equals(id)) {
//                return FabricLootTableBuilder.copyOf(manager.getLootTable(INDEXED_BURIED_TREASURE)).build();
//            }
//            return null;
//        });
//
//        LootTableEvents.REPLACE.register((resourceManager, manager, id, original, source) -> {
//            if(LootTables.UNDERWATER_RUIN_BIG_CHEST.equals(id) ||
//                    LootTables.UNDERWATER_RUIN_SMALL_CHEST.equals(id)) {
//                return FabricLootTableBuilder.copyOf(manager.getLootTable(INDEXED_WATER_RUIN)).build();
//            }
//            return null;
//        });
//
//        LootTableEvents.REPLACE.register((resourceManager, manager, id, original, source) -> {
//            if(LootTables.JUNGLE_TEMPLE_CHEST.equals(id) ||
//                    LootTables.DESERT_PYRAMID_CHEST.equals(id)) {
//                return FabricLootTableBuilder.copyOf(manager.getLootTable(INDEXED_TEMPLE)).build();
//            }
//            return null;
//        });


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
