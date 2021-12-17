package com.astrazoey.indexed.mixins;

import com.astrazoey.indexed.SetOreExperience;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OreBlock.class)
public class OreBlockMixin implements SetOreExperience {
    @Shadow
    @Final
    @Mutable
    private UniformIntProvider experienceDropped;

    @Override
    public void setExperience(UniformIntProvider intProvider) {
        this.experienceDropped = intProvider;
    }
}
