package net.denanu.amazia.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CuboidSampler extends Sampler{
	protected int sampleIdx = 0;
	
	public CuboidSampler() {
		super();
	}
	public CuboidSampler(Vec3d pos, int _size_x, int _size_y, int _size_z, int _offset_x, int _offset_y, int _offset_z) {
		super(pos, _size_x, _size_y, _size_z, _offset_x, _offset_y, _offset_z);
	}
	public CuboidSampler(int _x, int _y, int _z, int _size_x, int _size_y, int _size_z, int _offset_x, int _offset_y, int _offset_z) {
		super(_x, _y, _z, _size_x, _size_y, _size_z, _offset_x, _offset_y, _offset_z);
	}
	public CuboidSampler(Vec3d pos, int _size_x, int _size_y, int _size_z) {
		super(pos, _size_x, _size_y, _size_z);
	}
	public CuboidSampler(int _x, int _y, int _z, int _size_x, int _size_y, int _size_z) {
		super(_x, _y, _z, _size_x, _size_y, _size_z);
	}

	@Override
	public BlockPos getPos() {
		int dx = sampleIdx % this.getSizeX();
		int idx = Math.floorDiv(sampleIdx, this.getSizeX());
		int dz =  idx % this.getSizeZ();
		idx = Math.floorDiv(idx, this.getSizeZ());
		int dy =  idx;
		if (dy >= this.getSizeY()) {
			this.sampleIdx = 0;
			return this.getPos();
		}
		this.sampleIdx++;
		return new BlockPos(this.x + dx, this.y + dy, this.z + dz);
	}

}
