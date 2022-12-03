package net.denanu.amazia.entities.village.server.goal.druid.regeneration;

import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class DefaultFillerGenerator {

	public static void place(final ServerWorld world, final BlockPos pos) {
		world.setBlockState(pos, Blocks.STONE.getDefaultState());
	}
}
