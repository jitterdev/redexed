package com.astrazoey.indexed.archive;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import org.spongepowered.asm.mixin.Mixin;
/*
@Mixin(ToolItem.class)
public class ToolItemMixin extends Item {

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
*/