package net.denanu.amazia.entities.village.server.goal.cleric;

import net.denanu.amazia.entities.village.server.ClericEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.mechanics.happyness.HappynessMap;
import net.denanu.amazia.mechanics.happyness.IAmaziaHappynessEntity;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.denanu.amazia.mechanics.leveling.AmaziaXpGainMap;
import net.minecraft.entity.damage.DamageSource;

public class HealEntityGoal extends TimedVillageGoal<ClericEntity> {

	public HealEntityGoal(final ClericEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return this.entity.getTarget() != null && super.canStart();
	}


	@Override
	protected int getRequiredTime() {
		return this.entity.getHealTime();
	}

	@Override
	protected void takeAction() {
		if (this.entity.getTarget() != null) {
			if (this.entity.getTarget().isUndead()) {
				this.entity.getTarget().damage(DamageSource.mob(this.entity), this.entity.getHealAmount());
			}
			else {
				this.entity.getTarget().heal(this.entity.getHealAmount());
				if (this.entity.getTarget() instanceof final IAmaziaHappynessEntity happy) {
					HappynessMap.getHealedGainHappyness(happy);
					HappynessMap.gainHealOtherVillagerHappyness(this.entity);
				}
			}
			ActivityFoodConsumerMap.healUseFood(this.entity);
			AmaziaXpGainMap.gainHealXp(this.entity);
		}
		this.entity.setTarget(null);
	}

}
