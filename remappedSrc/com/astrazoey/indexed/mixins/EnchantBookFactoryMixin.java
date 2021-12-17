package com.astrazoey.indexed.mixins;

import com.astrazoey.indexed.ConfigMain;
import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;



@Mixin(targets = {"net/minecraft/village/TradeOffers$EnchantBookFactory"})
public class EnchantBookFactoryMixin {
    @Redirect(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I"))
    public int updateMaxEnchantment(Enchantment enchantment) {
        if(enchantment.getMaxLevel() > 3 && ConfigMain.enableVillagerNerfs) {
            return 3;
        } else {
            return enchantment.getMaxLevel();
        }
    }
}

