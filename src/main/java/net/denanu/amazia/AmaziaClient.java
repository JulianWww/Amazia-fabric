package net.denanu.amazia;

import net.denanu.amazia.entities.AmaziaEntities;
import net.fabricmc.api.ClientModInitializer;

public class AmaziaClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		AmaziaEntities.registerRenderer();
	}

}
