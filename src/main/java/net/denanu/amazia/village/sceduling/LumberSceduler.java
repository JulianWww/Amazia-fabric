package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.block.AmaziaBlocks;
import net.denanu.amazia.highlighting.BlockHighlightingAmaziaConfig;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.utils.LumberPathingData;
import net.denanu.blockhighlighting.Highlighter;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;

public class LumberSceduler extends VillageSceduler {
	private final HashMap<BlockPos, LumberPathingData> schools;
	private List<BlockPos> tmp;
	private List<BlockPos> emptys;
	private List<BlockPos> filled;

	public LumberSceduler(final Village _village) {
		super(_village);
		this.schools = new HashMap<>();
		this.emptys  = new ArrayList<>();
		this.filled =  new ArrayList<>();
		this.tmp = null;
	}

	@Override
	public NbtCompound writeNbt() {
		final NbtCompound nbt = new NbtCompound();
		nbt.put("blockSetups", NbtUtils.toNbt(this.toNbtList()));
		nbt.put("emptys", NbtUtils.toNbt(this.emptys));
		nbt.put("filled", NbtUtils.toNbt(this.filled));
		return nbt;
	}

	@Override
	public void readNbt(final NbtCompound nbt) {
		this.tmp = NbtUtils.toBlockPosList(nbt.getList("blockSetups", NbtElement.LIST_TYPE));
		this.emptys = NbtUtils.toBlockPosList(nbt.getList("emptys", NbtElement.LIST_TYPE));
		this.filled = NbtUtils.toBlockPosList(nbt.getList("filled", NbtElement.LIST_TYPE));
	}

	private List<BlockPos> toNbtList() {
		return new ArrayList<>(this.schools.keySet());
	}

	private void fromNbtList(final List<BlockPos> poses) {
		for (final BlockPos pos:  poses) { this.createFarm(pos); }
	}

	public static boolean isSchool(final ServerWorld world, final BlockPos pos) {
		return world.getBlockState(pos).isOf(AmaziaBlocks.TREE_FARM_MARKER);
	}

	public static boolean isEmpty(final ServerWorld world, final BlockPos pos) {
		return world.isAir(pos);
	}

	public static boolean isLog(final ServerWorld world, final BlockPos pos) {
		return world.getBlockState(pos).streamTags().anyMatch(tag -> tag == BlockTags.LOGS);
	}

	@Override
	public void discover(final ServerWorld world, final BlockPos pos) {
		this.discoverSchool(world, pos);
		this.discoverEmptysAndFilled(world, pos);
	}

	private void discoverEmptysAndFilled(final ServerWorld world, final BlockPos pos) {
		if (LumberSceduler.isSchool(world, pos.down())) {
			if (LumberSceduler.isEmpty(world, pos)) {
				this.addEmptySchool(world, pos.down());
			}
			else {
				this.removeEmptySchool(world, pos.down());
			}
			if (LumberSceduler.isLog(world, pos)) {
				this.addFilledSchool(world, pos.down());
			}
			else {
				this.removeFilledSchool(world, pos.down());
			}
		}
	}

	private void discoverSchool(final ServerWorld world, final BlockPos pos) {
		if (LumberSceduler.isSchool(world, pos)) {
			this.addSchool(world, pos);
			if (LumberSceduler.isEmpty(world, pos.up())) {
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

	private void createFarm(final BlockPos pos) {
		this.schools.put(pos, new LumberPathingData(pos, this.getVillage()));
	}

	private void addSchool(final ServerWorld world, final BlockPos pos) {
		if (!this.schools.containsKey(pos)) {
			this.createFarm(pos);
			VillageSceduler.markBlockAsFound(pos, world);
			this.setChanged();
		}
	}

	private void removeSchool(final ServerWorld world, final BlockPos pos) {
		if (this.schools.containsKey(pos)) {
			this.schools.get(pos).destroy(this.getVillage());
			this.schools.remove(pos);

			this.filled.remove(pos);
			this.emptys.remove(pos);

			VillageSceduler.markBlockAsLost(pos, world);
			this.setChanged();

			Highlighter.unhighlight(world, BlockHighlightingAmaziaConfig.LUMBERJACK_FULL,	pos);
			Highlighter.unhighlight(world, BlockHighlightingAmaziaConfig.LUMBERJACK_EMPTY,	pos);
		}
	}

	private void addEmptySchool(final ServerWorld world, final BlockPos pos) {
		if (!this.emptys.contains(pos)) {
			this.emptys.add(pos);
			VillageSceduler.markBlockAsFound(pos, world);
			this.setChanged();

			Highlighter.highlight(world, BlockHighlightingAmaziaConfig.LUMBERJACK_EMPTY, pos);
		}
	}

	private void removeEmptySchool(final ServerWorld world, final BlockPos pos) {
		if (this.emptys.contains(pos)) {
			this.emptys.remove(pos);
			VillageSceduler.markBlockAsLost(pos, world);
			this.setChanged();

			Highlighter.unhighlight(world, BlockHighlightingAmaziaConfig.LUMBERJACK_EMPTY, pos);
		}
	}

	private void addFilledSchool(final ServerWorld world, final BlockPos pos) {
		if (!this.filled.contains(pos)) {
			this.filled.add(pos);
			VillageSceduler.markBlockAsFound(pos, world);
			this.setChanged();

			Highlighter.highlight(world, BlockHighlightingAmaziaConfig.LUMBERJACK_FULL, pos);
		}
	}

	private void removeFilledSchool(final ServerWorld world, final BlockPos pos) {
		if (this.filled.contains(pos)) {
			this.filled.remove(pos);
			VillageSceduler.markBlockAsLost(pos, world);
			this.setChanged();

			Highlighter.unhighlight(world, BlockHighlightingAmaziaConfig.LUMBERJACK_FULL, pos);
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
			//this.setChanged();
			this.tmp = null;
		}
	}

	public LumberPathingData getHarvestLocation() {
		if (this.filled.size() > 0) {
			return this.schools.get(JJUtils.getRandomListElement(this.filled));
		}
		return null;
	}
}
