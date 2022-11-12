package net.denanu.amazia.entities.village.server.goal.storage;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.utils.callback.VoidToVoidCallback;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;

public class EnchanterGetItemFromStorageGoal extends GetItemFromStorage {

	public EnchanterGetItemFromStorageGoal(AmaziaVillagerEntity e, StorageGetInteractionGoalInterface _master) {
		super(e, _master, null);
	}
	
	public EnchanterGetItemFromStorageGoal(AmaziaVillagerEntity e, StorageGetInteractionGoalInterface _master, VoidToVoidCallback callback) {
		super(e, _master, callback);
	}
	
	@Override
	protected void takeAction() {
		if (this.getItem() == Items.BOOK || this.getItem() == Items.ENCHANTED_BOOK) {
			LootableContainerBlockEntity inventory = this.master.getTarget().getStorageInventory((ServerWorld)this.entity.getWorld());
			if (inventory != null) {
				for (int idx=0; idx < inventory.size(); idx++) {
					ItemStack stack = inventory.getStack(idx);
					if (stack.isOf(this.getItem())) {
						if (this.entity.getInventory().addStack(new ItemStack(this.getItem(), 1)).isEmpty()) {
							stack.setCount(stack.getCount() - 1);
							if (stack.isEmpty()) {
								inventory.setStack(idx, ItemStack.EMPTY);
							}
							else {
								inventory.setStack(idx, stack);
							}
						}
						return;
					}
				}
			}
		}
		else {
			super.takeAction();
		}
	}
}
