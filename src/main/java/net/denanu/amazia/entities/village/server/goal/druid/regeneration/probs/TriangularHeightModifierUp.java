package net.denanu.amazia.entities.village.server.goal.druid.regeneration.probs;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class TriangularHeightModifierUp implements AmaziaGenerationProbability {
	private final int top, bottom;

	public TriangularHeightModifierUp(final int top, final int bottom) {
		this.top = top;
		this.bottom = bottom;
	}

	@Override
	public boolean success(final float ability, final BlockPos pos, final ServerWorld world, final Random rand) {
		if (pos.getY() > this.top || pos.getY() < this.bottom) {
			return false;
		}
		final int prob = this.top - pos.getY();
		return prob >= rand.nextInt(this.bottom + this.top);
	}

}
