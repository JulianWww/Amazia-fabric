package net.denanu.amazia.entities.village.server.goal.druid.regeneration;


import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public interface AmaziaDruidRegenerator {
	public void place(ServerWorld world, BlockPos pos);
	public boolean test(float regrowAbility, Random rand, BlockPos pos, ServerWorld world);
}
