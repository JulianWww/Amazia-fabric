package net.denanu.amazia.entities.village.server.goal.druid.regeneration;

public enum OreRarity {
	VERY_COMMON(100f),
	COMMON(30f),
	UNCOMMON(10f),
	RARE(1f);

	private float value;

	public float getValue() {
		return this.value;
	}

	private OreRarity(final float val) {
		this.value = val;
	}
}
