package net.denanu.amazia.entities.village.server.goal;

import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.denanu.amazia.mechanics.hunger.IAmaziaFoodConsumerEntity;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.mob.MobEntity;

public class AmaziaLookAroundGoal extends LookAroundGoal {
	protected final IAmaziaFoodConsumerEntity eater;
	public AmaziaLookAroundGoal(final MobEntity mob) {
		super(mob);
		if (mob instanceof final IAmaziaFoodConsumerEntity eater) {
			this.eater = eater;
		}
		else {
			this.eater = null;
		}
	}

	@Override
	public void stop() {
		if (this.eater != null) {
			ActivityFoodConsumerMap.lookAroundUseFood(this.eater);
		}
	}
}
