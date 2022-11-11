package net.denanu.amazia.entities.village.server.goal.storage;

import javax.annotation.Nullable;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.utils.callback.VoidToVoidCallback;
import net.denanu.amazia.village.sceduling.utils.StoragePathingData;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public class GetItemFromStorage extends InteractWithContainerGoal {
	private StorageGetInteractionGoalInterface master;
	@Nullable
	private VoidToVoidCallback callback;
	
	public GetItemFromStorage(AmaziaVillagerEntity e, StorageGetInteractionGoalInterface _master) {
		this(e, _master, null);
	}
	
	public GetItemFromStorage(AmaziaVillagerEntity e, StorageGetInteractionGoalInterface _master, VoidToVoidCallback callback) {
		super(e);
		this.master = _master;
		this.callback = callback;
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
		if (this.callback != null)
			this.callback.call();
	}

	@Override
	public StoragePathingData getContainerPos() {
		return this.master.getTarget();
	}

	public Item getItem() {
		return this.master.getItem();
	}

}
