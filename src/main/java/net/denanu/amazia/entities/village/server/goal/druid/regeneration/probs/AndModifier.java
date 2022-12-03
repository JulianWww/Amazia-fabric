package net.denanu.amazia.entities.village.server.goal.druid.regeneration.probs;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class AndModifier extends LogicalModifier {
	@Override
	public boolean success(final float ability, final BlockPos pos, final ServerWorld world, final Random rand) {
		for (final AmaziaGenerationProbability mod : this.mods) {
			if (!mod.success(ability, pos, world, rand)) {
				return false;
			}
		}
		return true;
	}
}
