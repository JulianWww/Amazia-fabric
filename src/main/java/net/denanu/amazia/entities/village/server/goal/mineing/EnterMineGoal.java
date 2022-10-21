package net.denanu.amazia.entities.village.server.goal.mineing;

import net.denanu.amazia.entities.village.server.MinerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.village.structures.MineStructure;
import net.minecraft.util.math.BlockPos;

public class EnterMineGoal extends AmaziaGoToBlockGoal<MinerEntity> {

	public EnterMineGoal(MinerEntity e, int priority) {
		super(e, priority);
	}
	
	@Override
	public boolean canStart() {
		return !this.entity.isInMine() && super.canStart() && this.entity.canMine();
	}
	
	@Override
	public void tick() {
		super.tick();
		if (this.entity.getMine() != null && this.entity.getMine().isIn(this.entity.getBlockPos())) {
			this.reached =  true;
		}
	}
	
	@Override
	public void stop() {
		super.stop();
		if (this.entity.getMine() != null && this.entity.getMine().isIn(new BlockPos(this.entity.getPos()))) {
			this.entity.enterMine();
		}
	}

	@Override
	protected BlockPos getTargetBlock() {
		MineStructure mine = this.entity.setMine();
		return mine;
	}

}
