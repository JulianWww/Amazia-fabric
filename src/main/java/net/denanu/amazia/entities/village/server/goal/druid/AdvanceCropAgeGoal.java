package net.denanu.amazia.entities.village.server.goal.druid;

import net.denanu.amazia.entities.village.server.DruidEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;

public class AdvanceCropAgeGoal extends TimedVillageGoal<DruidEntity> {

	public AdvanceCropAgeGoal(final DruidEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getPlantAdvanceAgeTime();
	}

	@Override
	protected void takeAction() {
		final BlockState state = this.entity.world.getBlockState(this.entity.getToRegrow());
		state.randomTick((ServerWorld)this.entity.world, this.entity.getToRegrow(), this.entity.world.getRandom());
	}

}
