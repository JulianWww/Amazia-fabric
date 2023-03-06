package net.denanu.amazia.mechanics;

import net.denanu.amazia.mechanics.education.IAmaziaEducatedEntity;
import net.denanu.amazia.mechanics.happyness.IAmaziaHappynessEntity;
import net.denanu.amazia.mechanics.intelligence.IAmaziaIntelligenceEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public interface AmaziaMechanicsGuiEntity extends NamedScreenHandlerFactory, IAmaziaHappynessEntity, IAmaziaIntelligenceEntity, IAmaziaEducatedEntity {
	default void sendVillagerData(final PlayerEntity player2, final Text name) {
		player2.openHandledScreen(this);
	}

	@Override
	float getIntelligence();

	float getMaxVillagerHealth();
	float getMaxHunger();

	Identifier getProfession();

	Text getCustomName();
	Text getDefaultNameAcessor();

	SimpleInventory getInventory();
}
