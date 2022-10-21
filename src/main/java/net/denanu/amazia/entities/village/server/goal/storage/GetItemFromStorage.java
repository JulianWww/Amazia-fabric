package net.denanu.amazia.entities.village.server.goal.storage;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.village.sceduling.utils.StoragePathingData;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public class GetItemFromStorage extends InteractWithContainerGoal {
	private StorageGetInteractionGoalInterface master;
	
	public GetItemFromStorage(AmaziaVillagerEntity e, StorageGetInteractionGoalInterface _master) {
		super(e);
		this.master = _master;
	}

	@Override
	protected int getRequiredTime() {
		return 20;
	}

	@Override
	protected void takeAction() {
		LootableContainerBlockEntity inventory = this.master.getTarget().getStorageInventory((ServerWorld)this.entity.getWorld());
		if (inventory != null) {
			for (int idx=0; idx < inventory.size(); idx++) {
				ItemStack stack = inventory.getStack(idx);
				if (stack.isOf(this.getItem())) {
					ItemStack leftOver = this.entity.getInventory().addStack(stack);
					if (leftOver.isEmpty()) {
						inventory.setStack(idx, ItemStack.EMPTY);
					}
					else {
						inventory.setStack(idx, leftOver);
					}
					return;
				}
			}
		}
		return;
	}
	
	@Override
	public void stop() {
		super.stop();
		master.StorageInteractionDone();
	}

	@Override
	public StoragePathingData getContainerPos() {
		return this.master.getTarget();
	}

	public Item getItem() {
		return this.master.getItem();
	}

}
