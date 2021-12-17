package com.astrazoey.indexed.archive;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    //Allow channeling and riptide compat
    @Inject(method="tickRiptide", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;attackLivingEntity(Lnet/minecraft/entity/LivingEntity;)V"))
    public void applyLightningEffectToRiptide(Box a, Box b, CallbackInfo ci) {

        Entity entity = (Entity) (Object) this;

        if(EnchantmentHelper.getEquipmentLevel(Enchantments.CHANNELING, (LivingEntity) (Object) this) > 0) {
            if (((LivingEntity) (Object) this).world.isSkyVisible(((LivingEntity) (Object) this).getBlockPos())) {
                LightningEntity lightningEntity = (LightningEntity) EntityType.LIGHTNING_BOLT.create(((LivingEntity) (Object) this).world);
                assert lightningEntity != null;
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(((LivingEntity) (Object) this).getBlockPos()));
                lightningEntity.setChanneler(entity instanceof ServerPlayerEntity ? (ServerPlayerEntity)entity : null);
                ((LivingEntity) (Object) this).world.spawnEntity(lightningEntity);
                SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
                ((LivingEntity) (Object) this).playSound(soundEvent, 5.0F, 1.0F);
            }

        }
    }

}
*/