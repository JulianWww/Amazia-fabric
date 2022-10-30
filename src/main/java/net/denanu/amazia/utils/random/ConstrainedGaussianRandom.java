package net.denanu.amazia.utils.random;

public class ConstrainedGaussianRandom extends GaussianRandom {
	private final int min, max;

	public ConstrainedGaussianRandom(float mean, float deriv, int max, int min) {
		super(mean, deriv);
		this.max = max;
		this.min = min;
		assert max > min;
	}
	
	@Override
	public Integer next() {
		int value;
		do {
			value = super.next();
		} while (min > value || max < value);
		return value;
	}

}
