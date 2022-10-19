package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.List;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.village.Village;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class FarmingSceduler extends VillageSceduler {
	public List<BlockPos> possibleFarms;
	public List<BlockPos> crops;
	public List<BlockPos> emptyFarm;
	
	public FarmingSceduler(Village _village) {
		super(_village);
		possibleFarms 	= new ArrayList<BlockPos>();
		crops 			= new ArrayList<BlockPos>();
		emptyFarm 		= new ArrayList<BlockPos>();
	}
	
    public void writeNbt(NbtCompound nbt, String name) {
		JJUtils.writeNBT(nbt, this.possibleFarms, name + ".possibleFarms");
		JJUtils.writeNBT(nbt, this.crops, name + ".crops");
		JJUtils.writeNBT(nbt, this.emptyFarm, name + ".emptyFarm");
    }
    public void readNbt(NbtCompound nbt, String name) {
    	this.possibleFarms = JJUtils.readNBT(nbt, name + ".possibleFarms");
    	this.crops = JJUtils.readNBT(nbt, name + ".crops");
    	this.emptyFarm = JJUtils.readNBT(nbt, name + ".emptyFarm");
    }
    
    protected static boolean isFarmLand(ServerWorld world, BlockPos pos) {
    	return world.getBlockState(pos).getBlock().equals(Blocks.FARMLAND);
    }
    protected static boolean isPotentialFarmLand(ServerWorld world, BlockPos pos) {
    	Block block = world.getBlockState(pos).getBlock();
    	return (block.equals(Blocks.DIRT) || block.equals(Blocks.GRASS_BLOCK)) && isAjacantToFarmBlock(world, pos);
    }
    protected static boolean isAjacantToFarmBlock(ServerWorld world, BlockPos pos) {
    	return (
    			isFarmLand(world, pos.east()) ||
    			isFarmLand(world, pos.west()) ||
    			isFarmLand(world, pos.north()) ||
    			isFarmLand(world, pos.south())
    		);
    }
    
    public void discover(ServerWorld world, BlockPos pos) {
    	this.discoverFarmland(world, pos, true);
    	this.discoverCrops(world, pos);
    	this.discoverEmptyFarmland(world, pos);
    	this.discoverEmptyFarmland(world, pos.down());
    }
    
    private void discoverCrops(ServerWorld world, BlockPos pos) {
    	BlockState state = world.getBlockState(pos);
    	if (state.getBlock() instanceof CropBlock crop) {
    		if (crop.isMature(state)) {
    			this.addCrop(world, pos);
    			return;
    		}
    	}
    	if (state.getBlock() instanceof SugarCaneBlock cane) {
    		if (world.getBlockState(pos.down()).isOf(cane) && world.getBlockState(pos.down(2)).isOf(cane)) {
    			this.addCrop(world, pos.down(2));
    		}
    	}
    	this.removeCrop(world, pos);
    	this.removeCrop(world, pos.down());
	}
    
    private void discoverEmptyFarmland(ServerWorld world, BlockPos pos) {
    	if (isFarmLand(world, pos) && world.isAir(pos.up())) {
    		this.addEmptyFarmland(world, pos);
    	}
    	else {
    		this.removeEmptyFarmland(world, pos);
    	}
    }

	protected void discoverFarmland(ServerWorld world, BlockPos pos, boolean propagate) {
    	if (isFarmLand(world, pos)) {
    		removePossibleFarmLand(world, pos);
    		if (propagate) {
    			this.discoverAjacantFarmland(world, pos);
    		}
    	}
    	else if (isPotentialFarmLand(world, pos)) {
    		this.addPossibleFarmLand(world, pos);
    	}
    	else {
    		this.removePossibleFarmLand(world, pos);
    		if (propagate) {
    			this.discoverAjacantFarmland(world, pos);
    		}
    	}
    }
	private void discoverAjacantFarmland(ServerWorld world, BlockPos pos) {
		this.discoverFarmland(world, pos.east(), false);
		this.discoverFarmland(world, pos.west(), false);
		this.discoverFarmland(world, pos.north(), false);
		this.discoverFarmland(world, pos.south(), false);
	}

	private void addPossibleFarmLand(ServerWorld world, BlockPos pos) {
		if (!this.possibleFarms.contains(pos)) {
			markBlockAsFound(pos, world);
			this.possibleFarms.add(pos);
			this.setChanged();
		}
	}
	private void removePossibleFarmLand(ServerWorld world, BlockPos pos) {
		if (this.possibleFarms.contains(pos)) {
			markBlockAsLost(pos, world);
			this.possibleFarms.remove(pos);
			this.setChanged();
		}
	}
	private void addCrop(ServerWorld world, BlockPos pos) {
		if (!this.crops.contains(pos)) {
			markBlockAsFound(pos, world);
			this.crops.add(pos);
			this.setChanged();
		}
	}
	private void removeCrop(ServerWorld world, BlockPos pos) {
		if (this.crops.contains(pos)) {
			markBlockAsLost(pos, world);
			this.crops.remove(pos);
			this.setChanged();
		}
	}
	private void addEmptyFarmland(ServerWorld world, BlockPos pos) {
		if (!this.emptyFarm.contains(pos)) {
			markBlockAsFound(pos, world);
			this.emptyFarm.add(pos);
			this.setChanged();
		}
	}
	private void removeEmptyFarmland(ServerWorld world, BlockPos pos) {
		if (this.emptyFarm.contains(pos)) {
			markBlockAsLost(pos, world);
			this.emptyFarm.remove(pos);
			this.setChanged();
		}
	}
	
	public BlockPos getRandomPos(ServerWorld world, AmaziaVillagerEntity entity) {
		if (this.crops.size() > 0 && entity.canHarvest()) {
			return getRandomListElement(this.crops).down();
		}
		if (this.emptyFarm.size() > 0 && entity.canPlant()) {
			return getRandomListElement(this.emptyFarm);
		}
		if (this.possibleFarms.size() > 0 && entity.canHoe()) {
			return getRandomListElement(this.possibleFarms);
		}
		return null;
	}

	public boolean isHoable(Vec3d loc) {
		BlockPos pos = new BlockPos(loc);
		return this.possibleFarms.contains(pos.down());
	}

	public static boolean isPlantable(LivingEntity entity) {
		BlockPos pos = new BlockPos(entity.getPos());
		return (
				entity.getWorld().getBlockState(pos).getBlock().equals(Blocks.FARMLAND) &&
				entity.getWorld().isAir(pos.up())
			);
	}

	public static boolean isHarvistable(LivingEntity entity) {
		BlockState state = entity.getWorld().getBlockState(new BlockPos(entity.getPos()).up());
		if (state.getBlock() instanceof CropBlock crop) {
			return crop.isMature(state);
		}
		if (state.getBlock() instanceof SugarCaneBlock crop) {
			return true;
		}
		return false;
	}
}
