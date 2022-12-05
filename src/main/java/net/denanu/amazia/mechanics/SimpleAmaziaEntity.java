package net.denanu.amazia.mechanics;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

public class SimpleAmaziaEntity implements AmaziaMechanicsGuiEntity {

	@Override
	public Text getDisplayName() {
		return null;
	}

	@Override
	public ScreenHandler createMenu(final int var1, final PlayerInventory var2, final PlayerEntity var3) {
		return null;
	}

}
