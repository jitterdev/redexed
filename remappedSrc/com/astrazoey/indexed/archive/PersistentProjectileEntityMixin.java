package com.astrazoey.indexed.archive;

import com.astrazoey.indexed.SetFlameEnchantmentLevel;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*
@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileEntityMixin implements SetFlameEnchantmentLevel {

    public int flameEnchantmentLevel = 1;



    @Inject(method="applyEnchantmentEffects", at = @At(value ="HEAD"))
    public void getFlameEnchantmentLevel(LivingEntity entity, float damageModifier, CallbackInfo ci) {
        flameEnchantmentLevel = EnchantmentHelper.getEquipmentLevel(Enchantments.FLAME, entity);
    }



    @Redirect(method="onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(I)V"))
    public void adjustFlame(Entity entity, int seconds) {
        int newAmount = 5+((flameEnchantmentLevel-1)*3);
        entity.setOnFireFor(newAmount);
    }

    @ModifyConstant(method="onEntityHit", constant = @Constant(doubleValue = 0.6d, ordinal = 0))
    public double adjustPunch(double punchModifier) {
        return 0.45d;
    }

    @Override
    public void setFlameEnchantmentLevel(int flameLevel) {
        flameEnchantmentLevel = flameLevel;
        System.out.println("set flame level to " + flameLevel);
    }
}
*/