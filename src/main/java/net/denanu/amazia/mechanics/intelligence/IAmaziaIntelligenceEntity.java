package net.denanu.amazia.mechanics.intelligence;

import net.denanu.amazia.utils.random.ConstrainedGaussianFloatRandom;
import net.denanu.amazia.utils.random.GaussianFloatRandom;
import net.denanu.amazia.utils.random.RandomnessFactory;

public interface IAmaziaIntelligenceEntity {
	RandomnessFactory<Float> INTELLIGENCE_FACTORY = new ConstrainedGaussianFloatRandom(60, 10, 100, 50);
	RandomnessFactory<Float> CHILD_INTELLIGENCE_FACTORY = new GaussianFloatRandom(0, 5);

	static float getInitalIntelligence() {
		return IAmaziaIntelligenceEntity.INTELLIGENCE_FACTORY.next();
	}

	default float getChildIntelligence() {
		final float value = this.getIntelligence() + IAmaziaIntelligenceEntity.CHILD_INTELLIGENCE_FACTORY.next();
		if (value > 100) {
			return 100;
		}
		if (value < 10) {
			return 10;
		}

		return value;
	}

	float getIntelligence();

	boolean modifyIntelligence(float amount);

	default float maxIntelligence() {
		return IAmaziaIntelligenceEntity.maxIntelligence_s();
	}

	static float maxIntelligence_s() {
		return 100;
	}
}
