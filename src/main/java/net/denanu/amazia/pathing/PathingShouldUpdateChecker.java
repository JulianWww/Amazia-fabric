package net.denanu.amazia.pathing;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class PathingShouldUpdateChecker {
	public static boolean shouldUpdate(BlockPos pos, BlockState oldState, BlockState newState, int flags) {
		if (oldState.getBlock() == newState.getBlock()) {
			return false;
		}
		if ((oldState.isOf(Blocks.DIRT) || oldState.isOf(Blocks.GRASS_BLOCK) || oldState.isOf(Blocks.DIRT_PATH) && newState.isOf(Blocks.FARMLAND))) {
			return false;
		}
		return true;
	}
}
