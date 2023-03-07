package net.denanu.amazia.particles;

import net.denanu.amazia.Amazia;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmaziaParticles {
	// PYTHON GENERATOR BEGIN
	// PYTHON GENERATOR END


	public static DefaultParticleType register(final DefaultParticleType type, final String name) {
		Registry.register(Registry.PARTICLE_TYPE, Identifier.of(Amazia.MOD_ID, name), type);
		return type;
	}

	public static void setup() {}
}
