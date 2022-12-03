package net.denanu.amazia.entities.village.server.goal.mineing;

import net.denanu.amazia.entities.village.server.MinerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.village.structures.MineStructure;
import net.minecraft.util.math.BlockPos;

public class EnterMineGoal extends AmaziaGoToBlockGoal<MinerEntity> {

	public EnterMineGoal(final MinerEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	public boolean canStart() {
		final boolean v1 = !this.entity.isInMine();
		final boolean v2 = super.canStart();
		final boolean v3 = this.entity.canMine();
		return v1 && v2 && v3;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.entity.getMine() != null && (this.entity.getMine().isIn(this.entity.getBlockPos()) || this.entity.getMine().hasVillager())) {
			this.reached =  true;
		}
	}

	@Override
	public void stop() {
		super.stop();
		if (this.entity.getMine() != null && this.entity.getMine().isIn(new BlockPos(this.entity.getPos()))) {
			this.entity.enterMine();
		}
		else {
			this.entity.leaveMine();
		}
	}

	@Override
	protected BlockPos getTargetBlock() {
		final MineStructure mine = this.entity.setMine();
		return mine;
	}

}
