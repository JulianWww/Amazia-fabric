package net.denanu.amazia.mechanics.happyness;

public interface IAmaziaHappynessEntity {
	void gainHappyness(float amount);
	void looseHappyness(final float amount);

	float getHappyness();
	static float getDefaultHappyness() {
		return 100f;
	}
}
