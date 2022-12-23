package net.denanu.amazia.entities.village.server.goal.storage;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.mechanics.happyness.HappynessMap;
import net.denanu.amazia.village.sceduling.utils.DoubleDownPathingData;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public class PutItemInContainerGoal extends InteractWithContainerGoal {
	protected StoragePutInteractionGoalInterface master;

	public PutItemInContainerGoal(final AmaziaVillagerEntity e, final StoragePutInteractionGoalInterface _master) {
		super(e);
		this.master = _master;
	}

	@Override
	public boolean canStart() {
		return this.isStartable();
	}

	@Override
	public void stop() {
		super.stop();
		this.master.StorageInteractionDone();
	}

	@Override
	protected int getRequiredTime() {
		return 20;
	}

	@Override
	protected void takeAction() {
		final LootableContainerBlockEntity inventory = this.master.getTarget().getStorageInventory((ServerWorld)this.entity.getWorld());
		if (inventory != null) {
			int maxDepositable = this.master.getMaxDepositable();
			ItemStack toPut = this.getItem();
			for (int idx=0; idx < inventory.size(); idx++) {
				final ItemStack itemStack = inventory.getStack(idx);
				if (itemStack.isEmpty()) {
					inventory.setStack(idx, toPut.copy());

					final int depostiAmount = Math.min(toPut.getCount(), maxDepositable);
					inventory.getStack(idx).setCount( depostiAmount);
					toPut.setCount(toPut.getCount() - depostiAmount);
					break;
				}
				if (InteractWithContainerGoal.canMergeItems(itemStack, toPut)) {
					final int i = toPut.getMaxCount() - itemStack.getCount();
					final int j = Math.min(toPut.getCount(), Math.min(i, maxDepositable));
					toPut.decrement(j);
					itemStack.increment(j);

					maxDepositable = maxDepositable - j;
					if (maxDepositable == 0) {
						break;
					}
				}
				if (toPut.isEmpty()) { break; }
			}
			if (toPut.isEmpty()) {
				toPut = ItemStack.EMPTY;
			}
			this.entity.getInventory().setStack(this.getItemIdx(), toPut);
			inventory.markDirty();
			HappynessMap.looseDropOfItemsHappyness(this.entity);
		}
	}

	@Override
	public DoubleDownPathingData getContainerPos() {
		return this.master.getTarget();
	}

	public ItemStack getItem() {
		return this.master.getItem();
	}
	protected int getItemIdx() {
		return this.master.getItemIdx();
	}
}
