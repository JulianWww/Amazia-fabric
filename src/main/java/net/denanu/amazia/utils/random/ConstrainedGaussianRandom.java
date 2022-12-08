package net.denanu.amazia.utils.random;

public class ConstrainedGaussianRandom extends GaussianRandom {
	private final int min, max;

	public ConstrainedGaussianRandom(final float mean, final float deriv, final int max, final int min) {
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
		} while (this.min > value || this.max < value);
		return value;
	}
}
