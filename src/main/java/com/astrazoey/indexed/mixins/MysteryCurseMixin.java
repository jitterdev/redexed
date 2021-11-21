package com.astrazoey.indexed.mixins;

import com.astrazoey.indexed.EnchantingType;
import com.astrazoey.indexed.Indexed;
import com.astrazoey.indexed.MaxEnchantingSlots;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;

@Mixin(ItemStack.class)
public class MysteryCurseMixin {

    @Shadow
    public static void appendEnchantments(List<Text> tooltip, NbtList enchantments) {}



    @Redirect(method="getTooltip", at = @At(value="INVOKE", target = "Lnet/minecraft/item/ItemStack;appendEnchantments(Ljava/util/List;Lnet/minecraft/nbt/NbtList;)V"))
    public void hideEnchantments(List<Text> list, NbtList enchantments) {

        boolean enchantmentsHidden = false;

        Map<Enchantment, Integer> enchantingMap = EnchantmentHelper.fromNbt(enchantments);
        for(int i = 0; i < enchantingMap.size(); i++) {
            Object enchantment = enchantingMap.keySet().toArray()[i];
            if(enchantment == Indexed.MYSTERY_CURSE) {
                enchantmentsHidden = true;
                break;
            }
        }

        if(enchantmentsHidden) {
            MutableText mutableText = (new TranslatableText("enchantment.indexed.mystery_tooltip").formatted(Formatting.OBFUSCATED, Formatting.RED));
            NbtList newEnchantments = new NbtList();
            list.add(mutableText);
            appendEnchantments(list, newEnchantments);

        } else {
            appendEnchantments(list, ((ItemStack) (Object) this).getEnchantments());
        }
    }
}