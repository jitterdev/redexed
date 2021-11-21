package com.astrazoey.indexed.archive;

import net.minecraft.entity.DamageUtil;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*
@Mixin(DamageUtil.class)
public class DamageUtilMixin {

    @Inject(method = "getInflictedDamage", at = @At(value = "HEAD"), cancellable = true)
    private static void adjustProtectionAmount(float damageDealt, float protection, CallbackInfoReturnable<Float> cir) {
        protection = protection * 0.5f;
        float f = MathHelper.clamp(protection, 0.0F, 20.0F);
        cir.setReturnValue((Float) damageDealt * (1.0F - f / 25.0F));
    }

}
*/