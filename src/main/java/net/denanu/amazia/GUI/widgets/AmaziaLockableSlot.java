package net.denanu.amazia.GUI.widgets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class AmaziaLockableSlot extends Slot {
	private boolean take, put;

	public AmaziaLockableSlot(final Inventory inventory, final int index, final int x, final int y) {
		this(inventory, index, x, y, false, false);
	}

	public AmaziaLockableSlot(final Inventory inventory, final int index, final int x, final int y, final boolean put, final boolean take) {
		super(inventory, index, x, y);
		this.put = put;
		this.take = take;
	}

	public boolean canTake() {
		return this.take;
	}

	public boolean canPut() {
		return this.put;
	}

	public void lockPut() {
		this.put = false;
	}

	public void unlockPut() {
		this.put = true;
	}

	public void lockTake() {
		this.take = false;
	}

	public void unlockTake() {
		this.take = true;
	}

	public void setLocked(final boolean put, final boolean take) {
		this.put = put;
		this.take = take;
	}

	@Override
	public boolean canTakeItems(final PlayerEntity playerEntity) {
		return this.take;
	}

	@Override
	public boolean canInsert(final ItemStack stack) {
		return this.put;
	}
}
