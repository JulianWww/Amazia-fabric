package net.denanu.amazia.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

public class CuboidChunkSampler extends ChunkSampler {
protected int sampleIdx = 0;
	
	public CuboidChunkSampler(ChunkPos pos, int _size_x, int _size_z) {
		super(pos.x, pos.z, _size_x, _size_z);
	}
	public CuboidChunkSampler(LivingEntity entity, int _size_x, int _size_z, int _offset_x, int _offset_z) {
		super(entity, _size_x, _size_z, _offset_x, _offset_z);
	}
	public CuboidChunkSampler(int _x, int _z, int _size_x, int _size_z, int _offset_x, int _offset_z) {
		super(_x, _z, _size_x, _size_z, _offset_x, _offset_z);
	}
	public CuboidChunkSampler(LivingEntity entity, int _size_x, int _size_z) {
		super(entity, _size_x, _size_z);
	}
	public CuboidChunkSampler(int _x, int _z, int _size_x, int _size_z) {
		super(_x, _z, _size_x, _size_z);
	}

	@Override
	public ChunkPos getPos() {
		int dx = sampleIdx % this.getSizeX();
		int idx = Math.floorDiv(sampleIdx, this.getSizeX());
		int dz =  idx % this.getSizeZ();
		if (dz >= this.getSizeZ()) {
			this.sampleIdx = 0;
			return this.getPos();
		}
		this.sampleIdx++;
		return new ChunkPos(this.x + dx, this.z + dz);
	}

}
