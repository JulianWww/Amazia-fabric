package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.List;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.village.Village;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class FarmingSceduler extends VillageSceduler {
	public List<BlockPos> farms;
	
	public FarmingSceduler(Village _village) {
		super(_village);
		farms = new ArrayList<BlockPos>();
	}
	
    public void writeNbt(NbtCompound nbt, String name) {
		JJUtils.writeNBT(nbt, this.farms, name + ".farms");
    }
    public void readNbt(NbtCompound nbt, String name) {
    	this.farms = JJUtils.readNBT(nbt, name + ".farms");
    }
    
    public void discover(ServerWorld world, BlockPos pos) {
    	Block block = world.getBlockState(pos).getBlock();
    	if (block.equals(Blocks.FARMLAND)) {
    		this.addBlocks(pos);
    		this.addSeroundingBlocks(world, pos);
    	}
    	else if (!isPossibleFarmBlock(world, pos)) {
    		this.removeBlock(pos);
    	}
    }
	protected void addSeroundingBlocks(ServerWorld world, BlockPos pos) {
		this.checkForPotentialFarmland(world, pos.east());
		this.checkForPotentialFarmland(world, pos.west());
		this.checkForPotentialFarmland(world, pos.north());
		this.checkForPotentialFarmland(world, pos.south());
	}	
	protected static boolean isPossibleFarmBlock(ServerWorld world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		BlockState above = world.getBlockState(pos.up());
		return (block.equals(Blocks.FARMLAND) ||
				block.equals(Blocks.GRASS_BLOCK) ||
				block.equals(Blocks.DIRT)) && 
			   (above.isAir() ||
			    above.getBlock() instanceof CropBlock);
	}
	protected void checkForPotentialFarmland(ServerWorld world, BlockPos pos) {
		if (isPossibleFarmBlock(world, pos)) {
			this.addBlocks(pos);
		}
	}
	protected void addBlocks(BlockPos pos) {
		if (!this.farms.contains(pos) && pos != null) {
			this.farms.add(pos);
			this.setChanged();
		}
	}
	protected void removeBlock(BlockPos pos) {
		if (this.farms.contains(pos)) {
			this.farms.remove(pos);
			this.setChanged();
		}
	}
	
	
	protected static boolean isFullyGrownCrop(BlockState state) {
		if (state.getBlock() instanceof CropBlock crop) {
			return crop.isMature(state);
		}
		return false;
	}
	protected static boolean hasNoCrops(BlockState state) {
		return state.isAir();
	}
	public BlockPos getRandomPos(ServerWorld world) {
		List<BlockPos> fullyGrown = new ArrayList<BlockPos>();
		List<BlockPos> empty	  = new ArrayList<BlockPos>();
		
		for (BlockPos pos : this.farms) {
			BlockState state = world.getBlockState(pos.up());
			if (isFullyGrownCrop(state)) {
				fullyGrown.add(pos);
			}
			else if (hasNoCrops(state) && isPossibleFarmBlock(world, pos)) {
				empty.add(pos);
			}
		}
		if (fullyGrown.size() != 0) { return getRandomListElement(fullyGrown); }
		if (empty.size()      != 0) { return getRandomListElement(empty);      }
		return null;
	}
}
