package net.denanu.amazia.utils.random;

import net.denanu.amazia.mechanics.leveling.IAmaziaLevelProviderEntity;

public class LevelBasedLinearRange implements LevelBasedRandomnessFactory<Float> {
	final float zeroVal;
	final float slope;

	public LevelBasedLinearRange(final float zeroVal, final float endVal) {
		this(zeroVal, endVal, LevelBasedRandomnessFactory.MAX_VILLAGER_LEVEL);
	}

	public LevelBasedLinearRange(final float zeroVal, final float endVal, final float distance) {
		this.zeroVal = zeroVal;
		this.slope = (endVal - zeroVal)/distance;
	}

	@Override
	public Float next(final IAmaziaLevelProviderEntity entity) {
		return this.zeroVal + this.slope * entity.getLevel();
	}
}
