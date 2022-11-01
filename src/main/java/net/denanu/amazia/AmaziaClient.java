package net.denanu.amazia;

import net.denanu.amazia.GUI.AmaziaScreens;
import net.denanu.amazia.GUI.TradingScreen;
import net.denanu.amazia.commands.args.AmaziaArgumentTypes;
import net.denanu.amazia.entities.AmaziaEntities;
import net.denanu.amazia.networking.AmaziaNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class AmaziaClient implements ClientModInitializer {

	@SuppressWarnings("deprecation")
	@Override
	public void onInitializeClient() {
		AmaziaEntities.registerRenderer();
		
		// Networking
		AmaziaNetworking.registerS2CPackets();
		
		// Screen
		
		ScreenRegistry.register(AmaziaScreens.TRADING_SCREEN_HANDLER, TradingScreen::new);
	}

}
