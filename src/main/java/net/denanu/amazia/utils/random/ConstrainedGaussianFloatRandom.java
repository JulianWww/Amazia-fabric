package net.denanu.amazia.utils.random;

public class ConstrainedGaussianFloatRandom extends GaussianFloatRandom {
	private final int min, max;

	public ConstrainedGaussianFloatRandom(final float mean, final float deriv, final int max, final int min) {
		super(mean, deriv);
		this.max = max;
		this.min = min;
		assert max > min;
	}

	@Override
	public Float next() {
		float value;
		do {
			value = super.next();
		} while (this.min > value || this.max < value);
		return value;
	}
}
