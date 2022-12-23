package net.denanu.amazia.utils.random;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.mechanics.leveling.IAmaziaLevelProviderEntity;

public class GaussianIntRandom implements RandomnessFactory<Integer>, LevelBasedRandomnessFactory<Integer> {
	private final float mean, deriv;

	public GaussianIntRandom(final float mean, final float deriv) {
		this.mean = mean;
		this.deriv = deriv;
	}

	@Override
	public Integer next() {
		return (int)JJUtils.rand.nextGaussian(this.mean, this.deriv);
	}

	@Override
	public Integer next(final IAmaziaLevelProviderEntity entity) {
		return this.next();
	}
}
