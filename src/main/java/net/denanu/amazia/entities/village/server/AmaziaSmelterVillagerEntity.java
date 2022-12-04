package net.denanu.amazia.entities.village.server;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oshi.util.tuples.Triplet;

public abstract class AmaziaSmelterVillagerEntity extends AmaziaVillagerEntity {
	public static final ImmutableSet<Item> USABLE_ITEMS = ImmutableSet.of();
	public static final ImmutableMap<Item, Integer> REQUIRED_ITEMS = ImmutableMap.of(Items.COAL, 64);


	private Optional<Integer> blastingItem;
	private int amountOfCoal;
	@Nullable
	private BlockPos targetPos;
	public boolean shouldDeposit;
	@Nullable
	private BlockPos targetCraftPos;

	protected AmaziaSmelterVillagerEntity(final EntityType<? extends PassiveEntity> entityType, final World world) {
		super(entityType, world);
		this.blastingItem = Optional.empty();
		this.shouldDeposit = false;
	}

	@Override
	public void writeCustomDataToNbt(final NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		if (this.blastingItem.isPresent()) {
			nbt.putInt("smeltItem", this.blastingItem.get());
		}
	}

	@Override
	public void readCustomDataFromNbt(final NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.blastingItem = Optional.ofNullable(nbt.getInt("smeltItem"));
		this.scanForCoal();
	}

	@Override
	public boolean canDepositItems() {
		return !this.hasFreeSlot() || this.shouldDeposit;
	}

	public void sceduleDepositing() {
		this.shouldDeposit = true;
	}

	public boolean canSmelt() {
		return this.blastingItem.isPresent();
	}

	public boolean canOrFindSmelting() {
		if (!this.canSmelt()) {
			this.findSmeltingItem();
		}
		return this.canSmelt();
	}

	public Optional<Integer> getSmeltingItem() {
		return this.blastingItem;
	}

	public abstract void findSmeltingItem();
	protected void findSmeltingItem(final Collection<Item> itmes) {
		this.blastingItem = Optional.empty();
		for (int i = 0; i<this.getInventory().size(); i++) {
			if (itmes.contains(this.getInventory().getStack(i).getItem())) {
				this.blastingItem = Optional.of(i);
				return;
			}
		}
	}

	public abstract void requestSmeltable();

	public void setTargetPos(final BlockPos pos) {
		this.targetPos = pos;
	}

	public BlockPos getTargetPos() {
		return this.targetPos;
	}

	public boolean hasCoal() {
		return this.amountOfCoal > 3;
	}

	public boolean requestCoalOrCanRun() {
		if (!this.hasCoal() && this.age % 200 == 0) {
			this.requestCoal();
		}
		return this.hasCoal();
	}

	public void requestCoal() {
		this.requestItem(Items.COAL);
	}

	public void scanForCoal() {
		this.amountOfCoal = Math.max(this.countItems(Items.COAL), this.countItems(Items.CHARCOAL));
		this.findSmeltingItem();
		this.shouldDeposit = false;
	}

	@Override
	public Triplet<ItemStack, Integer, Integer> getDepositableItems() {
		return this.getDepositableItems(BlacksmithEntity.USABLE_ITEMS, BlacksmithEntity.REQUIRED_ITEMS);
	}

	public void setTargetCraftingLocPos(final BlockPos targetCraftPos) {
		this.targetCraftPos = targetCraftPos;
	}

	public BlockPos getTargetCraftPos() {
		return this.targetCraftPos;
	}

	@Override
	public void mobTick() {
		super.mobTick();
		if (this.age % 200 == 0) {
			this.requestCraftable();
		}
	}

	public abstract void requestCraftable();
}
