package net.denanu.amazia.mechanics.leveling;

public class AmaziaXpGainMap {
	static void gainXp(final IAmaziaLevelProviderEntity entity, final float xp) {
		entity.gainXp(xp);
	}

	/* static void gainXpWithProb(final IAmaziaLevelProviderEntity entity, final int prob, final float xp) {

	} */

	public static void gainCraftXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, 0.0001f);
	}

	public static void gainHealXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, 0.0001f);
	}

	public static void gainBlessXp(final IAmaziaLevelProviderEntity entity) {
		AmaziaXpGainMap.gainXp(entity, 0.0001f);
	}
}
