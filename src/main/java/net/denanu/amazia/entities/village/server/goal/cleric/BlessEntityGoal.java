package net.denanu.amazia.entities.village.server.goal.cleric;

import net.denanu.amazia.data.AmaziaStatusEffects;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.ClericEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.denanu.amazia.mechanics.leveling.AmaziaXpGainMap;
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
			ActivityFoodConsumerMap.blessUseFood(this.entity);
			AmaziaXpGainMap.gainBlessXp(this.entity);
		}
		else if (this.movementGoal.getTargetEntity() instanceof final AmaziaVillagerEntity villager) {
			villager.addStatusEffect(new StatusEffectInstance(
					AmaziaStatusEffects.BLESSING,
					this.entity.getBlessLastingTime(),
					(int) Math.ceil(this.entity.getLevel())
					));
			ActivityFoodConsumerMap.blessUseFood(this.entity);
			AmaziaXpGainMap.gainBlessXp(this.entity);
		}
	}

}
