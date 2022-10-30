package net.denanu.amazia.utils.random;

import net.denanu.amazia.JJUtils;

public class GaussianRandom implements RandomnessFactory<Integer> {
	private final float mean, deriv;
	
	public GaussianRandom(float mean, float deriv) {
		this.mean = mean;
		this.deriv = deriv;
	}
	
	@Override
	public Integer next() {
		return (int)JJUtils.rand.nextGaussian(mean, deriv);
	}

}
