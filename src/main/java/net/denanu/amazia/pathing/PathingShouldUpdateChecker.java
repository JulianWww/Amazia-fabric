package net.denanu.amazia.pathing;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class PathingShouldUpdateChecker {
	public static boolean shouldUpdate(final BlockPos pos, final BlockState oldState, final BlockState newState) {
		if (oldState.getBlock() == newState.getBlock()) {
			return false;
		}
		if (oldState.isOf(Blocks.DIRT) && oldState.isOf(Blocks.GRASS_BLOCK) || oldState.isOf(Blocks.DIRT_PATH) && newState.isOf(Blocks.FARMLAND)) {
			return false;
		}
		return true;
	}
}
