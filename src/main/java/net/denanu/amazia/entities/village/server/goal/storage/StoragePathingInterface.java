package net.denanu.amazia.entities.village.server.goal.storage;

import net.minecraft.util.math.BlockPos;

public interface StoragePathingInterface {
	public BlockPos getTargetBlockPos();

	public boolean canStartPathing();
	
	public void endPathingPhase();
}
