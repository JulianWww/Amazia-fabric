package net.denanu.amazia.entities.village.server.goal.farming;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.mechanics.hunger.ActivityFoodConsumerMap;
import net.denanu.amazia.utils.BlockChecks;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class HoeFarmLandGoal extends TimedVillageGoal<AmaziaVillagerEntity> {
	public HoeFarmLandGoal(final AmaziaVillagerEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		return super.canStart() && this.entity.getVillage().getFarming().isHoable(this.entity.getPos()) && this.entity.canHoe();
	}

	@Override
	public boolean shouldContinue() {
		return this.canStart() && super.shouldContinue();
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getHoeingTime();
	}

	@Override
	protected void takeAction() {
		final BlockPos pos = new BlockPos(this.entity.getPos());
		if (BlockChecks.isHoable(this.entity.getWorld().getBlockState(pos.down()).getBlock())) {
			this.entity.getWorld().setBlockState(pos.down(), Blocks.FARMLAND.getDefaultState());
			ActivityFoodConsumerMap.tillLandUseFood(this.entity);
		}
	}
}
