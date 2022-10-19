package net.denanu.amazia.village.structures;

import net.denanu.amazia.village.Village;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public abstract class VillageStructure extends BlockPos {
	private final Village village;
	
	public VillageStructure(BlockPos _center, Village village) {
		super(_center.getX(), _center.getY(), _center.getZ());
		this.village = village;
	}
	
	
	public abstract boolean isIn(BlockPos pos);
	public abstract BlockPos getMainEntrance();


	protected BlockPos getCenter() {
		return this;
	}


	public Village getVillage() {
		return village;
	}


	public void writeNbt(NbtCompound nbt, String name) {
	}
	public void readNbt(NbtCompound nbt, String name) {
	}
}
