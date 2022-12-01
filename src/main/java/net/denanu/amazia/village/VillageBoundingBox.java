package net.denanu.amazia.village;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.border.WorldBorderStage;

public class VillageBoundingBox {
	private final int min_x, min_z, max_x, max_z;

	public VillageBoundingBox(final int min_x, final int min_z, final int max_x, final int max_z) {
		this.min_x = min_x;
		this.min_z = min_z;
		this.max_x = max_x;
		this.max_z = max_z;
	}

	public VillageBoundingBox(final PacketByteBuf buf) {
		this(
				buf.readInt(),
				buf.readInt(),
				buf.readInt(),
				buf.readInt()
				);
	}

	public static PacketByteBuf toBuf(final PacketByteBuf buf, BlockPos pos, final int range) {
		if (pos == null) {
			pos = BlockPos.ORIGIN;
		}
		buf.writeInt(pos.getX() - range);
		buf.writeInt(pos.getZ() - range);
		buf.writeInt(pos.getX() + range);
		buf.writeInt(pos.getZ() + range);
		return buf;
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

	public static PacketByteBuf toBuf(final BlockPos pos, final int range) {
		return VillageBoundingBox.toBuf(PacketByteBufs.create(), pos, range);
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
		return this.min_z;
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
