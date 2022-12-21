package net.denanu.amazia.utils.blockPos;

import java.util.stream.Stream;

import com.google.common.collect.AbstractIterator;

import net.denanu.amazia.JJUtils;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.MathHelper;

public class BlockPosStream {
	public static Stream<BlockPos> circle(final BlockPos origin, final int r) {
		final var rr = r * r;
		final var box = BlockBox.create(origin.add(r, 0, r), origin.add(-r - 2, 0, -r - 2));
		return BlockPos.stream(box).filter(
				pos -> JJUtils.square(pos.getX() - origin.getX()) + JJUtils.square(pos.getZ() - origin.getZ()) < rr);
	}

	public static Iterable<BlockPos> iterate(final int startX, final int startY, final int startZ, final int endX,
			final int endY, final int endZ) {
		final var i = endX - startX + 1;
		final var j = endY - startY + 1;
		final var k = endZ - startZ + 1;
		final var l = i * j * k;
		return () -> new AbstractIterator<>() {
			private final Mutable pos = new Mutable();
			private int index;

			@Override
			protected BlockPos computeNext() {
				if (this.index == l) {
					return this.endOfData();
				}
				final var dy = this.index % j;
				final var j2 = this.index / j;
				final var dx = j2 % i;
				final var dz = j2 / i;
				++this.index;
				return this.pos.set(startX + dx, startY + dy, startZ + dz);
			}
		};
	}

	public static Iterable<BlockPos> iterate(final int startX, final int startY, final int startZ, final int endX,
			final int endY, final int endZ, final boolean inverseX, final boolean inverseY, final boolean inverseZ) {
		final var i = endX - startX + 1;
		final var j = endY - startY + 1;
		final var k = endZ - startZ + 1;
		final var l = i * j * k;

		final var xBase = inverseX ? endX : startX;
		final var yBase = inverseY ? endY : startY;
		final var zBase = inverseZ ? endZ : startZ;

		return () -> new AbstractIterator<>() {
			private final Mutable pos = new Mutable();
			private int index;

			@Override
			protected BlockPos computeNext() {
				if (this.index == l) {
					return this.endOfData();
				}
				final var dy = this.index % j;
				final var j2 = this.index / j;
				final var dx = j2 % i;
				final var dz = j2 / i;
				++this.index;
				return this.pos.set(inverseX ? xBase - dx : xBase + dx, inverseY ? yBase - dy : yBase + dy,
						inverseZ ? zBase - dz : zBase + dz);
			}
		};
	}

	public static Iterable<BlockPos> iterate(final ReversableBox box) {
		return BlockPosStream.iterate(MathHelper.floor(box.minX), MathHelper.floor(box.minY),
				MathHelper.floor(box.minZ), MathHelper.floor(box.maxX), MathHelper.floor(box.maxY),
				MathHelper.floor(box.maxZ), box.isInverseX(), box.isInverseY(), box.isInverseZ());
	}
}
