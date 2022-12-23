package net.denanu.amazia.mechanics.happyness;

import net.denanu.amazia.utils.random.ConstrainedGaussianFloatRandom;

public class HappynessMap {
	private static final ConstrainedGaussianFloatRandom ADULT_DIE_FACTORY = new ConstrainedGaussianFloatRandom(32.5f, 5f, 45, 20);
	private static final ConstrainedGaussianFloatRandom CHILD_DIE_FACTORY = new ConstrainedGaussianFloatRandom(65f,   6f, 45, 20);

	// UNIVERSAL HAPPYNESS BOONS

	public static void gainRadBookHappyness(final IAmaziaHappynessEntity entity) {
		entity.gainHappyness(2);
	}

	public static void getHealedGainHappyness(final IAmaziaHappynessEntity entity) {
		entity.gainHappyness(10);
	}

	public static void gainBlessingHappyness(final IAmaziaHappynessEntity entity) {
		entity.gainHappyness(1);
	}

	// UNIVERSAL MISERY

	public static void looseTakeDamageHappyness(final IAmaziaHappynessEntity entity) {
		entity.looseHappyness(8);
	}

	public static void loseHappynessSeeVillagerDie(final IAmaziaHappynessEntity entity, final boolean isBaby) {
		if (isBaby) {
			entity.looseHappyness(HappynessMap.CHILD_DIE_FACTORY.next());
		}
		else {
			entity.looseHappyness(HappynessMap.ADULT_DIE_FACTORY.next());
		}
	}
}
