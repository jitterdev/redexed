package com.astrazoey.indexed;

import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public interface SetOreExperience {

    static void set(Block oreBlock, UniformIntProvider intProvider) {
        if(oreBlock instanceof OreBlock) {
            ((SetOreExperience) (OreBlock) oreBlock).setExperience(intProvider);
        }

    }

    void setExperience(UniformIntProvider intProvider);

}
