package net.denanu.amazia;

import net.denanu.amazia.GUI.AmaziaScreens;
import net.denanu.amazia.GUI.AmaziaVillagerUIScreen;
import net.denanu.amazia.GUI.TradingScreen;
import net.denanu.amazia.GUI.debug.VillagePathingOverlay;
import net.denanu.amazia.GUI.renderers.VillageBorderRenderer;
import net.denanu.amazia.entities.AmaziaEntities;
import net.denanu.amazia.mechanics.leveling.AmaziaProfessions;
import net.denanu.amazia.networking.AmaziaNetworking;
import net.denanu.amazia.utils.Predicates;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class AmaziaClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		AmaziaEntities.registerRenderer();

		// Networking
		AmaziaNetworking.registerS2CPackets();

		// Screen

		HandledScreens.register(AmaziaScreens.TRADING_SCREEN_HANDLER, TradingScreen::new);
		HandledScreens.register(AmaziaScreens.VILLAGER_SCREEN_HANDLER, AmaziaVillagerUIScreen::new);

		VillagePathingOverlay.setup();
		Predicates.setup();

		WorldRenderEvents.BEFORE_DEBUG_RENDER.register(VillagePathingOverlay::render);
		// WorldRenderEvents.BEFORE_DEBUG_RENDER.register(VillageProjectileTargetingDebugOverlay::render);
		ClientTickEvents.END_CLIENT_TICK.register(new VillagePathingOverlay());

		ClientLifecycleEvents.CLIENT_STOPPING.register(VillageBorderRenderer::clear);
		AmaziaProfessions.setup();
	}

}
