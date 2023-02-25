package net.denanu.amazia.mechanics.education;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.mechanics.intelligence.IAmaziaIntelligenceEntity;

public interface IAmaziaEducatedEntity {
	static float baseEducation(final IAmaziaIntelligenceEntity entity) {
		return (float) (10 + 2*Math.log(1+JJUtils.rand.nextFloat(100000)));
	}

	float getEducation();
	void learn(float baseAmount);

	default float maxEducation() {
		return IAmaziaIntelligenceEntity.maxIntelligence_s();
	}
}
