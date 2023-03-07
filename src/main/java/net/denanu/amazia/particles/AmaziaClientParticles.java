package net.denanu.amazia.particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.SuspendParticle;

public class AmaziaClientParticles {
	public static void setup() {
		// PYTHON GENERATOR BEGIN
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.AXE, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.BEETROOT, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.BEETROOT_SEEDS, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.CARROT, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.COAL, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.HOE, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.LAPIS, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.PICK_AXE, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.POTATO, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.SAPLING, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.STICK, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.SWORD, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.TORCH, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.WHEAT, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.WHEAT_SEEDS, SuspendParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(AmaziaParticles.SHEARS, SuspendParticle.Factory::new);
		// PYTHON GENERATOR END
	}
}
