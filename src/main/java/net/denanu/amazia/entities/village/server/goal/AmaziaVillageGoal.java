package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;

public class AmaziaVillageGoal<E extends AmaziaVillagerEntity> extends BaseAmaziaVillageGoal<E> {
	public AmaziaVillageGoal(E e, int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return super.canStart() && !this.entity.isDeposeting();
	}
}
