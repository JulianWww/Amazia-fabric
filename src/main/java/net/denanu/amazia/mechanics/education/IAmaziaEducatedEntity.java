package net.denanu.amazia.mechanics.education;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.mechanics.intelligence.IAmaziaIntelligenceEntity;

public interface IAmaziaEducatedEntity {
	public static float baseEducation(final IAmaziaIntelligenceEntity entity) {
		return (float) (10 + 2*Math.log(1+JJUtils.rand.nextFloat(100000)));
	}

	public float getEducation();
	public void learn(float baseAmount);
}
