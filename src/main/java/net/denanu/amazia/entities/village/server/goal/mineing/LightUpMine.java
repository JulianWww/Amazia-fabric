package net.denanu.amazia.entities.village.server.goal.mineing;

import net.denanu.amazia.entities.village.server.MinerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class LightUpMine extends TimedMineGoal {

	public LightUpMine(MinerEntity e, int priority) {
		super(e, priority);
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getBlockPlaceTime();
	}

	@Override
	protected void takeAction() {
		if (this.entity.hasItem(Items.TORCH, 1)) {
			this.entity.getWorld().setBlockState(new BlockPos(this.entity.getPos()), Blocks.TORCH.getDefaultState());
			this.entity.removeItemFromInventory(Items.TORCH, 1);
		}
	}

	@Override
	protected boolean shouldStart() {
		boolean hasTorch = this.entity.hasItem(Items.TORCH, 1);
		if (!hasTorch) {
			this.entity.requestItem(Items.TORCH);
		}
		return hasTorch && this.entity.getWorld().getLightLevel(new BlockPos(this.entity.getPos())) < 7;
	}

}
