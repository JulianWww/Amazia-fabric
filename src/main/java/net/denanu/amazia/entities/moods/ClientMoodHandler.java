package net.denanu.amazia.entities.moods;

import net.denanu.amazia.Amazia;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;

public class ClientMoodHandler {	
	public static void handleMood(LivingEntity entity, VillagerMoods mood) {
		switch(mood) {
		case HAPPY: handleHappy(entity); break;
		case ANGRY: handleAngry(entity); break;
		default:
			Amazia.LOGGER.info("Unkwon mood " + mood);;
		}
	}

	private static void handleHappy(LivingEntity entity) {
		spawnParticles(entity, ParticleTypes.HAPPY_VILLAGER, entity.getRandom().nextInt(25) + 5);
	}
	
	private static void handleAngry(LivingEntity entity) {
		spawnParticles(entity, ParticleTypes.ANGRY_VILLAGER, entity.getRandom().nextInt(25) + 5);
	}
	
	private static void spawnParticles(LivingEntity entity, ParticleEffect particles, int number) {
		for (int i = 0; i < number; ++i) {
			entity.world.addParticle(particles, 
					entity.getX() + entity.getRandom().nextGaussian() * (double)0.3f, 
					entity.getBoundingBox().maxY + 0.5 + entity.getRandom().nextGaussian() * (double)0.13f, 
					entity.getZ() + entity.getRandom().nextGaussian() * (double)0.3f, 
					0.0, 
					0.0, 
					0.0);
        }
	}
}
