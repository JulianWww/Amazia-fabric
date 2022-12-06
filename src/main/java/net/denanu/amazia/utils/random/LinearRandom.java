package net.denanu.amazia.utils.random;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.mechanics.leveling.IAmaziaLevelProviderEntity;

public class LinearRandom implements RandomnessFactory<Integer>, LevelBasedRandomnessFactory<Integer> {
	private final int min, delta;

	public LinearRandom(final int min, final int max) {
		this.min = min;
		this.delta = max - min;
	}

	@Override
	public Integer next() {
		return JJUtils.rand.nextInt(this.delta) + this.min;
	}

	@Override
	public Integer next(final IAmaziaLevelProviderEntity entity) {
		return this.next();
	}
}
