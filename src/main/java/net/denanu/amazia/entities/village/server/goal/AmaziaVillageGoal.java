package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

public class AmaziaVillageGoal<E extends AmaziaVillagerEntity> extends BaseAmaziaVillageGoal<E> {
	public AmaziaVillageGoal(E e, int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return super.canStart() && !this.entity.isDeposeting();
	}
}
