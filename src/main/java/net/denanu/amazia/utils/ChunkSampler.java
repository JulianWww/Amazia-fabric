package net.denanu.amazia.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.ChunkPos;

public abstract class ChunkSampler {
	protected int x, z;
	protected int size_x, size_z;
	protected int offset_x, offset_z;
	
	public ChunkSampler() {
		this(0,0, 0,0, 0,0);
	}
	public ChunkSampler(LivingEntity entity, int _size_x, int _size_z, int _offset_x, int _offset_z) {
		this.set(entity, _size_x, _size_z, _offset_x, _offset_z);
	}
	public ChunkSampler(int _x, int _z, int _size_x, int _size_z, int _offset_x, int _offset_z) {
		this.set(_x, _z, _size_x, _size_z, _offset_x, _offset_z);
	}
	public ChunkSampler(LivingEntity entity, int _size_x, int _size_z) {
		this.set(entity, _size_x, _size_z);
	}
	public ChunkSampler(int _x, int _z, int _size_x, int _size_z) {
		this.set(_x, _z, _size_x, _size_z);
	}
	
	public int getX() { return this.x + this.offset_x; }
	public int getZ() { return this.z + this.offset_z; }
	public int getSizeX() { return this.size_x; }
	public int getSizeZ() { return this.size_z; }
	public int getOffsetX() { return this.offset_x; }
	public int getOffsetZ() { return this.offset_z; }
	
	public void setX(int val) { this.x = val - this.offset_x; }
	public void setZ(int val) { this.z = val - this.offset_z; }
	public void setSizeX(int val) { this.size_x = val; }
	public void setSizeZ(int val) { this.size_z = val; }
	public void setOffsetX(int val) { this.offset_x = val; }
	public void setOffsetZ(int val) { this.offset_z = val; }
	public void setOffsetX() { this.setOffsetX(Math.floorDiv(this.size_x, 2)); }
	public void setOffsetZ() { this.setOffsetZ(Math.floorDiv(this.size_z, 2)); }
	
	public void setDefaultOffset() {
		this.setOffsetX();
		this.setOffsetZ();
	}
	
	public void setPos(int x, int z) {
		this.setX(x);
		this.setZ(z);
	}
	public void setPos(ChunkPos pos) {
		this.setPos(pos.x, pos.z);
	}
	public void setPos(LivingEntity entity) {
		this.setPos(
				entity.getChunkPos()
			);
	}
	public void setSize(int x, int z) {
		this.setSizeX(x);
		this.setSizeZ(z);
	}
	public void setOffset(int x, int z) {
		this.setOffsetX(x);
		this.setOffsetZ(z);
	}
	public void set(int _x, int _z, int _size_x, int _size_z, int _offset_x, int _offset_z) {
		this.setSize(_size_x, _size_z);
		this.setOffset(_offset_x, _offset_z);
		this.setPos(_x, _z);
	}
	public void set(LivingEntity entity, int _size_x, int _size_z, int _offset_x, int _offset_z) {
		this.setSize(_size_x, _size_z);
		this.setOffset(_offset_x, _offset_z);
		this.setPos(entity.getChunkPos());
	}
	public void set(int _x, int _z, int _size_x, int _size_z) {
		this.setSize(_size_x, _size_z);
		this.setDefaultOffset();
		this.setPos(_x, _z);
	}
	public void set(LivingEntity entity, int _size_x, int _size_z) {
		this.setSize(_size_x, _size_z);
		this.setDefaultOffset();
		this.setPos(entity.getChunkPos());
	}
	
	public abstract ChunkPos getPos();
}
