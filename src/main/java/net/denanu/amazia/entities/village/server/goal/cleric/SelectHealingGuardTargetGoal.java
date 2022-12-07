package net.denanu.amazia.entities.village.server.goal.cleric;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.ClericEntity;
import net.denanu.amazia.entities.village.server.GuardEntity;

public class SelectHealingGuardTargetGoal extends SelectHealingTargetGoal<GuardEntity> {
	ClericEntity cleric;
	public SelectHealingGuardTargetGoal(final ClericEntity mob, final Class<GuardEntity> targetClass, final boolean checkVisibility) {
		super(mob, targetClass, checkVisibility);
		this.cleric = mob;
	}

	public SelectHealingGuardTargetGoal(final ClericEntity mob, final Class<GuardEntity> targetClass, final boolean checkVisibility, final boolean checkCanNavigate) {
		super(mob, targetClass, checkVisibility, checkCanNavigate);
		this.cleric = mob;
	}

	public SelectHealingGuardTargetGoal(final ClericEntity mob, final Class<GuardEntity> targetClass, final int reciprocalChance, final boolean checkVisibility, final boolean checkCanNavigate) {
		super(mob, targetClass, reciprocalChance, checkVisibility, checkCanNavigate, null);
		this.cleric = mob;
	}

	@Override
	protected void findClosestTarget() {
		if (AmaziaVillagerEntity.isNotFullHealth(this.cleric)) {
			this.targetEntity = this.cleric;
		}
		else if (this.cleric.hasVillage()) {
			this.targetEntity =  this.getLowestHpEntity(
					this.cleric.getVillage().getGuarding().getCombatants()
					);
		}
	}
}
