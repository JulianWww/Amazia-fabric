package net.denanu.amazia.entities.village.server.goal.druid.regeneration.probs;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public interface AmaziaGenerationProbability {
	public boolean success(float ability, BlockPos pos, ServerWorld world, Random rand);
}
