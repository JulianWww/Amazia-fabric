package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.List;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.highlighting.BlockHighlightingAmaziaIds;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.denanu.blockhighlighting.Highlighter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class FarmingSceduler extends VillageSceduler {
	public List<BlockPos> possibleFarms;
	public List<BlockPos> crops;
	public List<BlockPos> growing;
	public List<BlockPos> emptyFarm;

	public FarmingSceduler(final Village _village) {
		super(_village);
		this.possibleFarms 	= new ArrayList<>();
		this.crops 			= new ArrayList<>();
		this.emptyFarm 		= new ArrayList<>();
		this.growing 		= new ArrayList<>();
	}

	@Override
	public NbtCompound writeNbt() {
		final NbtCompound nbt = new NbtCompound();
		nbt.put("possibleFarms", NbtUtils.toNbt(this.possibleFarms));
		nbt.put("crops", NbtUtils.toNbt(this.crops));
		nbt.put("emptyFarm", NbtUtils.toNbt(this.emptyFarm));
		nbt.put("growing", NbtUtils.toNbt(this.growing));
		return nbt;
	}
	@Override
	public void readNbt(final NbtCompound nbt) {
		this.possibleFarms = NbtUtils.toBlockPosList(nbt.getList("possibleFarms", NbtElement.LIST_TYPE));
		this.crops = NbtUtils.toBlockPosList(nbt.getList("crops", NbtElement.LIST_TYPE));
		this.emptyFarm = NbtUtils.toBlockPosList(nbt.getList("emptyFarm", NbtElement.LIST_TYPE));
		this.growing = NbtUtils.toBlockPosList(nbt.getList("growing", NbtElement.LIST_TYPE));
	}

	@Override
	public void initialize() {
	}

	protected static boolean isFarmLand(final ServerWorld world, final BlockPos pos) {
		return world.getBlockState(pos).getBlock().equals(Blocks.FARMLAND);
	}
	protected static boolean isPotentialFarmLand(final ServerWorld world, final BlockPos pos) {
		final Block block = world.getBlockState(pos).getBlock();
		return (block.equals(Blocks.DIRT) || block.equals(Blocks.GRASS_BLOCK)) && FarmingSceduler.isAjacantToFarmBlock(world, pos);
	}
	protected static boolean isAjacantToFarmBlock(final ServerWorld world, final BlockPos pos) {
		return FarmingSceduler.isFarmLand(world, pos.east()) ||
				FarmingSceduler.isFarmLand(world, pos.west()) ||
				FarmingSceduler.isFarmLand(world, pos.north()) ||
				FarmingSceduler.isFarmLand(world, pos.south());
	}

	@Override
	public void discover(final ServerWorld world, final BlockPos pos) {
		this.discoverFarmland(world, pos, true);
		this.discoverCrops(world, pos);
		this.discoverGrowing(world, pos);
		this.discoverEmptyFarmland(world, pos);
		this.discoverEmptyFarmland(world, pos.down());
	}

	private void discoverCrops(final ServerWorld world, final BlockPos pos) {
		final BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof final CropBlock crop && crop.isMature(state)) {
			this.addCrop(world, pos);
			return;
		}
		if (state.getBlock() instanceof final SugarCaneBlock cane && world.getBlockState(pos.down()).isOf(cane) && world.getBlockState(pos.down(2)).isOf(cane)) {
			this.addCrop(world, pos.down(2));
		}
		this.removeCrop(world, pos);
		this.removeCrop(world, pos.down());
	}

	private void discoverEmptyFarmland(final ServerWorld world, final BlockPos pos) {
		if (FarmingSceduler.isFarmLand(world, pos) && world.isAir(pos.up())) {
			this.addEmptyFarmland(world, pos);
		}
		else {
			this.removeEmptyFarmland(world, pos);
		}
	}

	private void discoverGrowing(final ServerWorld world, final BlockPos pos) {
		final BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof final CropBlock crop && !crop.isMature(state)) {
			this.addGrowing(world, pos);
		} else {
			this.removeGrowing(world, pos);
		}
	}

	protected void discoverFarmland(final ServerWorld world, final BlockPos pos, final boolean propagate) {
		if (FarmingSceduler.isFarmLand(world, pos) || !FarmingSceduler.isPotentialFarmLand(world, pos)) {
			this.removePossibleFarmLand(world, pos);
			if (propagate) {
				this.discoverAjacantFarmland(world, pos);
			}
		} else {
			this.addPossibleFarmLand(world, pos);
		}
	}
	private void discoverAjacantFarmland(final ServerWorld world, final BlockPos pos) {
		this.discoverFarmland(world, pos.east(), false);
		this.discoverFarmland(world, pos.west(), false);
		this.discoverFarmland(world, pos.north(), false);
		this.discoverFarmland(world, pos.south(), false);
	}

	private void addPossibleFarmLand(final ServerWorld world, final BlockPos pos) {
		if (!this.possibleFarms.contains(pos)) {
			VillageSceduler.markBlockAsFound(pos, world);
			this.possibleFarms.add(pos);
			this.setChanged();

			Highlighter.highlight(world, BlockHighlightingAmaziaIds.FARMING_POSSIBLE_FARMS, pos);
		}
	}
	private void removePossibleFarmLand(final ServerWorld world, final BlockPos pos) {
		if (this.possibleFarms.contains(pos)) {
			VillageSceduler.markBlockAsLost(pos, world);
			this.possibleFarms.remove(pos);
			this.setChanged();

			Highlighter.unhighlight(world, BlockHighlightingAmaziaIds.FARMING_POSSIBLE_FARMS, pos);
		}
	}
	private void addCrop(final ServerWorld world, final BlockPos pos) {
		if (!this.crops.contains(pos)) {
			VillageSceduler.markBlockAsFound(pos, world);
			this.crops.add(pos);
			this.setChanged();

			Highlighter.highlight(world, BlockHighlightingAmaziaIds.FARMING_HARVISTABLE_FARMS, pos);
		}
	}
	private void removeCrop(final ServerWorld world, final BlockPos pos) {
		if (this.crops.contains(pos)) {
			VillageSceduler.markBlockAsLost(pos, world);
			this.crops.remove(pos);
			this.setChanged();

			Highlighter.unhighlight(world, BlockHighlightingAmaziaIds.FARMING_HARVISTABLE_FARMS, pos);
		}
	}
	private void addEmptyFarmland(final ServerWorld world, final BlockPos pos) {
		if (!this.emptyFarm.contains(pos)) {
			VillageSceduler.markBlockAsFound(pos, world);
			this.emptyFarm.add(pos);
			this.setChanged();

			Highlighter.highlight(world, BlockHighlightingAmaziaIds.FARMING_EMPTY_FARMS, pos);
		}
	}
	private void removeEmptyFarmland(final ServerWorld world, final BlockPos pos) {
		if (this.emptyFarm.contains(pos)) {
			VillageSceduler.markBlockAsLost(pos, world);
			this.emptyFarm.remove(pos);
			this.setChanged();

			Highlighter.unhighlight(world, BlockHighlightingAmaziaIds.FARMING_EMPTY_FARMS, pos);
		}
	}
	private void addGrowing(final ServerWorld world, final BlockPos pos) {
		if (!this.growing.contains(pos)) {
			VillageSceduler.markBlockAsFound(pos, world);
			this.growing.add(pos);
			this.setChanged();

			Highlighter.highlight(world, BlockHighlightingAmaziaIds.FARMING_GOWING_FARMS, pos);
		}
	}
	private void removeGrowing(final ServerWorld world, final BlockPos pos) {
		if (this.growing.contains(pos)) {
			VillageSceduler.markBlockAsLost(pos, world);
			this.growing.remove(pos);
			this.setChanged();

			Highlighter.unhighlight(world, BlockHighlightingAmaziaIds.FARMING_GOWING_FARMS, pos);
		}
	}

	public BlockPos getRandomPos(final ServerWorld world, final AmaziaVillagerEntity entity) {
		if (this.crops.size() > 0 && entity.canHarvest()) {
			return VillageSceduler.getRandomListElement(this.crops).down();
		}
		if (this.emptyFarm.size() > 0 && entity.canPlant()) {
			return VillageSceduler.getRandomListElement(this.emptyFarm);
		}
		if (this.possibleFarms.size() > 0 && entity.canHoe()) {
			return VillageSceduler.getRandomListElement(this.possibleFarms);
		}
		return null;
	}

	public boolean isHoable(final Vec3d loc) {
		final BlockPos pos = new BlockPos(loc);
		return this.possibleFarms.contains(pos.down());
	}

	public static boolean isPlantable(final LivingEntity entity) {
		final BlockPos pos = new BlockPos(entity.getPos());
		return entity.getWorld().getBlockState(pos).getBlock().equals(Blocks.FARMLAND) &&
				entity.getWorld().isAir(pos.up());
	}

	public static boolean isHarvistable(final LivingEntity entity) {
		final BlockState state = entity.getWorld().getBlockState(new BlockPos(entity.getPos()).up());
		if (state.getBlock() instanceof final CropBlock crop) {
			return crop.isMature(state);
		}
		if (state.getBlock() instanceof final SugarCaneBlock crop) {
			return true;
		}
		return false;
	}

	public BlockPos getGrowing() {
		return JJUtils.getRandomListElement(this.growing);
	}
}
