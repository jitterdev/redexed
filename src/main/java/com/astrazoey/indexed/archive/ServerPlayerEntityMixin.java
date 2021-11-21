package com.astrazoey.indexed.archive;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    //This is here to make the keeping enchantment keep when clicking "respawn"
    @Inject(method="copyFrom", at = @At(value="RETURN"))
    public void keepItemsAfterDeath(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if(!alive) {
            if(!oldPlayer.isSpectator() && !((ServerPlayerEntity) (Object) this).world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
                ((ServerPlayerEntity) (Object) this).getInventory().clone(oldPlayer.getInventory());
            }
        }
    }
}
*/