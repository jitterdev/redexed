package com.astrazoey.indexed.mixins;

import com.astrazoey.indexed.ConfigMain;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;


@Mixin(PersistentProjectileEntity.class)
public class PunchMixin  {

    @ModifyConstant(method="onEntityHit", constant = @Constant(doubleValue = 0.6d, ordinal = 0))
    public double adjustPunch(double punchModifier) {
        if(ConfigMain.enableEnchantmentNerfs) {
            return 0.45d;
        } else {
            return 0.6d;
        }

    }


}

