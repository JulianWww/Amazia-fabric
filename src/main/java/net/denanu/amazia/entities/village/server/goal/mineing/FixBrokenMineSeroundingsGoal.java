package net.denanu.amazia.entities.village.server.goal.mineing;

import net.denanu.amazia.entities.village.server.MinerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

public class FixBrokenMineSeroundingsGoal extends TimedMineGoal {
	public FixBrokenMineSeroundingsGoal(MinerEntity e, int priority) {
		super(e, priority);
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getBlockPlaceTime();
	}

	@Override
	protected void takeAction() {
		if (this.pos != null) {
			if (this.entity.hasCobbleStone()) {
				this.entity.world.setBlockState(this.pos, Blocks.COBBLESTONE.getDefaultState());
				this.entity.removeItemFromInventory(Items.COBBLESTONE, 1);
			}
			else {
				this.entity.requestItem(Items.COBBLESTONE);
			}
		}
	}

	@Override
	protected boolean shouldStart() {
		if (this.entity.canMineOre()) {
			return false;
		}
		if (this.pos == null) {
			this.pos = this.entity.getMine().getToFixPos(this.entity);
		}
		return this.pos != null;
	}
}
