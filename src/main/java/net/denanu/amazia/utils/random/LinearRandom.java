package net.denanu.amazia.utils.random;

import net.denanu.amazia.JJUtils;

public class LinearRandom implements RandomnessFactory<Integer> {
	private final int min, delta;
	
	public LinearRandom(final int min, final int max) {
		this.min = min;
		this.delta = max - min;
	}

	@Override
	public Integer next() {
		return JJUtils.rand.nextInt(this.delta) + this.min;
	}

}
