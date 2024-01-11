package com.astrazoey.indexed.blocks;

import com.astrazoey.indexed.Indexed;
import net.minecraft.block.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.joml.Vector3d;

import java.util.Map;
import java.util.function.ToIntFunction;


public class CrystalGlobeBlock extends Block {

    public static final int MAX_LEVEL = 8;
    public static final IntProperty LEVEL = Properties.LEVEL_8;
    public static final ToIntFunction<BlockState> STATE_TO_LUMINANCE;

    public CrystalGlobeBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)this.stateManager.getDefaultState().with(LEVEL, 0));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {

        VoxelShape base = VoxelShapes.cuboid(0.125f, 0f, 0.125f, 0.875f, 0.125f, 0.875f);
        VoxelShape stand = VoxelShapes.cuboid(0.375f, 0.125f, 0.375f, 0.625f, 0.25f, 0.625);
        VoxelShape head = VoxelShapes.cuboid(0.1875f, 0.25f, 0.1875f, 0.8125f, 0.875f, 0.8125f);

        return VoxelShapes.union(head, VoxelShapes.union(base, stand));
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

            int totalEnchants = 0;

            for(Enchantment i : enchantmentIntegerMap.keySet()) {
                int enchantmentLevel = enchantmentIntegerMap.get(i);

                if(i.isCursed()) {
                    newEnchantingMap.put(i,enchantmentLevel);
                } else {
                    enchantmentLevel--;
                    incrementCrystalLevel(state, world, pos);
                    totalEnchants++;
                }

                if(enchantmentLevel > 0) {
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
                    totalEnchants++;
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

            if(totalEnchants > 0) {
                world.playSound(null, pos, Indexed.CRYSTAL_USE_SOUND_EVENT, SoundCategory.BLOCKS, 0.2f, 0.8f + world.getRandom().nextFloat() / 2.5f);

                if(world instanceof ServerWorld) {
                    ((ServerWorld) world).spawnParticles(Indexed.CRYSTAL_BREAK, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 20, 0.25, 0.25, 0.25, 0);
                }

                return ActionResult.SUCCESS;
            } else {
                return ActionResult.FAIL;
            }
        } else if(player.getMainHandStack().isEmpty() && state.get(LEVEL) >= MAX_LEVEL) {
            world.playSound(null, pos, Indexed.CRYSTAL_HARVEST_SOUND_EVENT, SoundCategory.BLOCKS, 1f, 0.9f + world.getRandom().nextFloat()/ 5.0f);

            world.setBlockState(pos, state.with(LEVEL, 0), Block.NOTIFY_ALL);
            StatusEffectInstance statusEffectInstance = new StatusEffectInstance(Indexed.ENCHANTED_STATUS_EFFECT, 300*20);
            player.addStatusEffect(statusEffectInstance);

            if(world instanceof ServerWorld) {
                this.dropExperience((ServerWorld) world, pos, getCrystalPower(world, pos));
            }

            int finalClusterCount = countAmethystClusters(pos, world, true);

            if(player instanceof ServerPlayerEntity) {
                Indexed.USE_CRYSTAL_GLOBE.trigger((ServerPlayerEntity) player);
                if(finalClusterCount >= MAX_LEVEL) {
                    Indexed.FILL_CRYSTAL_GLOBE.trigger((ServerPlayerEntity) player);
                }

            }


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

        if(i < MAX_LEVEL && (world.getRandom().nextInt(100)) <= getCrystalPower(world, pos) && !world.isClient()) {
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

                for(double alpha = 0; alpha <= 1; alpha+=0.025) {
                    positionLerp = lerpVector(positionOne, positionTwo, alpha);
                    if(world instanceof ServerWorld) {
                        ((ServerWorld)world).spawnParticles(Indexed.CRYSTAL_BREAK, positionLerp.x+0.5, positionLerp.y+0.5, positionLerp.z+0.5, 1, 0, 0, 0, 0);
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
        return state.getBlock() == Blocks.SMALL_AMETHYST_BUD ||
                state.getBlock() == Blocks.MEDIUM_AMETHYST_BUD ||
                state.getBlock() == Blocks.LARGE_AMETHYST_BUD ||
                state.getBlock() == Blocks.AMETHYST_CLUSTER;
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(3) == 0 && state.get(LEVEL) >= MAX_LEVEL) {
            world.addParticle(Indexed.CRYSTAL_HARVEST, pos.getX()+(random.nextDouble()), pos.getY()+(random.nextDouble()), pos.getZ()+(random.nextDouble()), random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D, random.nextGaussian() * 0.005D);
        }

        if (random.nextInt(24) == 0 && state.get(LEVEL) >= MAX_LEVEL) {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), Indexed.CRYSTAL_AMBIENT_SOUND_EVENT, SoundCategory.BLOCKS, 2f, 0.8f + world.getRandom().nextFloat() / 2.5f, true);
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

    static {
        STATE_TO_LUMINANCE = (state) -> {
            return 2 * (Integer)state.get(LEVEL);
        };
    }

}
