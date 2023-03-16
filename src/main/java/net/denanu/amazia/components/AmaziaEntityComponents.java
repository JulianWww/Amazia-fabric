package net.denanu.amazia.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.denanu.amazia.Amazia;
import net.denanu.amazia.components.components.BooleanCompnent;
import net.denanu.amazia.components.components.IBooleanCompnent;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;

public class AmaziaEntityComponents implements EntityComponentInitializer {
	public static final ComponentKey<IBooleanCompnent> PART_OF_VILLAGE = ComponentRegistry.getOrCreate(Identifier.of(Amazia.MOD_ID, "is-part-of-village"), IBooleanCompnent.class);

	@Override
	public void registerEntityComponentFactories(final EntityComponentFactoryRegistry registry) {
		registry.registerFor(AnimalEntity.class, AmaziaEntityComponents.PART_OF_VILLAGE, e -> new BooleanCompnent(false));
	}

	public static boolean getIsPartOfVillage(final AnimalEntity animal) {
		return AmaziaEntityComponents.PART_OF_VILLAGE.get(animal).getValue();
	}

	public static void setIsPartOfVillage(final AnimalEntity animal, final boolean value) {
		AmaziaEntityComponents.PART_OF_VILLAGE.get(animal).setValue(value);
	}
}
