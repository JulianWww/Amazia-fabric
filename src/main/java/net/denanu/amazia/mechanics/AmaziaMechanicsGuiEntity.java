package net.denanu.amazia.mechanics;

import java.util.OptionalInt;

import net.denanu.amazia.GUI.AmaziaVillagerUIScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;

public interface AmaziaMechanicsGuiEntity {
	default public void sendVillagerData(final PlayerEntity player2, final Text name) {
		final OptionalInt optionalInt = player2.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> new AmaziaVillagerUIScreenHandler(syncId, playerInventory, this), name));
	}
}
