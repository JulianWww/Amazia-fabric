package net.denanu.amazia.utils.blockPos;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ReversableBox extends Box {
	private final boolean inverseX, inverseY, inverseZ;

	/**
	 * Creates a box of the given positions as corners.
	 */
	public ReversableBox(final double x1, final double y1, final double z1, final double x2, final double y2,
			final double z2) {
		super(x1, y1, z1, x2, y2, z2);

		this.inverseX = x2 > x1;
		this.inverseY = y2 > y1;
		this.inverseZ = z2 > z1;
	}

	/**
	 * Creates a box that only contains the given block position.
	 */
	public ReversableBox(final BlockPos pos) {
		this(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
	}

	/**
	 * Creates a box of the given positions as corners.
	 */
	public ReversableBox(final BlockPos pos1, final BlockPos pos2) {
		this(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
	}

	/**
	 * Creates a box of the given positions as corners.
	 */
	public ReversableBox(final Vec3d pos1, final Vec3d pos2) {
		this(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
	}

	public boolean isInverseX() {
		return this.inverseX;
	}

	public boolean isInverseY() {
		return this.inverseY;
	}

	public boolean isInverseZ() {
		return this.inverseZ;
	}
}
