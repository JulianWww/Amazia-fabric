package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;

public abstract class AmaziaGoToBlockGoal<E extends AmaziaVillagerEntity> extends BaseAmaziaGoToBlockGoal<E> {

	public AmaziaGoToBlockGoal(final E e, final int priority) {
		super(e, priority);
	}

	public AmaziaGoToBlockGoal(final E e, final int priority, final float speed) {
		super(e, priority, speed);
	}

	@Override
	public boolean canStart() {
		//final boolean a = !this.entity.isDeposeting();
		//final boolean b  =super.canStart();
		return super.canStart() && !this.entity.isDeposeting();
	}
}
