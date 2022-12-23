package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;

public abstract class AmaziaGoToBlockGoal<E extends AmaziaVillagerEntity> extends BaseAmaziaGoToBlockGoal<E> {

	public AmaziaGoToBlockGoal(final E e, final int priority) {
		super(e, priority);
	}
	public AmaziaGoToBlockGoal(final E e, final int priority, final int foodUsage) {
		super(e, priority, foodUsage);
	}

	public AmaziaGoToBlockGoal(final E e, final int priority, final int foodUsage, final float speed) {
		super(e, priority, foodUsage, speed);
	}

	@Override
	public boolean canStart() {
		return super.canStart() && !this.entity.isDeposeting();
	}
}
