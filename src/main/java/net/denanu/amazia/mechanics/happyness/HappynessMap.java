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

	public static void looseHappynessFromFleeing(final IAmaziaHappynessEntity entity) {
		entity.looseHappyness(2);
	}

	public static void looseDropOfItemsHappyness(final IAmaziaHappynessEntity entity) {
		entity.looseHappyness(0.5f);
	}

	public static void loosePickUpItemsHappyness(final IAmaziaHappynessEntity entity) {
		entity.looseHappyness(0.5f);
	}

	// MINER

	public static void gainMinerFindOreHappyness(final IAmaziaHappynessEntity entity) {
		entity.gainHappyness(3);
	}

	public static void looseMineBlockHappyness(final IAmaziaHappynessEntity entity) {
		entity.looseHappyness(0.3f);
	}

	// CLERIC

	public static void gainHealOtherVillagerHappyness(final IAmaziaHappynessEntity entity) {
		entity.gainHappyness(2);
	}

	// LUMBERJACK

	public static void looseChopTreeHappyness(final IAmaziaHappynessEntity entity) {
		entity.looseHappyness(2);
	}

	// CRAFTERS

	public static void looseCraftHappyness(final IAmaziaHappynessEntity entity) {
		entity.looseHappyness(2);
	}

	// RANCHER

	public static void looseFeedAnimalHappyness(final IAmaziaHappynessEntity entity) {
		entity.looseHappyness(3);
	}

	// FARMER

	public static void looseHarvestCropHappyness(final IAmaziaHappynessEntity entity) {
		entity.looseHappyness(2);
	}

	public static void looseTillDirtHappyness(final IAmaziaHappynessEntity entity) {
		entity.looseHappyness(3);
	}
}
