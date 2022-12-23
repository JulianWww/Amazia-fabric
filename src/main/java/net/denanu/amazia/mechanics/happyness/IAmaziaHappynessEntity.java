package net.denanu.amazia.mechanics.happyness;

public interface IAmaziaHappynessEntity {
	void gainHappyness(float amount);
	void looseHappyness(final float amount);

	default void applyHappiness(final float ammount) {
		if (ammount > 0) {
			this.gainHappyness(ammount);
		}
		else {
			this.looseHappyness(-ammount);
		}
	}

	float getHappyness();
	static float getDefaultHappyness() {
		return 100f;
	}

	default boolean isDepressed() {
		return this.getHappyness() < 30f;
	}
}
