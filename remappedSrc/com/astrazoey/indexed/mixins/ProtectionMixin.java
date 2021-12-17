package com.astrazoey.indexed.mixins;

import com.astrazoey.indexed.ConfigMain;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ProtectionMixin {
}

//Halves the amount of protection you get from enchantments. But all enchantments (except Protection) are adjusted for this.
@Mixin(DamageUtil.class)
class DamageUtilMixin {

    @Inject(method = "getInflictedDamage", at = @At(value = "HEAD"), cancellable = true)
    private static void adjustProtectionAmount(float damageDealt, float protection, CallbackInfoReturnable<Float> cir) {
        if(ConfigMain.enableEnchantmentNerfs) {
            protection = protection * 0.5f;
            float f = MathHelper.clamp(protection, 0.0F, 20.0F);
            cir.setReturnValue((Float) damageDealt * (1.0F - f / 25.0F));
        }
    }

}

@Mixin(ProtectionEnchantment.class)
class ProtectionAdjustmentMixin extends Enchantment {
    protected ProtectionAdjustmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes, ProtectionEnchantment.Type protectionType) {
        super(weight, type, slotTypes);
    }

    //Doubled the protection amounts here in order to nerf protection.
    //Nerf happens in the DamageUtilMixin above
    @ModifyConstant(method = "getProtectionAmount", constant = @Constant(intValue = 2, ordinal = -1))
    public int doubleSpecializedProtections1(int protection) {
        if(ConfigMain.enableEnchantmentNerfs) {
            return 4;
        } else {
            return 2;
        }
    }
    @ModifyConstant(method = "getProtectionAmount", constant = @Constant(intValue = 3, ordinal = -1))
    public int doubleSpecializedProtections2(int protection) {
        if(ConfigMain.enableEnchantmentNerfs) {
            return 6;
        } else {
            return 3;
        }
    }
}