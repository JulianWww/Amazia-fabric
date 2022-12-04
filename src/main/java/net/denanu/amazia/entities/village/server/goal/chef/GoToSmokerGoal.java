package net.denanu.amazia.entities.village.server.goal.chef;

import net.denanu.amazia.entities.village.server.ChefEntity;
import net.denanu.amazia.entities.village.server.goal.blacksmithing.GoToBlastFurnaceGoal;
import net.denanu.amazia.village.sceduling.utils.BlockAreaPathingData;

public class GoToSmokerGoal extends GoToBlastFurnaceGoal<ChefEntity> {
	public GoToSmokerGoal(final ChefEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	protected BlockAreaPathingData<?> getTarget() {
		return this.entity.getVillage().getSmoking().getLocation();
	}
}
