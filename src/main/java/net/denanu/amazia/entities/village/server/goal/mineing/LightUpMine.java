package net.denanu.amazia.entities.village.server.goal.mineing;

import net.denanu.amazia.entities.village.server.MinerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class LightUpMine extends TimedMineGoal {
	protected MinerEntity entity;

	public LightUpMine(MinerEntity e, int priority) {
		super(e, priority);
		this.entity = e;
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getBlockPlaceTime();
	}

	@Override
	protected void takeAction() {
		this.entity.getWorld().setBlockState(new BlockPos(this.entity.getPos()), Blocks.TORCH.getDefaultState());
	}

	@Override
	protected boolean shouldStart() {
		return this.entity.getWorld().getLightLevel(new BlockPos(this.entity.getPos())) < 9;
	}

}
