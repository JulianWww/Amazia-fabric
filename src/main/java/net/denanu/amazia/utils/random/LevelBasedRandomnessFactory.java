package net.denanu.amazia.utils.random;

import net.denanu.amazia.mechanics.leveling.IAmaziaLevelProviderEntity;

public interface LevelBasedRandomnessFactory<T> {
	public static int MAX_VILLAGER_LEVEL = 100;
	public T next(final IAmaziaLevelProviderEntity entity);
}
