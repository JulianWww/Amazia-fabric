package net.denanu.amazia.mechanics.intelligence;

import net.denanu.amazia.utils.random.ConstrainedGaussianFloatRandom;
import net.denanu.amazia.utils.random.RandomnessFactory;

public interface IAmaziaIntelligenceEntity {
	static RandomnessFactory<Float> INTELLIGENCE_FACTORY = new ConstrainedGaussianFloatRandom(60, 10, 100, 50);

	public static float getInitalIntelligence() {
		return IAmaziaIntelligenceEntity.INTELLIGENCE_FACTORY.next();
	}

	public float getIntelligence();
}
