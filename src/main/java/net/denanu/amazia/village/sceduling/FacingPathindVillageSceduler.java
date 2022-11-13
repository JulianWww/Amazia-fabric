package net.denanu.amazia.village.sceduling;

import java.util.List;

import net.denanu.amazia.village.Village;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class FacingPathindVillageSceduler extends VillageSceduler {

	public FacingPathindVillageSceduler(Village _village) {
		super(_village);
	}

	protected Direction getFacing(BlockPos pos) {
		BlockState state = this.getVillage().getWorld().getBlockState(pos);
		if (state.getBlock() instanceof ChestBlock) {
			return ChestBlock.getFacing(state);
		}
		if (state.getBlock() instanceof BarrelBlock) {
			return state.get(BarrelBlock.FACING);
		}
		return null;
	}

	protected void fromBlockPosList(List<BlockPos> positions) {
		for (BlockPos pos: positions) {
			Direction facing = this.getFacing(pos);
			if (facing != null) {
				this.addPathingOption(pos, facing);
			}
		}
	}

	protected abstract void addPathingOption(BlockPos pos, Direction facing);
}
