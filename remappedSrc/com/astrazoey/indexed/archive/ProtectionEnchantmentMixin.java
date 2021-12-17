package com.astrazoey.indexed.archive;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*
@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin extends Enchantment {
    protected ProtectionEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes, ProtectionEnchantment.Type protectionType) {
        super(weight, type, slotTypes);
    }

    //Doubled the protection amounts here in order to nerf protection.
    //Nerf happens in the DamageUtilMixin
    @ModifyConstant(method = "getProtectionAmount", constant = @Constant(intValue = 2, ordinal = -1))
    public int doubleSpecializedProtections1(int protection) {
        return 4;
    }
    @ModifyConstant(method = "getProtectionAmount", constant = @Constant(intValue = 3, ordinal = -1))
    public int doubleSpecializedProtections2(int protection) {
        return 6;
    }


    /**
     * @author

    @Overwrite
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean canAccept(Enchantment other) {
        if(((Enchantment) (Object) this) == other) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }
}
*/