package com.astrazoey.indexed;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.DamageParticle;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.util.Identifier;

public class Client implements ClientModInitializer {

    @Override
    @Environment(EnvType.CLIENT)
    public void onInitializeClient() {

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
    }
}
