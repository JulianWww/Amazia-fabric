package net.denanu.amazia.mechanics.leveling;

import net.minecraft.util.Identifier;

public interface IAmaziaLevelProviderEntity {
	public float getLevel();
	public float getLevel(Identifier profession);
	public void gainXp(float xpVal);
	public void gainXp(Identifier profession, float xpVal);

	public float getLevelBoost();
	public void setLevelBoost(final float levelBoost);
}
