package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.utils.NoHeightPathingData;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PathingNoHeightSceduler extends VillageSceduler {
	private List<BlockPos> enchantingTables;
	private HashMap<BlockPos, NoHeightPathingData> tables;
	
	public PathingNoHeightSceduler(Village _village) {
		super(_village);
		this.enchantingTables = new ArrayList<BlockPos>();
		this.tables = new HashMap<BlockPos, NoHeightPathingData>();
	}

	@Override
	public NbtCompound writeNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.put("tables", NbtUtils.toNbt(this.tables.keySet()));
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.enchantingTables = NbtUtils.toBlockPosList(nbt.getList("tables", NbtList.LIST_TYPE));
		
	}

	@Override
	public void discover(ServerWorld world, BlockPos pos) {
		if (world.getBlockState(pos).isOf(Blocks.ENCHANTING_TABLE)) {
			this.enchantingTables.add(pos);
			this.setChanged();
			this.createTable(pos);
			markBlockAsFound(pos, world);
		}
		else if(this.tables.remove(pos) != null) {
			this.enchantingTables.remove(pos);
			this.setChanged();
			markBlockAsLost(pos, world);
		}
		
	}

	@Override
	public void initialize() {
		if (this.enchantingTables != null) {
			for (BlockPos pos : this.enchantingTables) {
				this.createTable(pos);
			}
		}
	}
	
	private void createTable(BlockPos pos) {
		this.tables.put(pos, new NoHeightPathingData(pos, this.getVillage()));
	}
	
	public NoHeightPathingData getTableLocation() {
		if (this.tables.size() > 0) {
			return this.tables.get(JJUtils.getRandomListElement(this.enchantingTables));
		}
		return null;
	}
}
