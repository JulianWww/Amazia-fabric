package net.denanu.amazia.entities.village.server.goal.lumber;

import net.denanu.amazia.entities.village.server.LumberjackEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.minecraft.util.math.BlockPos;

public class GoToLumberLocationGoal extends AmaziaGoToBlockGoal<LumberjackEntity> {

	public GoToLumberLocationGoal(LumberjackEntity e, int priority) {
		super(e, priority);
	}
	
	@Override
	public boolean canStart() {
		boolean a = this.entity.hasLumberLoc();
		//boolean b = !this.entity.isInLumberLoc();
		//boolean c = super.canStart();
		return this.entity.hasLumberLoc() && !this.entity.isInLumberLoc() && super.canStart();
	}

	@Override
	protected BlockPos getTargetBlock() {
		BlockPos pos = this.entity.getLumberingLoc().getAccessPoint();
		return pos;
	}

}
