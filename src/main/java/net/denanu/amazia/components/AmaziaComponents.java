package net.denanu.amazia.components;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import dev.onyxstudios.cca.api.v3.component.ComponentFactory;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.denanu.amazia.entities.village.server.FarmerEntity;

public class AmaziaComponents implements EntityComponentInitializer {

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		ComponentFactory<FarmerEntity, IBaritone> componentFactory = BaritoneAPI.getProvider().componentFactory();
	}

}
