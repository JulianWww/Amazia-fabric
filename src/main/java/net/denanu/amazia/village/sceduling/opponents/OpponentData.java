package net.denanu.amazia.village.sceduling.opponents;

import net.minecraft.entity.mob.MobEntity;

public class OpponentData implements Comparable<OpponentData> {
	private final MobEntity target;
	private final int priority;

	public OpponentData(final MobEntity target, final int priority) {
		this.target = target;
		this.priority = priority;
	}

	public int getPriority() {
		return this.priority;
	}

	public boolean equals(final MobEntity mob) {
		return this.target.getId() == mob.getId();
	}

	public MobEntity getTarget() {
		return this.target;
	}

	@Override
	public int compareTo(final OpponentData o) {
		return Integer.compare(this.priority, o.priority);
	}
}
