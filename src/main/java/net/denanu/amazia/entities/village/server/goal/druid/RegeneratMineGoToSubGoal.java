package net.denanu.amazia.entities.village.server.goal.druid;

import net.denanu.amazia.entities.village.server.DruidEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.denanu.amazia.village.structures.MineStructure;
import net.minecraft.util.math.BlockPos;

public class RegeneratMineGoToSubGoal extends AmaziaGoToBlockGoal<DruidEntity> {
	public RegeneratMineGoToSubGoal(final DruidEntity e, final int priority) {
		super(e, priority);
	}


	@Override
	public void tick() {
		super.tick();
		if (this.entity.getMine() != null && this.entity.getMine().isIn(this.entity.getBlockPos())) {
			this.reached =  true;
		}
	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
		if (this.entity.getMine() != null && this.entity.getMine().isIn(this.entity.getBlockPos())) {

		}
	}

	@Override
	protected BlockPos getTargetBlock() {
		final MineStructure mine = this.entity.setMine();
		return mine;
	}

}
