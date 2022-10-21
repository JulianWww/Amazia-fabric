package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.block.AmaziaBlocks;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.utils.LumberPathingData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LumberSceduler extends VillageSceduler {
	private HashMap<BlockPos, LumberPathingData> schools;
	private List<BlockPos> tmp;
	private List<BlockPos> emptys;
	private List<BlockPos> filled;

	public LumberSceduler(Village _village) {
		super(_village);
		this.schools = new HashMap<BlockPos, LumberPathingData>();
		this.emptys  = new ArrayList<BlockPos>();
		this.filled =  new ArrayList<BlockPos>();
		this.tmp = null;
	}

	@Override
	public void writeNbt(NbtCompound nbt, String name) {
		JJUtils.writeNBT(nbt, this.toNbtList(), name + ".blockSetups");
		JJUtils.writeNBT(nbt, this.emptys, name + ".emptys");
		JJUtils.writeNBT(nbt, this.filled, name + ".filled");
	}

	@Override
	public void readNbt(NbtCompound nbt, String name) {
		this.tmp = JJUtils.readNBT(nbt, name + ".blockSetups");
		this.emptys = JJUtils.readNBT(nbt, name + ".emptys");
		this.filled = JJUtils.readNBT(nbt, name + ".filled");
	}
	
	private List<BlockPos> toNbtList() {
		return new ArrayList<BlockPos>(this.schools.keySet());
	}
	
	private void fromNbtList(List<BlockPos> poses) {
		for (BlockPos pos:  poses) { this.createFarm(pos); }
	}
	
	public static boolean isSchool(ServerWorld world, BlockPos pos) {
		return world.getBlockState(pos).isOf(AmaziaBlocks.TREE_FARM_MARKER);
	}
	
	public static boolean isEmpty(ServerWorld world, BlockPos pos) {
		return world.isAir(pos);
	}
	
	public static boolean isLog(ServerWorld world, BlockPos pos) {
		return world.getBlockState(pos).streamTags().anyMatch(tag -> tag == BlockTags.LOGS);
	}

	@Override
	public void discover(ServerWorld world, BlockPos pos) {
		this.discoverSchool(world, pos);
		this.discoverEmptysAndFilled(world, pos);
	}

	private void discoverEmptysAndFilled(ServerWorld world, BlockPos pos) {
		if (isSchool(world, pos.down())) {
			if (isEmpty(world, pos)) {
				this.addEmptySchool(world, pos.down());
			}
			else {
				this.removeEmptySchool(world, pos.down());
			}
			if (isLog(world, pos)) {
				this.addFilledSchool(world, pos.down());
			}
			else {
				this.removeFilledSchool(world, pos.down());
			}
		}
	}

	private void discoverSchool(ServerWorld world, BlockPos pos) {
		if (isSchool(world, pos)) {
			this.addSchool(world, pos);
			if (isEmpty(world, pos.up())) {
				this.addEmptySchool(world, pos);
			}
			else {
				this.removeEmptySchool(world, pos);
			}
		}
		else {
			this.removeEmptySchool(world, pos);
			this.removeSchool(world, pos);
		}
	}

	private void createFarm(BlockPos pos) {
		this.schools.put(pos, new LumberPathingData(pos, this.getVillage()));
	}
	
	private void addSchool(ServerWorld world, BlockPos pos) {
		if (!this.schools.containsKey(pos)) {
			this.createFarm(pos);
			markBlockAsFound(pos, world);
			this.setChanged();
		}
	}
	
	private void removeSchool(ServerWorld world, BlockPos pos) {
		if (this.schools.containsKey(pos)) {
			this.schools.get(pos).destroy(getVillage());
			this.schools.remove(pos);
			markBlockAsLost(pos, world);
			this.setChanged();
		}
	}
	
	private void addEmptySchool(ServerWorld world, BlockPos pos) {
		if (!this.emptys.contains(pos)) {
			this.emptys.add(pos);
			markBlockAsFound(pos, world);
			this.setChanged();
		}
	}
	
	private void removeEmptySchool(ServerWorld world, BlockPos pos) {
		if (this.emptys.contains(pos)) {
			this.emptys.remove(pos);
			markBlockAsLost(pos, world);
			this.setChanged();
		}
	}
	
	private void addFilledSchool(ServerWorld world, BlockPos pos) {
		if (!this.filled.contains(pos)) {
			this.filled.add(pos);
			markBlockAsFound(pos, world);
			this.setChanged();
		}
	}
	
	private void removeFilledSchool(ServerWorld world, BlockPos pos) {
		if (this.filled.contains(pos)) {
			this.filled.remove(pos);
			markBlockAsLost(pos, world);
			this.setChanged();
		}
	}
	
	public LumberPathingData getPlantLocation() {
		if (this.emptys.size() > 0) {
			return this.schools.get(JJUtils.getRandomListElement(this.emptys));
		}
		return null;
	}

	@Override
	public void initialize() {
		if (this.tmp != null) {
			this.fromNbtList(this.tmp);
			this.setChanged();
			this.tmp = null;
		}
	}
	@Override
	public BlockPos getRandomPos(ServerWorld world, AmaziaVillagerEntity entity) {
		return null;
	}

	public LumberPathingData getHarvestLocation() {
		if (this.filled.size() > 0) {
			return this.schools.get(JJUtils.getRandomListElement(this.filled));
		}
		return null;
	}
}
