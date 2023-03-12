package net.denanu.amazia.GUI;

import net.denanu.amazia.GUI.widgets.AmaziaLockableSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class TakeOnlyContainerScreenHandler extends ScreenHandler {
	private static final int columns = 9;
	private final Inventory inventory;
	private final int rows;

	public TakeOnlyContainerScreenHandler(final ScreenHandlerType<?> type, final int syncId, final PlayerInventory playerInventory, final Inventory inventory, final int rows) {
		super(type, syncId);
		int k;
		int j;
		ScreenHandler.checkSize(inventory, rows * 9);
		this.inventory = inventory;
		this.rows = rows;
		inventory.onOpen(playerInventory.player);
		final int i = (this.rows - 4) * 18;
		for (j = 0; j < this.rows; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlot(new AmaziaLockableSlot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18, false, true));
			}
		}
		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
			}
		}
		for (j = 0; j < 9; ++j) {
			this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
		}
	}

	@Override
	public boolean canUse(final PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(final PlayerEntity player, final int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		final Slot slot = this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			final ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index < this.rows * TakeOnlyContainerScreenHandler.columns ? !this.insertItem(itemStack2, this.rows * TakeOnlyContainerScreenHandler.columns, this.slots.size(), true) : !this.insertItem(itemStack2, 0, this.rows * TakeOnlyContainerScreenHandler.columns, false)) {
				return ItemStack.EMPTY;
			}
			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
		}
		return itemStack;
	}

	@Override
	public void close(final PlayerEntity player) {
		super.close(player);
		this.inventory.onClose(player);
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public int getRows() {
		return this.rows;
	}
}
