package net.denanu.amazia.entities.village.server.goal.utils;

import net.denanu.amazia.entities.village.server.AmaziaSmelterVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.storage.CraftGoal;
import net.denanu.amazia.mechanics.leveling.AmaziaXpGainMap;

public class CraftAtCraftingLocationGoal extends CraftGoal<AmaziaSmelterVillagerEntity> {
	final boolean doMulticraft;

	public CraftAtCraftingLocationGoal(final AmaziaSmelterVillagerEntity e, final int priority, final boolean doMulitiCraft) {
		super(e, priority);
		this.doMulticraft = doMulitiCraft;
	}

	@Override
	public boolean canStart() {
		return !this.entity.shouldDeposit && super.canStart() && this.entity.getTargetCraftPos() != null && this.entity.getBlockPos().getManhattanDistance(this.entity.getTargetCraftPos()) <= 2;
	}

	@Override
	public void takeAction() {
		super.takeCraftAction();
		if (!this.doMulticraft || this.entity.getCraftInput() == null || !CraftGoal.canCraft(this.entity)) {
			this.entity.endCraft();
			this.entity.sceduleDepositing();
			//this.entity.requestCraftable();
		}
		AmaziaXpGainMap.gainCraftXp(this.entity);
	}
}
