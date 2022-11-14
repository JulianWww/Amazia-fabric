package net.denanu.amazia.village.sceduling;

import java.util.List;

import net.denanu.amazia.village.Village;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class FacingPathingVillageSceduler extends VillageSceduler {

	public FacingPathingVillageSceduler(final Village _village) {
		super(_village);
	}

	protected Direction getFacing(final BlockPos pos) {
		final BlockState state = this.getVillage().getWorld().getBlockState(pos);
		if (state.getBlock() instanceof ChestBlock) {
			return ChestBlock.getFacing(state);
		}
		if (state.getBlock() instanceof BarrelBlock) {
			return state.get(BarrelBlock.FACING);
		}
		if (state.getBlock() instanceof AbstractFurnaceBlock) {
			return state.get(AbstractFurnaceBlock.FACING);
		}
		return null;
	}

	protected void fromBlockPosList(final List<BlockPos> positions) {
		for (final BlockPos pos: positions) {
			this.addPathingOption(pos);
		}
	}

	protected abstract void addPathingOption(BlockPos pos, Direction facing);

	protected void addPathingOption(final BlockPos pos) {
		final Direction facing = this.getFacing(pos);
		if (facing != null) {
			this.addPathingOption(pos, facing);
		}
	}
}
