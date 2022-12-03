package net.denanu.amazia.entities.village.server.goal.druid.regeneration.probs;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class TrapezoidHeightModifier implements AmaziaGenerationProbability {
	int center;
	TriangularHeightModifierUp top;
	TriangularHeightModifierDown bottom;

	public TrapezoidHeightModifier(final int top, final int bottom) {
		this(
				top,
				Math.floorDiv(top + bottom, 2),
				bottom
				);
	}
	public TrapezoidHeightModifier(final int top, final int center, final int bottom) {
		this.center = center;
		this.top = new TriangularHeightModifierUp(top, center);
		this.bottom = new TriangularHeightModifierDown(center, bottom);
	}

	@Override
	public boolean success(final float ability, final BlockPos pos, final ServerWorld world, final Random rand) {
		if (pos.getY() > this.center) {
			return this.top.success(ability, pos, world, rand);
		}
		return this.bottom.success(ability, pos, world, rand);
	}
}
