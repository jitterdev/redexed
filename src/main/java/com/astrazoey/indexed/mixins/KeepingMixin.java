package com.astrazoey.indexed.mixins;

import com.astrazoey.indexed.Indexed;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class KeepingMixin {
}

@Mixin(ServerPlayerEntity.class)
class ServerPlayerEntityMixin {

    //This is here to make the keeping enchantment keep when clicking "respawn"
    @Inject(method="copyFrom", at = @At(value="RETURN"))
    public void keepItemsAfterDeath(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if(!alive) {
            if(!oldPlayer.isSpectator() && !((ServerPlayerEntity) (Object) this).getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
                ((ServerPlayerEntity) (Object) this).getInventory().clone(oldPlayer.getInventory());
            }
        }
    }
}

@Mixin(value = PlayerInventory.class)
class PlayerInventoryMixin {

    @WrapOperation(
            method="dropAll",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z")
    )
    public boolean saveKeepingItems(ItemStack itemStack, Operation<Boolean> original) {
        if(!itemStack.isEmpty()) {
            int keepingLevel = EnchantmentHelper.getLevel(Indexed.KEEPING, itemStack);
            if(keepingLevel <= 0) {
                return original.call(itemStack);
            } else {
                int randomValue = (int) (Math.random() * 100);
                int keepingFactor = 50 + (keepingLevel * 10);
                return (keepingFactor >= randomValue);
            }
        } else {
            return original.call(itemStack);
        }
    }

}
