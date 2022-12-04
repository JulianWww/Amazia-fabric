package net.denanu.amazia.entities.village.server.goal.utils;

import net.denanu.amazia.entities.village.server.AmaziaSmelterVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.storage.CraftGoal;

public class CraftAtCraftingLocationGoal extends CraftGoal<AmaziaSmelterVillagerEntity> {

	public CraftAtCraftingLocationGoal(final AmaziaSmelterVillagerEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return !this.entity.shouldDeposit && super.canStart() && this.entity.getTargetCraftPos() != null && this.entity.getBlockPos().getManhattanDistance(this.entity.getTargetCraftPos()) <= 2;
	}

	@Override
	public void takeAction() {
		super.takeCraftAction();
		if (this.entity.getCraftInput() == null || !CraftGoal.canCraft(this.entity)) {
			this.entity.endCraft();
			this.entity.sceduleDepositing();
			//this.entity.requestCraftable();
		}
	}
}
