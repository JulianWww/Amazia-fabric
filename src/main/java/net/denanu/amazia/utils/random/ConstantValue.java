package net.denanu.amazia.utils.random;

import net.denanu.amazia.mechanics.leveling.IAmaziaLevelProviderEntity;

public class ConstantValue<T extends Number> implements RandomnessFactory<T>, LevelBasedRandomnessFactory<T> {
	private final T value;
	public ConstantValue(final T val) {
		this.value = val;
	}
	@Override
	public T next() {
		return this.value;
	}
	@Override
	public T next(final IAmaziaLevelProviderEntity entity) {
		return this.next();
	}
}
