package com.astrazoey.indexed;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.DamageParticle;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;

import static com.astrazoey.indexed.Indexed.CRYSTAL_GLOBE;

public class Client implements ClientModInitializer {

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {

        BlockRenderLayerMap.INSTANCE.putBlock(CRYSTAL_GLOBE, RenderLayer.getCutout());


        //Initialize Particle
//        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
//            registry.register(new Identifier("indexed", "particle/crystal_harvest"));
//        }));
//        Registry.register(Registries.PARTICLE_TYPE, new Identifier("indexed", "particle/crystal_harvest"), Indexed.CRYSTAL_HARVEST);
        ParticleFactoryRegistry.getInstance().register(Indexed.CRYSTAL_HARVEST, EndRodParticle.Factory::new);

//        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
//            registry.register(new Identifier("indexed", "particle/crystal_break"));
//        }));
//        Registry.register(Registries.PARTICLE_TYPE, new Identifier("indexed", "particle/crystal_break"), Indexed.CRYSTAL_BREAK);
        ParticleFactoryRegistry.getInstance().register(Indexed.CRYSTAL_BREAK, DamageParticle.Factory::new);

        //Load Indexed Config on Client
        Identifier identifier = new Identifier(Indexed.MOD_ID);
        ClientLifecycleEvents.CLIENT_STARTED.register(identifier, callbacks -> {
            System.out.println("INDEXED: Client started. Loading config.");
            Indexed.initializeConfig();
        });

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            NbtCompound nbtCompound = stack.getOrCreateNbt();
            if (nbtCompound.getBoolean("Unbreakable") && MaxEnchantingSlots.getCurrent(stack) > MaxEnchantingSlots.getEnchantType(stack).getMaxEnchantingSlots()) {
                stack.addHideFlag(ItemStack.TooltipSection.UNBREAKABLE);
                lines.add(Text.translatable("item.unbreakable").formatted(Formatting.RED).formatted(Formatting.STRIKETHROUGH));
            } else if (nbtCompound.getBoolean("Unbreakable") && nbtCompound.contains("HideFlags", NbtElement.NUMBER_TYPE) && MaxEnchantingSlots.getCurrent(stack) <= MaxEnchantingSlots.getEnchantType(stack).getMaxEnchantingSlots() && lines.contains(Text.translatable("item.unbreakable").formatted(Formatting.RED).formatted(Formatting.STRIKETHROUGH))) {
                int flags = nbtCompound.getInt("HideFlags");
                nbtCompound.putInt("HideFlags", flags & ~ItemStack.TooltipSection.UNBREAKABLE.getFlag());
                lines.remove(Text.translatable("item.unbreakable").formatted(Formatting.RED).formatted(Formatting.STRIKETHROUGH));
            }
        });
    }
}
