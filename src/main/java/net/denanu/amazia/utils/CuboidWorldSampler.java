package net.denanu.amazia.utils;

import net.minecraft.util.math.BlockPos;

public class CuboidWorldSampler extends CuboidSampler {
	public CuboidWorldSampler(BlockPos pos, int _size_x, int _size_z) {
		super(pos.getX(), 0, pos.getZ(), _size_x, 0, _size_z);
	}
	
	@Override 
	public void setY(int y) { this.y = -64; }
	public void setY() { this.y = -64; }
	@Override
	public void setSizeY(int y) {this.setSizeY(); }
	public void setSizeY() {this.size_y = 319; }
	@Override
	public void setOffsetY(int y) { this.setOffsetY(); }
	public void setOffsetY() { offset_y = 0; }
}
