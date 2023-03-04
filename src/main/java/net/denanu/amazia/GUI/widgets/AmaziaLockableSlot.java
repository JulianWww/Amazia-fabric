package net.denanu.amazia.GUI.widgets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class AmaziaLockableSlot extends Slot {
	private boolean open;

	public AmaziaLockableSlot(final Inventory inventory, final int index, final int x, final int y) {
		this(inventory, index, x, y, false);
	}

	public AmaziaLockableSlot(final Inventory inventory, final int index, final int x, final int y, final boolean open) {
		super(inventory, index, x, y);
		this.open = open;
	}

	public boolean isLocked() {
		return !this.open;
	}

	public void lock() {
		this.open = false;
	}

	public void unlock() {
		this.open = true;
	}

	public void setLocked(final boolean locked) {
		this.open = !locked;
	}

	@Override
	public boolean canTakeItems(final PlayerEntity playerEntity) {
		return true;// this.open;
	}

	@Override
	public boolean canInsert(final ItemStack stack) {
		return true;//this.open;
	}
}
