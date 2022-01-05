package com.astrazoey.indexed.blocks;

import com.astrazoey.indexed.Indexed;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Random;


public class CrystalGlobeBlock extends Block {

    public static final int MAX_LEVEL = 8;
    public static final IntProperty LEVEL = Properties.LEVEL_8;

    public CrystalGlobeBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)this.stateManager.getDefaultState().with(LEVEL, 0));
    }

    private double lerpDouble(double start, double finish, double alpha) {
        return start + alpha * (finish - start);
    }

    private Vector3d lerpVector(Vector3d start, Vector3d finish, double alpha) {
        return new Vector3d(
                lerpDouble(start.x, finish.x, alpha),
                lerpDouble(start.y, finish.y, alpha),
                lerpDouble(start.z, finish.z, alpha));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitresult) {
        if((player.getStackInHand(hand).hasEnchantments() || player.getStackInHand(hand).isOf(Items.ENCHANTED_BOOK))) {
            ItemStack heldItem = player.getStackInHand(hand);
            Map<Enchantment, Integer> enchantmentIntegerMap = EnchantmentHelper.get(heldItem);
            Map<Enchantment, Integer> newEnchantingMap = EnchantmentHelper.get(heldItem);
            newEnchantingMap.clear();

            for(Enchantment i : enchantmentIntegerMap.keySet()) {
                int enchantmentLevel = enchantmentIntegerMap.get(i);
                enchantmentLevel--;
                if(enchantmentLevel <= 0) {
                    //enchantmentIntegerMap.remove(i);

                } else {
                    newEnchantingMap.put(i,enchantmentLevel);
                }
                incrementCrystalLevel(state, world, pos);
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
        } else if(player.getStackInHand(hand).isOf(Items.AMETHYST_SHARD) && state.get(LEVEL) >= MAX_LEVEL) {
            world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1f, 2f);

            //Stats
            player.incrementStat(Stats.USED.getOrCreateStat(player.getStackInHand(hand).getItem()));
            if(!player.isCreative()) {
                player.getStackInHand(hand).decrement(1);
            }

            world.setBlockState(pos, state.with(LEVEL, 0), Block.NOTIFY_ALL);
            StatusEffectInstance statusEffectInstance = new StatusEffectInstance(Indexed.ENCHANTED_STATUS_EFFECT, 300*20);
            player.addStatusEffect(statusEffectInstance);

            if(world instanceof ServerWorld) {
                this.dropExperience((ServerWorld) world, pos, getCrystalPower(world, pos));
            }

            countAmethystClusters(pos, world, true);

            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }

    private int getCrystalPower(World world, BlockPos pos) {
        return 25 + (Math.min(countAmethystClusters(pos, world, false),8)*3);
    }

    private void incrementCrystalLevel(BlockState state, World world, BlockPos pos) {
        int i = state.get(LEVEL);

        if(i < MAX_LEVEL && (world.getRandom().nextInt(100)) <= getCrystalPower(world, pos)) {
            i++;
            world.setBlockState(pos, state.with(LEVEL, i), Block.NOTIFY_ALL);
        }
    }

    private boolean destroyAmethyst(BlockPos.Mutable checkedPos, BlockPos pos, World world) {
        int random = world.getRandom().nextInt(100);

        if(random <= 12) {
            if(!world.isClient()) {
                world.breakBlock(checkedPos, true, null, Block.NOTIFY_ALL);

                Vector3d positionOne = new Vector3d(pos.getX(), pos.getY(),pos.getZ());
                Vector3d positionTwo = new Vector3d(checkedPos.getX(), checkedPos.getY(), checkedPos.getZ());
                Vector3d positionLerp;

                for(double alpha = 0; alpha <= 1; alpha+=0.05) {
                    positionLerp = lerpVector(positionOne, positionTwo, alpha);
                    if(world instanceof ServerWorld) {
                        ((ServerWorld)world).spawnParticles(ParticleTypes.ENCHANTED_HIT, positionLerp.x+0.5, positionLerp.y+0.5, positionLerp.z+0.5, 1, 0, 0, 0, 0);
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private int countAmethystClusters(BlockPos pos, World world, boolean destroyAmethyst) {
        int scanSize = 3;
        int clusterCount = 0;
        int amethystBroken = 0;

        BlockPos.Mutable checkedPos = pos.mutableCopy();
        checkedPos = checkedPos.move(-scanSize,0,-scanSize);

        for(int i = -scanSize; i <= scanSize; i++) {
            for(int j = -scanSize; j <= scanSize; j++) {

                if(isAmethystCluster(world.getBlockState(checkedPos))) {
                    clusterCount++;

                    if(destroyAmethyst && amethystBroken < 3) {
                        if(destroyAmethyst(checkedPos, pos, world)) {
                            amethystBroken++;
                        }
                    }

                }
                checkedPos = checkedPos.move(1,0,0);
            }
            checkedPos = checkedPos.move(-scanSize*2 - 1,0,1);
        }

        return clusterCount;
    }

    private boolean isAmethystCluster(BlockState state) {


        if(state.getBlock() == Blocks.SMALL_AMETHYST_BUD ||
        state.getBlock() == Blocks.MEDIUM_AMETHYST_BUD ||
        state.getBlock() == Blocks.LARGE_AMETHYST_BUD ||
        state.getBlock() == Blocks.AMETHYST_CLUSTER) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(LEVEL);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

}
