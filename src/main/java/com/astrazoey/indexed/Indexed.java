package com.astrazoey.indexed;

import com.astrazoey.indexed.blocks.CrystalGlobeBlock;
import com.astrazoey.indexed.criterion.*;
import com.astrazoey.indexed.enchantments.*;
import com.astrazoey.indexed.mixins.CriterionRegistryAccessor;
import com.astrazoey.indexed.registry.IndexedItems;
import com.astrazoey.indexed.status_effects.EnchantedStatusEffect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.*;
import net.minecraft.loot.*;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import org.lwjgl.system.CallbackI;

import java.util.Iterator;
import java.util.Map;


public class Indexed implements ModInitializer {

    public static final Identifier CONFIG_PACKET = new Identifier("indexed", "config");

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


    //Blocks
    public static final Block CRYSTAL_GLOBE = new CrystalGlobeBlock(FabricBlockSettings.
            of(Material.AMETHYST).
            strength(1.5f).
            hardness(1.5f).
            luminance(3).
            sounds(BlockSoundGroup.AMETHYST_BLOCK).
            breakByHand(true)
    );

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



    @Override
    public void onInitialize() {

        IndexedItems.registerItems();

        //Blocks
        Registry.register(Registry.BLOCK, new Identifier("indexed", "crystal_globe"), CRYSTAL_GLOBE);
        Registry.register(Registry.ITEM, new Identifier("indexed", "crystal_globe"), new BlockItem(CRYSTAL_GLOBE, new FabricItemSettings().group(ItemGroup.MISC)));


        //Status Effects
        Registry.register(Registry.STATUS_EFFECT, new Identifier("indexed", "enchanted"), ENCHANTED_STATUS_EFFECT);

        //Criterion Registration
        CriterionRegistryAccessor.registerCriterion(OVERCHARGE_ITEM);
        CriterionRegistryAccessor.registerCriterion(ENCHANT_GOLD_BOOK);
        CriterionRegistryAccessor.registerCriterion(REPAIR_ITEM);
        CriterionRegistryAccessor.registerCriterion(GRIND_ESSENCE);
        CriterionRegistryAccessor.registerCriterion(MULTISHOT_CROSSBOW);
        CriterionRegistryAccessor.registerCriterion(MAX_GOLD);
        CriterionRegistryAccessor.registerCriterion(MAX_KNOCKBACK);

        //Ores Drop Experience
        SetOreExperience.set(Blocks.COPPER_ORE, UniformIntProvider.create(1,3));
        SetOreExperience.set(Blocks.DEEPSLATE_COAL_ORE, UniformIntProvider.create(1,3));
        SetOreExperience.set(Blocks.IRON_ORE, UniformIntProvider.create(1,3));
        SetOreExperience.set(Blocks.DEEPSLATE_IRON_ORE, UniformIntProvider.create(1,3));
        SetOreExperience.set(Blocks.GOLD_ORE, UniformIntProvider.create(2,4));
        SetOreExperience.set(Blocks.DEEPSLATE_GOLD_ORE, UniformIntProvider.create(2,4));

        //Add Items to Chests
        LootTableLoadingCallback.EVENT.register(((resourceManager, manager, id, supplier, setter) -> {
            if(LootTables.END_CITY_TREASURE_CHEST.equals(id) ||
                    LootTables.ABANDONED_MINESHAFT_CHEST.equals(id) ||
                    LootTables.STRONGHOLD_LIBRARY_CHEST.equals(id) ||
                    LootTables.BASTION_TREASURE_CHEST.equals(id) ||
                    LootTables.WOODLAND_MANSION_CHEST.equals(id) ||
                    LootTables.NETHER_BRIDGE_CHEST.equals(id) ||
                    LootTables.PILLAGER_OUTPOST_CHEST.equals(id) ||
                    LootTables.RUINED_PORTAL_CHEST.equals(id)) {
                supplier.copyFrom(manager.getTable(INDEXED_LOOT));

            }
        }));

        LootTableLoadingCallback.EVENT.register(((resourceManager, manager, id, supplier, setter) -> {
            if(LootTables.NETHER_BRIDGE_CHEST.equals(id)) {
                supplier.copyFrom(manager.getTable(INDEXED_NETHER_BRIDGE));

            }
        }));

        LootTableLoadingCallback.EVENT.register(((resourceManager, manager, id, supplier, setter) -> {
            if(LootTables.PILLAGER_OUTPOST_CHEST.equals(id)) {
                supplier.copyFrom(manager.getTable(INDEXED_OUTPOST));
            }
        }));

        LootTableLoadingCallback.EVENT.register(((resourceManager, manager, id, supplier, setter) -> {
            if(LootTables.WOODLAND_MANSION_CHEST.equals(id)) {
                supplier.copyFrom(manager.getTable(INDEXED_MANSION));
            }
        }));

        LootTableLoadingCallback.EVENT.register(((resourceManager, manager, id, supplier, setter) -> {
            if(LootTables.ABANDONED_MINESHAFT_CHEST.equals(id)) {
                supplier.copyFrom(manager.getTable(INDEXED_MINESHAFT));
            }
        }));

        LootTableLoadingCallback.EVENT.register(((resourceManager, manager, id, supplier, setter) -> {
            if(LootTables.SHIPWRECK_TREASURE_CHEST.equals(id)) {
                supplier.copyFrom(manager.getTable(INDEXED_SHIPWRECK));
            }
        }));

        LootTableLoadingCallback.EVENT.register(((resourceManager, manager, id, supplier, setter) -> {
            if(LootTables.BURIED_TREASURE_CHEST.equals(id)) {
                supplier.copyFrom(manager.getTable(INDEXED_BURIED_TREASURE));
            }
        }));

        LootTableLoadingCallback.EVENT.register(((resourceManager, manager, id, supplier, setter) -> {
            if(LootTables.UNDERWATER_RUIN_BIG_CHEST.equals(id) ||
                    LootTables.UNDERWATER_RUIN_SMALL_CHEST.equals(id)) {
                supplier.copyFrom(manager.getTable(INDEXED_WATER_RUIN));
            }
        }));

        LootTableLoadingCallback.EVENT.register(((resourceManager, manager, id, supplier, setter) -> {
            if(LootTables.JUNGLE_TEMPLE_CHEST.equals(id) ||
                LootTables.DESERT_PYRAMID_CHEST.equals(id)) {
                supplier.copyFrom(manager.getTable(INDEXED_TEMPLE));
            }
        }));

        //Crystal Usage
        /*
        UseBlockCallback.EVENT.register((player, world, hand, hitresult) -> {
            var pos = hitresult.getBlockPos();
            var block = world.getBlockState(pos);
            if((block.isOf(Blocks.AMETHYST_CLUSTER)) && (player.getStackInHand(hand).hasEnchantments() || player.getStackInHand(hand).isOf(Items.ENCHANTED_BOOK))) {
                ItemStack heldItem = player.getStackInHand(hand);
                Map<Enchantment, Integer> enchantmentIntegerMap = EnchantmentHelper.get(heldItem);
                Map<Enchantment, Integer> newEnchantingMap = EnchantmentHelper.get(heldItem);
                newEnchantingMap.clear();

                for(Enchantment i : enchantmentIntegerMap.keySet()) {
                    int enchantmentLevel = enchantmentIntegerMap.get(i);
                    enchantmentLevel--;
                    System.out.println("Enchantment level = " + enchantmentLevel);
                    if(enchantmentLevel <= 0) {
                        System.out.println("Removing enchantment" + i);
                        //enchantmentIntegerMap.remove(i);

                    } else {
                        System.out.println("Reducing enchantment " + i);
                        //enchantmentIntegerMap.put(i,enchantmentLevel);
                        newEnchantingMap.put(i,enchantmentLevel);
                    }
                }

                EnchantmentHelper.set(newEnchantingMap, heldItem);
                if(heldItem.isOf(Items.ENCHANTED_BOOK)) {
                    NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(heldItem);
                    nbtList.clear();
                    for(Enchantment i : newEnchantingMap.keySet()) {
                        Identifier identifier = EnchantmentHelper.getEnchantmentId(i);
                        nbtList.add(EnchantmentHelper.createNbt(identifier, newEnchantingMap.get(i)));
                    }
                    heldItem.getOrCreateNbt().put("StoredEnchantments", nbtList);
                    if(heldItem.getNbt() != null) {
                        if (heldItem.getNbt().getList("StoredEnchantments", 10).isEmpty()) {
                            ItemStack newItem = Items.BOOK.getDefaultStack();
                            newItem.setNbt(heldItem.getNbt());
                            player.setStackInHand(hand, newItem);
                        }
                    }

                }

                world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1f, 1.5f);
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.PASS;
            }
        });
        */

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
