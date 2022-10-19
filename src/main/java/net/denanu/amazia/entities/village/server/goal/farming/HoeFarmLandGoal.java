package net.denanu.amazia.entities.village.server.goal.farming;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaVillageGoal;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.denanu.amazia.utils.BlockChecks;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class HoeFarmLandGoal extends TimedVillageGoal {
	public HoeFarmLandGoal(AmaziaVillagerEntity e, int priority) {
		super(e, priority);
	}
	
	@Override
	public boolean canStart() {
		return super.canStart() && this.entity.canHoe() && this.entity.getVillage().getFarming().isHoable(this.entity.getPos());
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
		BlockPos pos = new BlockPos(this.entity.getPos());
		if (BlockChecks.isHoable(this.entity.getWorld().getBlockState(pos.down()).getBlock())) {
			this.entity.getWorld().setBlockState(pos.down(), Blocks.FARMLAND.getDefaultState());
		}
	}
}
