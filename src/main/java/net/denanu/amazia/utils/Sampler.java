package net.denanu.amazia.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class Sampler {
	protected int x, y, z;
	protected int size_x, size_y, size_z;
	protected int offset_x, offset_y, offset_z;
	
	public Sampler() {
		this(0,0,0, 0,0,0, 0,0,0);
	}
	public Sampler(Vec3d pos, int _size_x, int _size_y, int _size_z, int _offset_x, int _offset_y, int _offset_z) {
		this.set(pos, _size_x, _size_y, _size_z, _offset_x, _offset_y, _offset_z);
	}
	public Sampler(int _x, int _y, int _z, int _size_x, int _size_y, int _size_z, int _offset_x, int _offset_y, int _offset_z) {
		this.set(_x, _y, _z, _size_x, _size_y, _size_z, _offset_x, _offset_y, _offset_z);
	}
	public Sampler(Vec3d pos, int _size_x, int _size_y, int _size_z) {
		this.set(pos, _size_x, _size_y, _size_z);
	}
	public Sampler(int _x, int _y, int _z, int _size_x, int _size_y, int _size_z) {
		this.set(_x, _y, _z, _size_x, _size_y, _size_z);
	}
	
	public int getX() { return this.x + this.offset_x; }
	public int getY() { return this.y + this.offset_y; }
	public int getZ() { return this.z + this.offset_z; }
	public int getSizeX() { return this.size_x; }
	public int getSizeY() { return this.size_y; }
	public int getSizeZ() { return this.size_z; }
	public int getOffsetX() { return this.offset_x; }
	public int getOffsetY() { return this.offset_y; }
	public int getOffsetZ() { return this.offset_z; }
	
	public void setX(int val) { this.x = val - this.offset_x; }
	public void setY(int val) { this.y = val - this.offset_y; }
	public void setZ(int val) { this.z = val - this.offset_z; }
	public void setSizeX(int val) { this.size_x = val; }
	public void setSizeY(int val) { this.size_y = val; }
	public void setSizeZ(int val) { this.size_z = val; }
	public void setOffsetX(int val) { this.offset_x = val; }
	public void setOffsetY(int val) { this.offset_y = val; }
	public void setOffsetZ(int val) { this.offset_z = val; }
	public void setOffsetX() { this.setOffsetX(Math.floorDiv(this.size_x, 2)); }
	public void setOffsetY() { this.setOffsetY(Math.floorDiv(this.size_y, 2)); }
	public void setOffsetZ() { this.setOffsetZ(Math.floorDiv(this.size_z, 2)); }
	
	public void setDefaultOffset() {
		this.setOffsetX();
		this.setOffsetY();
		this.setOffsetZ();
	}
	
	public void setPos(int x, int y, int z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	public void setPos(BlockPos pos) {
		this.setPos(pos.getX(), pos.getY(), pos.getZ());
	}
	public void setPos(Vec3d pos) {
		this.setPos(
				(int)Math.floor(pos.getX() + 0.5f),
				(int)Math.floor(pos.getY() + 0.5f),
				(int)Math.floor(pos.getZ() + 0.5f)
			);
	}
	public void setSize(int x, int y, int z) {
		this.setSizeX(x);
		this.setSizeY(y);
		this.setSizeZ(z);
	}
	public void setOffset(int x, int y, int z) {
		this.setOffsetX(x);
		this.setOffsetY(y);
		this.setOffsetZ(z);
	}
	public void set(int _x, int _y, int _z, int _size_x, int _size_y, int _size_z, int _offset_x, int _offset_y, int _offset_z) {
		this.setSize(_size_x, _size_y, _size_z);
		this.setOffset(_offset_x, _offset_y, _offset_z);
		this.setPos(_x, _y, _z);
	}
	public void set(Vec3d pos, int _size_x, int _size_y, int _size_z, int _offset_x, int _offset_y, int _offset_z) {
		this.setSize(_size_x, _size_y, _size_z);
		this.setOffset(_offset_x, _offset_y, _offset_z);
		this.setPos(pos);
	}
	public void set(int _x, int _y, int _z, int _size_x, int _size_y, int _size_z) {
		this.setSize(_size_x, _size_y, _size_z);
		this.setDefaultOffset();
		this.setPos(_x, _y, _z);
	}
	public void set(Vec3d pos, int _size_x, int _size_y, int _size_z) {
		this.setSize(_size_x, _size_y, _size_z);
		this.setDefaultOffset();
		this.setPos(pos);
	}
	
	public abstract BlockPos getPos();
}
