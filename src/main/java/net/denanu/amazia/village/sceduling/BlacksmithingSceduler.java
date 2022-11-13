package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.List;

import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class BlacksmithingSceduler extends VillageSceduler {
	private List<BlockPos> furnaces;
	private List<BlockPos> availableFurnaces;
	private List<BlockPos> anvils;

	public BlacksmithingSceduler(Village _village) {
		super(_village);
		this.furnaces = new ArrayList<BlockPos>();
		this.availableFurnaces = new ArrayList<BlockPos>();
		this.anvils = new ArrayList<BlockPos>();
	}

	@Override
	public NbtCompound writeNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.put("furnaces", 			NbtUtils.toNbt(this.furnaces));
		nbt.put("availableFurnaces",	NbtUtils.toNbt(this.availableFurnaces));
		nbt.put("anvils",				NbtUtils.toNbt(this.anvils));
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.furnaces 			= NbtUtils.toBlockPosList(nbt.getList("furnaces", 			NbtElement.LIST_TYPE));
		this.availableFurnaces 	= NbtUtils.toBlockPosList(nbt.getList("availableFurnaces", 	NbtElement.LIST_TYPE));
		this.anvils 			= NbtUtils.toBlockPosList(nbt.getList("anvils", 			NbtElement.LIST_TYPE));
	}

	@Override
	public void discover(ServerWorld world, BlockPos pos) {
		this.discoverAnvil(world, pos);
		this.discoverFurnace(world, pos);
	}

	private void discoverFurnace(ServerWorld world, BlockPos pos) {
		if (world.getBlockState(pos).isOf(Blocks.BLAST_FURNACE)) {
			this.furnaces.add(pos);
			this.setChanged();
			VillageSceduler.markBlockAsFound(pos, world);
			
			BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof BlastFurnaceBlockEntity furnace) {
				if (furnace.getStack(0).isEmpty()) {
					this.availableFurnaces.add(pos);
				}
			}
		}
		else {
			if (this.furnaces.remove(pos)) {
				this.availableFurnaces.remove(pos);
				this.setChanged();
				VillageSceduler.markBlockAsLost(pos, world);
			}
		}
	}

	private void discoverAnvil(ServerWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.isOf(Blocks.ANVIL) || state.isOf(Blocks.CHIPPED_ANVIL) || state.isOf(Blocks.DAMAGED_ANVIL)) {
			this.anvils.add(pos);
			this.setChanged();
			VillageSceduler.markBlockAsFound(pos, world);
		}
		else {
			if (this.anvils.remove(pos)) {
				this.setChanged();
				VillageSceduler.markBlockAsLost(pos, world);
			}
		}
	}

	@Override
	public void initialize() {
	}

}
