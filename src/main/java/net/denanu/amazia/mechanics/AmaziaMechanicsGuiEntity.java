package net.denanu.amazia.mechanics;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;

public interface AmaziaMechanicsGuiEntity extends NamedScreenHandlerFactory {
	default void sendVillagerData(final PlayerEntity player2, final Text name) {
		player2.openHandledScreen(this);
	}

	float getIntelligence();
}
