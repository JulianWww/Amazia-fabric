package net.denanu.amazia.entities.village.server.goal.mechanics.hunger;

import net.denanu.amazia.entities.village.server.goal.utils.TimedGoal;
import net.denanu.amazia.mechanics.hunger.AmaziaFoodConsumerEntity;

public class EatGoal extends TimedGoal {
	private final AmaziaFoodConsumerEntity entity;

	public EatGoal(final AmaziaFoodConsumerEntity entity) {
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
