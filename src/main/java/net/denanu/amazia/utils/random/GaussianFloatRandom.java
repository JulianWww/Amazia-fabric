package net.denanu.amazia.utils.random;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.mechanics.leveling.IAmaziaLevelProviderEntity;

public class GaussianFloatRandom implements RandomnessFactory<Float>, LevelBasedRandomnessFactory<Float> {
	private final float mean, deriv;

	public GaussianFloatRandom(final float mean, final float deriv) {
		this.mean = mean;
		this.deriv = deriv;
	}

	@Override
	public Float next() {
		return (float) JJUtils.rand.nextGaussian(this.mean, this.deriv);
	}

	@Override
	public Float next(final IAmaziaLevelProviderEntity entity) {
		return this.next();
	}
}
