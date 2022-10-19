package net.denanu.amazia.entities.village.server.goal.farming;

import net.denanu.amazia.entities.village.server.FarmerEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.village.sceduling.FarmingSceduler;

public class HarvestCropsGoal extends TimedVillageGoal {
	private FarmerEntity entity;

	public HarvestCropsGoal(FarmerEntity e, int priority) {
		super(e, priority);
		entity = e;
	}
	
	@Override
	public boolean canStart() {
		return super.canStart() && this.entity.canHarvest() && FarmingSceduler.isHarvistable(this.entity);
	}
	
	@Override
	public boolean shouldContinue() {
		return this.canStart() && super.shouldContinue();
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getHarvestTime();
	}

	@Override
	protected void takeAction() {
		this.entity.harvest();
	}

}
