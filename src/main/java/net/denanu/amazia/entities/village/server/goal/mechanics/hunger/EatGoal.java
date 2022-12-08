package net.denanu.amazia.entities.village.server.goal.mechanics.hunger;

import net.denanu.amazia.entities.village.server.goal.utils.TimedGoal;
import net.denanu.amazia.mechanics.hunger.IAmaziaFoodConsumerEntity;

public class EatGoal extends TimedGoal {
	private final IAmaziaFoodConsumerEntity entity;

	public EatGoal(final IAmaziaFoodConsumerEntity entity) {
		this.entity = entity;
	}

	@Override
	public boolean canStart() {
		return this.entity.shouldEatFood();
	}

	@Override
	protected int getRequiredTime() {
		return 20;
	}

	@Override
	protected void takeAction() {
		this.entity.consumeNutrishousItem();
	}
}
