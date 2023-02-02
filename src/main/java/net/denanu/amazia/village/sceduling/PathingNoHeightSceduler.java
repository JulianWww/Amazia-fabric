package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.utils.NoHeightPathingData;
import net.denanu.blockhighlighting.Highlighter;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class PathingNoHeightSceduler extends VillageSceduler {
	private List<BlockPos> enchantingTables;
	private final HashMap<BlockPos, NoHeightPathingData> tables;
	private final Predicate<BlockState> tester;
	private final Identifier id;

	public PathingNoHeightSceduler(final Village _village, final Predicate<BlockState> tester, final Identifier id) {
		super(_village);
		this.enchantingTables = new ArrayList<>();
		this.tables = new HashMap<>();
		this.tester = tester;
		this.id = id;
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
			Highlighter.highlight(world, this.id, pos);
		}
		else if(this.tables.remove(pos) != null) {
			this.enchantingTables.remove(pos);
			this.setChanged();
			VillageSceduler.markBlockAsLost(pos, world);
			Highlighter.unhighlight(world, this.id, pos);
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

	@Nullable
	public NoHeightPathingData getLocation() {
		if (this.tables.size() > 0) {
			return this.tables.get(JJUtils.getRandomListElement(this.enchantingTables));
		}
		return null;
	}

	@Nullable
	public NoHeightPathingData getLocation(final Predicate<BlockState> pred) {
		if (this.tables.size() > 0) {
			return this.tables.get(JJUtils.getRandomListElement(this.enchantingTables, pred, this.getVillage().getWorld()));
		}
		return null;
	}
}
