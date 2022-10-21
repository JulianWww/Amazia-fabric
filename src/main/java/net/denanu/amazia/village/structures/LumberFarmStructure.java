package net.denanu.amazia.village.structures;

import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.LumberSceduler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LumberFarmStructure extends VillageStructure {

	public LumberFarmStructure(BlockPos _center, Village village) {
		super(_center, village);
	}

	@Override
	public boolean isIn(BlockPos pos) {
		return  Math.abs(pos.getX() - this.getX()) <= 1 &&
				Math.abs(pos.getZ() - this.getZ()) <= 1 &&
				pos.getY() >= this.getY() && pos.getY() <= this.getY()+1;
	}
	
	public BlockPos getPlantLoc() {
		return this.up();
	}
	
	public boolean isEmpty(World world) {
		return LumberSceduler.isEmpty((ServerWorld)world, this.getPlantLoc());
	}
	public boolean isFull(World world) {
		return LumberSceduler.isLog((ServerWorld)world, this.getPlantLoc());
	}
}
