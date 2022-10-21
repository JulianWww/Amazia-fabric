package net.denanu.amazia.entities.village.server.goal.farming;

import net.denanu.amazia.entities.village.server.FarmerEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.village.sceduling.FarmingSceduler;

public class PlantCropsGoal extends TimedVillageGoal<FarmerEntity> {

	public PlantCropsGoal(FarmerEntity e, int priority) {
		super(e, priority);
		entity = e;
	}
	
	@Override
	public boolean canStart() {
		return super.canStart() && this.entity.canPlant() && FarmingSceduler.isPlantable(this.entity);
	}
	
	@Override
	public boolean shouldContinue() {
		return this.canStart() && super.shouldContinue();
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getPlantTime();
	}

	@Override
	protected void takeAction() {
		this.entity.plant();
	}
}
