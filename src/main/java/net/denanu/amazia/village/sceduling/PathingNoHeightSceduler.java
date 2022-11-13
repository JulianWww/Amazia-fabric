package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.utils.NoHeightPathingData;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class PathingNoHeightSceduler extends VillageSceduler {
	private List<BlockPos> enchantingTables;
	private final HashMap<BlockPos, NoHeightPathingData> tables;
	private final Predicate<BlockState> tester;

	public PathingNoHeightSceduler(final Village _village, final Predicate<BlockState> tester) {
		super(_village);
		this.enchantingTables = new ArrayList<BlockPos>();
		this.tables = new HashMap<BlockPos, NoHeightPathingData>();
		this.tester = tester;
	}

	@Override
	public NbtCompound writeNbt() {
		final NbtCompound nbt = new NbtCompound();
		nbt.put("tables", NbtUtils.toNbt(this.tables.keySet()));
		return nbt;
	}

	@Override
	public void readNbt(final NbtCompound nbt) {
		this.enchantingTables = NbtUtils.toBlockPosList(nbt.getList("tables", NbtElement.LIST_TYPE));

	}

	@Override
	public void discover(final ServerWorld world, final BlockPos pos) {
		if (this.tester.test(world.getBlockState(pos))) {
			this.enchantingTables.add(pos);
			this.setChanged();
			this.createTable(pos);
			VillageSceduler.markBlockAsFound(pos, world);
		}
		else if(this.tables.remove(pos) != null) {
			this.enchantingTables.remove(pos);
			this.setChanged();
			VillageSceduler.markBlockAsLost(pos, world);
		}

	}

	@Override
	public void initialize() {
		if (this.enchantingTables != null) {
			for (final BlockPos pos : this.enchantingTables) {
				this.createTable(pos);
			}
		}
	}

	private void createTable(final BlockPos pos) {
		this.tables.put(pos, new NoHeightPathingData(pos, this.getVillage()));
	}

	public NoHeightPathingData getLocation() {
		if (this.tables.size() > 0) {
			return this.tables.get(JJUtils.getRandomListElement(this.enchantingTables));
		}
		return null;
	}
}
