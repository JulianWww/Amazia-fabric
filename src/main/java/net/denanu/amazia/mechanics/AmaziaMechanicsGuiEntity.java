package net.denanu.amazia.mechanics;

import java.util.OptionalInt;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;

public interface AmaziaMechanicsGuiEntity extends NamedScreenHandlerFactory {
	default public void sendVillagerData(final PlayerEntity player2, final Text name) {
		final OptionalInt optionalInt = player2.openHandledScreen(this);
	}
}
