package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.components.AmaziaBlockComponents;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.sceduling.utils.DoubleDownPathingData;
import net.denanu.blockhighlighting.Highlighter;
import net.minecraft.block.Block;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AbstractFurnaceSceduler extends FacingPathingVillageSceduler {
	public List<BlockPos> location;
	public List<BlockPos> available;
	public Map<BlockPos, DoubleDownPathingData> pathing;
	private final Class<? extends Block> toFind;
	private final Identifier highlighter;

	public AbstractFurnaceSceduler(final Village _village, final Class<? extends Block> toFind, final Identifier highlighter) {
		super(_village);
		this.location = new ArrayList<>();
		this.available = new ArrayList<>();
		this.pathing = new HashMap<>();
		this.toFind = toFind;
		this.highlighter = highlighter;
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
	}

	@Override
	public void discover(final ServerWorld world, final BlockPos pos) {
		if (world.getBlockState(pos).getBlock().getClass() == this.toFind) {
			if (!this.location.contains(pos)) {
				this.location.add(pos);
				this.setChanged();
				VillageSceduler.markBlockAsFound(pos, world);

				Highlighter.highlight(world, this.highlighter, pos);

				final BlockEntity entity = world.getBlockEntity(pos);
				AmaziaBlockComponents.addVillage(entity, this.getVillage());
				if (entity instanceof final AbstractFurnaceBlockEntity furnace) {
					if (furnace.getStack(0).isEmpty()) {
						this.available.add(pos);
					}
					this.addPathingOption(pos);
				}
			}
		} else if (this.location.remove(pos)) {
			this.available.remove(pos);
			this.pathing.remove(pos).destroy(this.getVillage());
			final BlockEntity entity = world.getBlockEntity(pos);
			AmaziaBlockComponents.removeVillage(entity, this.getVillage());
			VillageSceduler.markBlockAsLost(pos, world);
			this.setChanged();

			Highlighter.unhighlight(world, this.highlighter, pos);
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

	@Nullable
	public DoubleDownPathingData getLocation() {
		if (!this.pathing.isEmpty()) {
			return this.pathing.get(JJUtils.getRandomListElement(this.available));
		}
		return null;
	}

	@Nullable
	public DoubleDownPathingData getKitchenLocation() {
		if (!this.location.isEmpty()) {
			return this.pathing.get(JJUtils.getRandomListElement(this.location));
		}
		return null;
	}

	public Collection<DoubleDownPathingData> getAccessPoints() {
		return this.pathing.values();
	}
}
