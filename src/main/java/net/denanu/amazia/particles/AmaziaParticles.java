package net.denanu.amazia.particles;

import net.denanu.amazia.Amazia;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmaziaParticles {
	// PYTHON GENERATOR BEGIN
	public static final DefaultParticleType AXE = AmaziaParticles.register(FabricParticleTypes.simple(), "axe");
	public static final DefaultParticleType BEETROOT = AmaziaParticles.register(FabricParticleTypes.simple(), "beetroot");
	public static final DefaultParticleType BEETROOT_SEEDS = AmaziaParticles.register(FabricParticleTypes.simple(), "beetroot_seeds");
	public static final DefaultParticleType CARROT = AmaziaParticles.register(FabricParticleTypes.simple(), "carrot");
	public static final DefaultParticleType COAL = AmaziaParticles.register(FabricParticleTypes.simple(), "coal");
	public static final DefaultParticleType HOE = AmaziaParticles.register(FabricParticleTypes.simple(), "hoe");
	public static final DefaultParticleType LAPIS = AmaziaParticles.register(FabricParticleTypes.simple(), "lapis");
	public static final DefaultParticleType PICK_AXE = AmaziaParticles.register(FabricParticleTypes.simple(), "pick_axe");
	public static final DefaultParticleType POTATO = AmaziaParticles.register(FabricParticleTypes.simple(), "potato");
	public static final DefaultParticleType SAPLING = AmaziaParticles.register(FabricParticleTypes.simple(), "sapling");
	public static final DefaultParticleType SWORD = AmaziaParticles.register(FabricParticleTypes.simple(), "sword");
	public static final DefaultParticleType TORCH = AmaziaParticles.register(FabricParticleTypes.simple(), "torch");
	public static final DefaultParticleType WHEAT = AmaziaParticles.register(FabricParticleTypes.simple(), "wheat");
	public static final DefaultParticleType WHEAT_SEEDS = AmaziaParticles.register(FabricParticleTypes.simple(), "wheat_seeds");
	// PYTHON GENERATOR END


	public static DefaultParticleType register(final DefaultParticleType type, final String name) {
		Registry.register(Registry.PARTICLE_TYPE, Identifier.of(Amazia.MOD_ID, name), type);
		return type;
	}

	public static void setup() {}
}
