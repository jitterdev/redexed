package com.astrazoey.indexed.archive;

import com.astrazoey.indexed.Indexed;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
/*
@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {


    @Redirect(method="dropAll", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    public boolean saveKeepingItems(ItemStack itemStack) {
        if(!itemStack.isEmpty()) {
            int keepingLevel = EnchantmentHelper.getLevel(Indexed.KEEPING, itemStack);
            if(keepingLevel <= 0) {
                return false;
            } else {
                int randomValue = (int) (Math.random() * 100);
                int keepingFactor = 50 + (keepingLevel * 10);
                return (keepingFactor >= randomValue);
            }
        } else {
            return true;
        }
    }

}
*/