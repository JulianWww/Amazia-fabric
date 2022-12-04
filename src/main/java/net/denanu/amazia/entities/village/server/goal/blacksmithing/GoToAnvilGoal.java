package net.denanu.amazia.entities.village.server.goal.blacksmithing;

import javax.annotation.Nullable;

import net.denanu.amazia.entities.village.server.BlacksmithEntity;
import net.denanu.amazia.entities.village.server.goal.utils.GoToCraftingLocationGoal;
import net.denanu.amazia.village.sceduling.utils.BlockAreaPathingData;

public class GoToAnvilGoal extends GoToCraftingLocationGoal<BlacksmithEntity> {

	public GoToAnvilGoal(final BlacksmithEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	@Nullable
	public BlockAreaPathingData<?> getTarget() {
		return this.entity.getVillage().getBlacksmithing().getLocation();
	}

}
