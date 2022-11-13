package net.denanu.amazia.entities.village.server.goal.blacksmithing;

import net.denanu.amazia.entities.village.server.BlacksmithEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.minecraft.util.math.BlockPos;

public class GoToBlastFurnaceGoal extends AmaziaGoToBlockGoal<BlacksmithEntity> {

	public GoToBlastFurnaceGoal(BlacksmithEntity e, int priority) {
		super(e, priority);
	}

	@Override
	protected BlockPos getTargetBlock() {
		return null;
	}

}
