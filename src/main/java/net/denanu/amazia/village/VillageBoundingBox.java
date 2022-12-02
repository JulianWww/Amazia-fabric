package net.denanu.amazia.village;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.border.WorldBorderStage;

public class VillageBoundingBox {
	private final double min_x, min_z, max_x, max_z;

	public VillageBoundingBox(final Box box) {
		this(
				box.minX,
				box.minZ,
				box.maxX,
				box.maxZ
				);
	}

	public VillageBoundingBox(final double min_x, final double min_z, final double max_x, final double max_z) {
		this.min_x = min_x;
		this.min_z = min_z;
		this.max_x = max_x;
		this.max_z = max_z;
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof final VillageBoundingBox box) {
			return
					box.min_x == this.min_x &&
					box.min_z == this.min_z &&
					box.max_x == this.max_x &&
					box.max_z == this.max_z;
		}
		return false;
	}

	public boolean isIn(final BlockPos pos) {
		return pos.getX() >= this.min_x && pos.getX() <= this.max_x && pos.getZ() >= this.min_z && pos.getZ() <= this.max_z;
	}

	public double getBoundEast() {
		return this.max_x;
	}

	public double getBoundSouth() {
		return this.max_z;
	}

	public double getBoundNorth() {
		return this.min_z;
	}

	public double getBoundWest() {
		return this.min_x;
	}

	@Override
	public int hashCode() {
		long l = Double.doubleToLongBits(this.min_x);
		int i = (int)(l ^ l >>> 32);
		l = Double.doubleToLongBits(this.min_z);
		i = 31 * i + (int)(l ^ l >>> 32);
		l = Double.doubleToLongBits(this.max_x);
		i = 31 * i + (int)(l ^ l >>> 32);
		l = Double.doubleToLongBits(this.max_z);
		i = 31 * i + (int)(l ^ l >>> 32);
		return i;
	}

	public double getDistanceInsideBorder(final double x, final double z) {
		final double d = z - this.getBoundNorth();
		final double e = this.getBoundSouth() - z;
		final double f = x - this.getBoundWest();
		final double g = this.getBoundEast() - x;
		double h = Math.min(f, g);
		h = Math.min(h, d);
		return Math.min(h, e);
	}

	public WorldBorderStage getStage() {
		return WorldBorderStage.STATIONARY;
	}
}
