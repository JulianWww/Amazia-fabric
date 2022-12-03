package net.denanu.amazia.utils.blockPos;

import java.util.stream.Stream;

import net.denanu.amazia.JJUtils;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;

public class BlockPosStream {
	public static Stream<BlockPos> circle(final BlockPos origin, final int r) {
		final int rr = r*r;
		final BlockBox box = BlockBox.create(origin.add(r, 0, r), origin.add(-r-2, 0,-r-2));
		return BlockPos.stream(box).filter(
				pos -> JJUtils.square(pos.getX() - origin.getX()) + JJUtils.square(pos.getZ() - origin.getZ()) < rr
				);
	}
}
