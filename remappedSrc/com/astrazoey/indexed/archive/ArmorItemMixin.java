package com.astrazoey.indexed.archive;

import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
/*
@Mixin(ArmorItem.class)
public class ArmorItemMixin extends Item {
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
*/