package com.astrazoey.indexed.mixins;


import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;


public class RepairItemMixin {
}

@Mixin(BowItem.class)
class BowItemMixin extends Item  {

    public BowItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.STRING);
    }

}

@Mixin(FishingRodItem.class)
class FishingRodItemMixin extends Item {
    public FishingRodItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.STRING);
    }
}

@Mixin(FlintAndSteelItem.class)
class FlintAndSteelItemMixin extends Item {

    public FlintAndSteelItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.FLINT);
    }
}


@Mixin(ShearsItem.class)
class ShearsItemMixin extends Item {

    public ShearsItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.IRON_INGOT);
    }

}

@Mixin(ToolItem.class)
class ToolItemMixin extends Item {

    public ToolItemMixin(Settings settings) {
        super(settings);
    }

    //diamonds can now repair netherite
    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {

        if(((ToolItem) (Object) this).asItem() == Items.NETHERITE_SWORD ||
                ((ToolItem) (Object) this).asItem() == Items.NETHERITE_PICKAXE ||
                ((ToolItem) (Object) this).asItem() == Items.NETHERITE_AXE ||
                ((ToolItem) (Object) this).asItem() == Items.NETHERITE_HOE ||
                ((ToolItem) (Object) this).asItem() == Items.NETHERITE_SHOVEL
        ) {
            return ingredient.isOf(Items.DIAMOND);
        }


        return ((ToolItem) (Object) this).getMaterial().getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
    }
}

@Mixin(ArmorItem.class)
class ArmorItemMixin extends Item {
    public ArmorItemMixin(Settings settings) {
        super(settings);
    }

    //diamonds can now repair netherite
    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {

        if(((ArmorItem) (Object) this).asItem() == Items.NETHERITE_HELMET ||
                ((ArmorItem) (Object) this).asItem() == Items.NETHERITE_CHESTPLATE ||
                ((ArmorItem) (Object) this).asItem() == Items.NETHERITE_LEGGINGS ||
                ((ArmorItem) (Object) this).asItem() == Items.NETHERITE_BOOTS
        ) {
            return ingredient.isOf(Items.DIAMOND);
        }


        return ((ArmorItem) (Object) this).getMaterial().getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
    }
}

@Mixin(TridentItem.class)
class TridentItemMixin extends Item {

    public ThreadLocal<ItemStack> tridentItem = new ThreadLocal<ItemStack>();
    public ThreadLocal<LivingEntity> tridentOwner = new ThreadLocal<LivingEntity>();

    public TridentItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.NAUTILUS_SHELL);
    }

}




