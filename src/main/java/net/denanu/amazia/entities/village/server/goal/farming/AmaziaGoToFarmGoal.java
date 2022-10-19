package net.denanu.amazia.entities.village.server.goal.farming;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.AmaziaGoToBlockGoal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class AmaziaGoToFarmGoal extends AmaziaGoToBlockGoal {

	public AmaziaGoToFarmGoal(AmaziaVillagerEntity e, int priority) {
		super(e, priority);
	}

	@Override
	protected BlockPos getTargetBlock() {
		ServerWorld sword = (ServerWorld) this.entity.getWorld();
		BlockPos pos = this.entity.getVillage().getFarming().getRandomPos(sword, this.entity);
		return pos;
	}

}
