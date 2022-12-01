package net.denanu.amazia.entities.village.server.goal.druid.regeneration;


import net.denanu.amazia.entities.village.server.goal.druid.regeneration.probs.AmaziaGenerationProbability;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.OrePlacedFeatures;

public class SingleBlockDruidRegenerator implements AmaziaDruidRegenerator {
	public AmaziaGenerationProbability mod;
	public BlockState state;

	public SingleBlockDruidRegenerator(final AmaziaGenerationProbability mod, final BlockState state) {
		this.mod = mod;
		this.state = state;
	}

	@Override
	public void place(final ServerWorld world, final BlockPos pos) {
		world.setBlockState(pos, this.state);
	}

	protected float probability(final float regrowAbility, final ServerWorld world, final BlockPos pos) {
		OrePlacedFeatures.ORE_COAL_LOWER.value().placementModifiers();
		return regrowAbility;
	}

	@Override
	public boolean test(final float regrowAbility, final Random rand, final BlockPos pos, final ServerWorld world) {
		return this.mod.success(regrowAbility, pos, world, rand);
	}

}
