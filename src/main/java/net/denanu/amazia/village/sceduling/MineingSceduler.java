package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.block.AmaziaBlocks;
import net.denanu.amazia.block.custom.MineMarkerBlock;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.structures.MineStructure;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class MineingSceduler extends VillageSceduler {
	private List<BlockPos> miningCores;
	private Map<BlockPos, MineStructure> mines;

	public MineingSceduler(Village _village) {
		super(_village);
		miningCores = new ArrayList<BlockPos>();
		mines = new HashMap<BlockPos, MineStructure>();
	}
	
	@Override
	public void writeNbt(NbtCompound nbt, String name) {
		JJUtils.writeNBT(nbt, this.miningCores, name + ".miningCores");
		for (Entry<BlockPos, MineStructure> element : this.mines.entrySet()) {
			element.getValue().writeNbt(nbt, name);
		}
	}

	@Override
	public void readNbt(NbtCompound nbt, String name) {
		this.miningCores = JJUtils.readNBT(nbt, name + ".miningCores");
		for (BlockPos pos : this.miningCores) {
			this.mines.put(pos, new MineStructure(pos, this.getVillage(), nbt, name));
		}
	}
	
	public void initialize() {
		for (Entry<BlockPos, MineStructure> pos : this.mines.entrySet()) {
			Direction direction = this.getFacing(pos.getKey());
			pos.getValue().setDirection(direction);
		}
	}
	
	protected Direction getFacing(BlockPos pos) {
    	BlockState state = this.getVillage().getPathingGraph().getWorld().getBlockState(pos);
    	return state.get(MineMarkerBlock.FACING);
    }
	
	private void addMine(BlockPos pos) {
		Direction direction = this.getFacing(pos);
		if (direction != null) {
			this.mines.put(pos, new MineStructure(pos, this.getVillage(), direction));
		}
	}

	@Override
	public void discover(ServerWorld world, BlockPos pos) {
		this.discoverMiningCore(world, pos);
	}
	
	private void discoverMiningCore(ServerWorld world, BlockPos pos) { // probably good to be more specific but i dont know the exact conditins yet
		BlockState state = world.getBlockState(pos);
		if (state.isOf(AmaziaBlocks.MINE_MARKER)) {
			this.addMiningCore(world, pos);
		}
		else {
			this.removeMiningCore(world, pos);
		}
	}
	
	private void addMiningCore(ServerWorld world, BlockPos pos) {
		if (!this.miningCores.contains(pos)) {
			this.miningCores.add(pos);
			this.addMine(pos);
			markBlockAsFound(pos, world);
			this.setChanged();
		}
	}
	private void removeMiningCore(ServerWorld world, BlockPos pos) {
		if (this.miningCores.contains(pos)) {
			this.miningCores.remove(pos);
			this.mines.get(pos).destroy();
			this.mines.remove(pos);
			markBlockAsLost(pos, world);
			this.setChanged();
		}
	}

	@Override
	public BlockPos getRandomPos(ServerWorld world, AmaziaVillagerEntity entity) {
		return null;
	}

	public MineStructure getSuggestedMine() {
		BlockPos key = getRandomListElement(this.miningCores);
		if (key == null) { return null; }
		MineStructure mine = this.mines.get(key);
		return mine != null && !mine.hasVillager() && !mine.getIsEnd() ? mine : null;
	}
}
