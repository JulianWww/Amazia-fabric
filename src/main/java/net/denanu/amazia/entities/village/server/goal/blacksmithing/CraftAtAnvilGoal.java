package net.denanu.amazia.entities.village.server.goal.blacksmithing;

import net.denanu.amazia.entities.village.server.BlacksmithEntity;
import net.denanu.amazia.entities.village.server.goal.storage.CraftGoal;

public class CraftAtAnvilGoal extends CraftGoal<BlacksmithEntity> {

	public CraftAtAnvilGoal(final BlacksmithEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return super.canStart() && this.entity.getTargetAnvilPos() != null && this.entity.getBlockPos().getManhattanDistance(this.entity.getTargetAnvilPos()) <= 2;
	}

	@Override
	public void takeAction() {
		super.takeAction();
		this.entity.sceduleDepositing();
	}
}
