package com.zeml.rotp_zwa.init;

import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, RotpWhiteAlbumAddon.MOD_ID);

    public static final RegistryObject<BasicParticleType> ICE = PARTICLES.register("ice_shard", () -> new BasicParticleType(false));


}
