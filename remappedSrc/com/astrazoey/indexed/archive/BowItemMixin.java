package com.astrazoey.indexed.archive;

import com.astrazoey.indexed.SetFlameEnchantmentLevel;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*
@Mixin(BowItem.class)
public class BowItemMixin extends Item implements SetFlameEnchantmentLevel {

    public int flameEnchantmentLevel = 1;

    public BowItemMixin(Settings settings) {
        super(settings);
    }


    @Inject(method="onStoppedUsing", at = @At(value ="HEAD"))
    public void getFlameEnchantmentLevel(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        flameEnchantmentLevel = EnchantmentHelper.getLevel(Enchantments.FLAME, stack);
    }


    @Redirect(method="onStoppedUsing", at = @At(value = "INVOKE", target ="Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    public boolean setFlameEnchantment(World world, Entity entity) {

        SetFlameEnchantmentLevel.set((PersistentProjectileEntity) entity, flameEnchantmentLevel);
        world.spawnEntity(entity);
        return true;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.STRING);
    }

    @Override
    public void setFlameEnchantmentLevel(int flameLevel) {

    }
}
*/