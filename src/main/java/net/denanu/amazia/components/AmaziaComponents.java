package net.denanu.amazia.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.denanu.amazia.Amazia;
import net.denanu.amazia.components.entity.BooleanCompnent;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.util.Identifier;

public class AmaziaComponents implements EntityComponentInitializer {
	public static final ComponentKey<BooleanCompnent> PART_OF_VILLAGE = ComponentRegistry.getOrCreate(Identifier.of(Amazia.MOD_ID, "is-part-of-village"), BooleanCompnent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(AnimalEntity.class, PART_OF_VILLAGE, e -> new BooleanCompnent(false));
	}
	
	public static boolean getIsPartOfVillage(AnimalEntity animal) {
		boolean out = PART_OF_VILLAGE.get(animal).getValue();
		return out;
	}
	
	public static void setIsPartOfVillage(AnimalEntity animal, boolean value) {
		PART_OF_VILLAGE.get(animal).setValue(value);
	}
}
