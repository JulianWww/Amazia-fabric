package net.denanu.amazia.entities.village.server.goal.cleric;

import net.denanu.amazia.entities.village.server.ClericEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class BlessEntityGoal extends TimedVillageGoal<ClericEntity> {
	GoToBlessEntityGoal movementGoal;

	public BlessEntityGoal(final ClericEntity e, final int priority, final GoToBlessEntityGoal movementGoal) {
		super(e, priority);
		this.movementGoal = movementGoal;
	}

	@Override
	public boolean canStart() {
		return !this.movementGoal.isRunning() && this.movementGoal.getLastTarget() != null && this.movementGoal.getLastTarget().getBlockPos().getManhattanDistance(this.entity.getBlockPos()) < 5 && super.canStart();
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getBlessTime();
	}

	@Override
	public void stop() {
		super.stop();
		this.movementGoal.forgetLastTarget();
	}

	@Override
	protected void takeAction() {
		if (this.movementGoal.getLastTarget() instanceof final PlayerEntity player) {
			player.addStatusEffect(new StatusEffectInstance(
					StatusEffects.LUCK,
					this.entity.getBlessLastingTime()
					));
		}
	}

}
