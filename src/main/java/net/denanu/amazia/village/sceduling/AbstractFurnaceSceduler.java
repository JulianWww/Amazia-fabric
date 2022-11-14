package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.components.AmaziaBlockComponents;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.utils.DoubleDownPathingData;
import net.minecraft.block.Block;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AbstractFurnaceSceduler extends FacingPathingVillageSceduler {
	public List<BlockPos> location;
	public List<BlockPos> available;
	public Map<BlockPos, DoubleDownPathingData> pathing;
	private final Class<? extends Block> toFind;

	public AbstractFurnaceSceduler(final Village _village, final Class<? extends Block> toFind) {
		super(_village);
		this.location = new ArrayList<BlockPos>();
		this.available = new ArrayList<BlockPos>();
		this.pathing = new HashMap<BlockPos, DoubleDownPathingData>();
		this.toFind = toFind;
	}

	@Override
	public NbtCompound writeNbt() {
		final NbtCompound nbt = new NbtCompound();
		nbt.put("location",  NbtUtils.toNbt(this.location));
		nbt.put("available", NbtUtils.toNbt(this.available));
		return nbt;
	}
	@Override
	public void readNbt(final NbtCompound nbt) {
		this.location  = NbtUtils.toBlockPosList(nbt.getList("location",  NbtElement.LIST_TYPE));
		this.available = NbtUtils.toBlockPosList(nbt.getList("available", NbtElement.LIST_TYPE));
		return;
	}

	@Override
	public void discover(final ServerWorld world, final BlockPos pos) {
		if (world.getBlockState(pos).getBlock().getClass() == this.toFind && !this.location.contains(pos)) {
			this.location.add(pos);
			this.setChanged();
			VillageSceduler.markBlockAsFound(pos, world);

			final BlockEntity entity = world.getBlockEntity(pos);
			AmaziaBlockComponents.addVillage(entity, this.getVillage());
			if (entity instanceof final AbstractFurnaceBlockEntity furnace) {
				if (furnace.getStack(0).isEmpty()) {
					this.available.add(pos);
				}
				this.addPathingOption(pos);
			}
		} else if (this.location.remove(pos)) {
			this.available.remove(pos);
			VillageSceduler.markBlockAsLost(pos, world);
			this.setChanged();
		}
	}

	@Override
	protected void addPathingOption(final BlockPos pos, final Direction facing) {
		this.pathing.put(pos, new DoubleDownPathingData(pos, this.getVillage(), facing));
	}

	@Override
	public void initialize() {
		this.fromBlockPosList(this.location);
	}

	public void makeAvailableFurnace(final BlockPos pos) {
		if (!this.available.contains(pos)) {
			this.available.add(pos);
		}
	}

	public void removeAvailableFurnace(final BlockPos pos) {
		this.available.remove(pos);
	}

	public DoubleDownPathingData getLocation() {
		if (this.pathing.size() > 0) {
			return this.pathing.get(JJUtils.getRandomListElement(this.available));
		}
		return null;
	}
}
