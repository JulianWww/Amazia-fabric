package net.denanu.amazia.entities.village.server.goal.mineing;

import net.denanu.amazia.entities.village.server.MinerEntity;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.denanu.amazia.mechanics.leveling.AmaziaXpGainMap;

public class ExtendMineGoal extends TimedMineGoal {
	public ExtendMineGoal(final MinerEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getMineTime();
	}

	@Override
	public void start() {
		super.start();
		// this.entity.setStackInHand(Hand.MAIN_HAND, this.entity.getPick());
	}

	@Override
	public void stop() {
		super.stop();
		// this.entity.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
	}

	@Override
	protected void takeAction() {
		this.entity.getWorld().breakBlock(this.pos, true);
		this.entity.addSeroundingOreBlock(this.pos);
		this.entity.damage(this.entity.getPick());
		ActivityFoodConsumerMap.mineBlockUseFood(this.entity);
		AmaziaXpGainMap.gainEnchantXp(this.entity);
	}

	@Override
	protected boolean shouldStart() {
		if (this.pos == null) {
			this.pos = this.entity.getNextOreBlock();
		}
		if (this.pos == null) {
			this.pos = this.entity.getMine().getBlockToExtend(this.entity);
		}
		return this.pos != null;
	}
}
