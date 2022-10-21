package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;

public abstract class AmaziaGoToBlockGoal<E extends AmaziaVillagerEntity> extends BaseAmaziaGoToBlockGoal<E> {

	public AmaziaGoToBlockGoal(E e, int priority) {
		super(e, priority);
	}
	@Override
	public boolean canStart() {
		return super.canStart() && !this.entity.isDeposeting();
	}
}
