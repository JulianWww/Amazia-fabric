package net.denanu.amazia.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.denanu.amazia.Amazia;
import net.denanu.amazia.components.entity.PartOfVillageComponent;
import net.minecraft.util.Identifier;

public class AmaziaComponents implements EntityComponentInitializer {
	public static final ComponentKey<PartOfVillageComponent> PART_OF_VILLAGE = ComponentRegistry.getOrCreate(Identifier.of(Amazia.MOD_ID, "is-part-of-village"), PartOfVillageComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.register(PART_OF_VILLAGE, PartOfVillageComponent::new);
	}
}
