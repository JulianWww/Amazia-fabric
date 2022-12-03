package net.denanu.amazia.entities.village.server.goal.farming;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class AmaziaGoToFarmGoal extends AmaziaGoToBlockGoal<AmaziaVillagerEntity> {

	public AmaziaGoToFarmGoal(final AmaziaVillagerEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	protected BlockPos getTargetBlock() {
		if (this.entity.getCanUpdate()) {
			final ServerWorld sword = (ServerWorld) this.entity.getWorld();
			final BlockPos pos = this.entity.getVillage().getFarming().getRandomPos(sword, this.entity);
			return pos == null ? null : pos.up();
		}
		return null;
	}

}
