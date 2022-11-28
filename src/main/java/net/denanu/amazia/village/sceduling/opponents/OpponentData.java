package net.denanu.amazia.village.sceduling.opponents;

import net.minecraft.entity.LivingEntity;

public class OpponentData implements Comparable<OpponentData> {
	private final LivingEntity target;
	private int priority;

	public OpponentData(final LivingEntity target, final int priority) {
		this.target = target;
		this.priority = priority;
	}

	public int getPriority() {
		return this.priority;
	}

	public boolean equals(final LivingEntity mob) {
		return this.target.getId() == mob.getId();
	}

	public LivingEntity getTarget() {
		return this.target;
	}

	@Override
	public int compareTo(final OpponentData o) {
		return Integer.compare(this.priority, o.priority);
	}

	public void decrementPriority() {
		this.priority--;
	}
}
