package net.denanu.amazia.mechanics.leveling;

public class AmaziaXpGainMap {
	private final static float DEFAULT_XP = 0.0001f;

	static void gainXp(final IAmaziaLevelProviderEntity entity, final float xp) {
		entity.gainXp(xp);
	}

	/*
	 * static void gainXpWithProb(final IAmaziaLevelProviderEntity entity, final int
	 * prob, final float xp) {
	 *
	 * }
	 */

	public static void gainCraftXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, AmaziaXpGainMap.DEFAULT_XP);
	}

	public static void gainHealXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, AmaziaXpGainMap.DEFAULT_XP);
	}

	public static void gainBlessXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, AmaziaXpGainMap.DEFAULT_XP);
	}

	public static void gainMineRegrowXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, AmaziaXpGainMap.DEFAULT_XP);
	}

	public static void gainCropGrowXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, AmaziaXpGainMap.DEFAULT_XP);
	}

	public static void gainEnchantXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, AmaziaXpGainMap.DEFAULT_XP);
	}

	public static void gainPlantCropXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, AmaziaXpGainMap.DEFAULT_XP);
	}

	public static void gainHarvestCropXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, AmaziaXpGainMap.DEFAULT_XP);
	}

	public static void gainTillFarmLandXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, AmaziaXpGainMap.DEFAULT_XP);
	}

	public static void gainAttackXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, AmaziaXpGainMap.DEFAULT_XP);
	}

	public static void gainPlantSaplingXP(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, AmaziaXpGainMap.DEFAULT_XP);
	}

	public static void gainChopTreeXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, AmaziaXpGainMap.DEFAULT_XP);
	}
}
