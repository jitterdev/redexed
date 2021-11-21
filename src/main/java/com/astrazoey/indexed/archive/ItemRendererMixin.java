package com.astrazoey.indexed.archive;

import com.astrazoey.indexed.Indexed;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    public ThreadLocal<ModelTransformation.Mode> renderMode = new ThreadLocal<ModelTransformation.Mode>();


    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "HEAD"))
    public void captureRenderMode(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        this.renderMode.set(renderMode);
    }

    @Redirect(method= "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    public boolean checkForHiddenCurse(ItemStack itemStack) {
        if(itemStack.isEmpty()) {
            return true;
        } else if((EnchantmentHelper.getLevel(Indexed.HIDDEN_CURSE, itemStack) > 0 && (renderMode.get() != ModelTransformation.Mode.GROUND) && (renderMode.get() != ModelTransformation.Mode.GUI) && (renderMode.get() != ModelTransformation.Mode.FIXED))) {
            return true;
        } else {
            return false;
        }
    }

}
*/