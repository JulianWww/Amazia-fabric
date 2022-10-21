package net.denanu.amazia.entities.village.server.goal.storage;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.goal.BaseAmaziaGoToBlockGoal;
import net.minecraft.util.math.BlockPos;

public class GoToStorageGoal extends BaseAmaziaGoToBlockGoal<AmaziaVillagerEntity> {
	private StoragePathingInterface master;

	public GoToStorageGoal(AmaziaVillagerEntity e, StoragePathingInterface _master) {
		super(e, -2);
		master = _master;
	}
	
	@Override
	public boolean canStart() {
		return super.canStart() && master.canStartPathing();
	}
	
	@Override
	public void stop() {
		super.stop();
		master.endPathingPhase();
	}

	@Override
	protected BlockPos getTargetBlock() {
		BlockPos pos = this.master.getTargetBlockPos();
		return this.master.getTargetBlockPos();
	}

}
