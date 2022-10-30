package net.denanu.amazia.utils.random;

public class ConstantValue<T extends Number> implements RandomnessFactory<T> {
	private final T value;
	public ConstantValue(T val) {
		this.value = val;
	}
	@Override
	public T next() {
		return this.value;
	}
}
