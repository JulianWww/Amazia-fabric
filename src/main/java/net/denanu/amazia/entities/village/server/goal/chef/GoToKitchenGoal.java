package net.denanu.amazia.entities.village.server.goal.chef;

import net.denanu.amazia.entities.village.server.ChefEntity;
import net.denanu.amazia.entities.village.server.goal.utils.GoToCraftingLocationGoal;
import net.denanu.amazia.village.sceduling.utils.BlockAreaPathingData;

public class GoToKitchenGoal extends GoToCraftingLocationGoal<ChefEntity> {
	public GoToKitchenGoal(final ChefEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	public BlockAreaPathingData<?> getTarget() {
		return this.entity.getVillage().getSmoking().getKitchenLocation();
	}
}