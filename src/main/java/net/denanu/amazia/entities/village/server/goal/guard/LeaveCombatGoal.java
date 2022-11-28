package net.denanu.amazia.entities.village.server.goal.guard;

import net.denanu.amazia.entities.village.server.GuardEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.minecraft.util.math.BlockPos;

public class LeaveCombatGoal extends AmaziaGoToBlockGoal<GuardEntity> {

	public LeaveCombatGoal(final GuardEntity e, final int priority, final float speed) {
		super(e, priority, speed);
	}

	@Override
	public boolean canStart() {
		return this.entity.shouldFlee() && super.canStart();
	}

	@Override
	public void stop() {
		super.stop();
		this.entity.endShouldFlee();
	}

	@Override
	protected BlockPos getTargetBlock() {
		return this.entity.getVillage().getPathingGraph().getRandomNode();
	}

}
