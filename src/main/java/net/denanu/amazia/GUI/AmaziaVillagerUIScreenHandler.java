package net.denanu.amazia.GUI;

import net.denanu.amazia.mechanics.AmaziaMechanicsGuiEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class AmaziaVillagerUIScreenHandler extends ScreenHandler {
	AmaziaMechanicsGuiEntity entity;

	public AmaziaVillagerUIScreenHandler(final ScreenHandlerType<?> type, final int syncId) {
		super(type, syncId);
	}

	public AmaziaVillagerUIScreenHandler(final int syncId, final PlayerInventory playerInventory) {
		this(syncId, playerInventory, null);
	}

	public AmaziaVillagerUIScreenHandler(final int syncId, final PlayerInventory playerInventory,
			final AmaziaMechanicsGuiEntity entity) {
		super(AmaziaScreens.VILLAGER_SCREEN_HANDLER, syncId);
		this.entity = entity;
	}

	@Override
	public ItemStack transferSlot(final PlayerEntity var1, final int var2) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(final PlayerEntity var1) {
		return true;
	}

}
