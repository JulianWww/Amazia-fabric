package net.denanu.amazia.entities.village.server.goal.storage;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.village.sceduling.utils.StoragePathingData;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public class PutItemInContainerGoal extends InteractWithContainerGoal {
	protected StoragePutInteractionGoalInterface master;

	public PutItemInContainerGoal(AmaziaVillagerEntity e, StoragePutInteractionGoalInterface _master) {
		super(e);
		this.master = _master;
	}
	
	@Override
	public void stop() {
		super.stop();
		master.StorageInteractionDone();
	}

	@Override
	protected int getRequiredTime() {
		return 20;
	}

	@Override
	protected void takeAction() {
		LootableContainerBlockEntity inventory = this.master.getTarget().getStorageInventory((ServerWorld)this.entity.getWorld());
		if (inventory != null) {
			int maxDepositable = this.master.getMaxDepositable();
			ItemStack toPut = this.getItem();
			for (int idx=0; idx < inventory.size(); idx++) {
				ItemStack itemStack = inventory.getStack(idx);
				if (itemStack.isEmpty()) {
					inventory.setStack(idx, toPut.copy());
					
					int depostiAmount = Math.min(toPut.getCount(), maxDepositable);
					inventory.getStack(idx).setCount( depostiAmount);
					toPut.setCount(toPut.getCount() - depostiAmount);
					break;
	            } else if (canMergeItems(itemStack, toPut)) {
	                int i = toPut.getMaxCount() - itemStack.getCount();
	                int j = Math.min(toPut.getCount(), Math.min(i, maxDepositable));
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
		}
	}
	
	public StoragePathingData getContainerPos() {
		return this.master.getTarget();
	}

	public ItemStack getItem() {
		return master.getItem();
	}
	protected int getItemIdx() {
		return master.getItemIdx();
	}
}
