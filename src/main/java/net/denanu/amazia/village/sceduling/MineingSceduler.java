package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import net.denanu.amazia.block.AmaziaBlocks;
import net.denanu.amazia.block.custom.VillageMarkerBlock;
import net.denanu.amazia.highlighting.BlockHighlightingAmaziaConfig;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.structures.MineStructure;
import net.denanu.clientblockhighlighting.Highlighter;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class MineingSceduler extends VillageSceduler {
	private List<BlockPos> miningCores;
	private final Map<BlockPos, MineStructure> mines;

	public MineingSceduler(final Village _village) {
		super(_village);
		this.miningCores = new ArrayList<>();
		this.mines = new HashMap<>();
	}

	@Override
	public NbtCompound writeNbt() {
		final NbtCompound nbt = new NbtCompound();
		nbt.put("miningCores", NbtUtils.toNbt(this.miningCores));
		for (final Entry<BlockPos, MineStructure> element : this.mines.entrySet()) {
			nbt.put(element.getKey().toShortString(), element.getValue().writeNbt());
		}
		return nbt;
	}

	@Override
	public void readNbt(final NbtCompound nbt) {
		this.miningCores = NbtUtils.toBlockPosList(nbt.getList("miningCores", NbtElement.LIST_TYPE));
		for (final BlockPos pos : this.miningCores) {
			this.mines.put(pos, new MineStructure(pos, this.getVillage(), nbt.getCompound(pos.toShortString())));
		}
	}

	@Override
	public void initialize() {
		for (final Entry<BlockPos, MineStructure> pos : this.mines.entrySet()) {
			final Direction direction = this.getFacing(pos.getKey());
			pos.getValue().setDirection(direction);
		}
	}

	protected Direction getFacing(final BlockPos pos) {
		final BlockState state = this.getVillage().getWorld().getBlockState(pos);
		return state.get(VillageMarkerBlock.FACING);
	}

	private void addMine(final BlockPos pos) {
		final Direction direction = this.getFacing(pos);
		if (direction != null) {
			this.mines.put(pos, new MineStructure(pos, this.getVillage(), direction));
		}
	}

	@Override
	public void discover(final ServerWorld world, final BlockPos pos) {
		this.discoverMiningCore(world, pos);
	}

	private void discoverMiningCore(final ServerWorld world, final BlockPos pos) { // probably good to be more specific but i dont know the exact conditins yet
		final BlockState state = world.getBlockState(pos);
		if (state.isOf(AmaziaBlocks.MINE_MARKER)) {
			this.addMiningCore(world, pos);
		}
		else {
			this.removeMiningCore(world, pos);
		}
	}

	private void addMiningCore(final ServerWorld world, final BlockPos pos) {
		if (!this.miningCores.contains(pos)) {
			this.miningCores.add(pos);
			this.addMine(pos);
			VillageSceduler.markBlockAsFound(pos, world);
			this.setChanged();

			Highlighter.highlight(world, BlockHighlightingAmaziaConfig.MINEING, pos);
		}
	}
	private void removeMiningCore(final ServerWorld world, final BlockPos pos) {
		if (this.miningCores.contains(pos)) {
			this.miningCores.remove(pos);
			this.mines.get(pos).destroy();
			this.mines.remove(pos);
			VillageSceduler.markBlockAsLost(pos, world);
			this.setChanged();

			Highlighter.unhighlight(world, BlockHighlightingAmaziaConfig.MINEING, pos);
		}
	}

	@Nullable
	private MineStructure getRandomMine() {
		final BlockPos key = VillageSceduler.getRandomListElement(this.miningCores);
		if (key == null) { return null; }
		return this.mines.get(key);
	}

	@Nullable
	public MineStructure getSuggestedMine() {
		final MineStructure mine = this.getRandomMine();
		return mine != null && !mine.hasVillager() && !mine.getIsEnd() ? mine : null;
	}

	@Nullable
	public MineStructure getSugerstedRegenerationMine() {
		final MineStructure mine = this.getRandomMine();
		return mine != null && !mine.hasVillager() && mine.getIsEnd() && !mine.hasVillager() ? mine : null;
	}
}
