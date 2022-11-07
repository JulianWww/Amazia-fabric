package net.denanu.amazia.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.denanu.amazia.Amazia;
import net.denanu.amazia.components.entity.BooleanSyncedCompnent;
import net.denanu.amazia.components.entity.IBooleanCompnent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;

public class AmaziaComponents implements EntityComponentInitializer {
	public static final ComponentKey<IBooleanCompnent> PART_OF_VILLAGE = ComponentRegistry.getOrCreate(Identifier.of(Amazia.MOD_ID, "is-part-of-village"), IBooleanCompnent.class);
	public static final ComponentKey<IBooleanCompnent> CAN_COLLIDE = ComponentRegistry.getOrCreate(Identifier.of(Amazia.MOD_ID, "can-collide"), IBooleanCompnent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(AnimalEntity.class, PART_OF_VILLAGE, e -> new BooleanSyncedCompnent(false));
		registry.registerFor(Entity.class,       CAN_COLLIDE,     e -> new BooleanSyncedCompnent(true));
	}
	
	public static boolean getIsPartOfVillage(AnimalEntity animal) {
		return PART_OF_VILLAGE.get(animal).getValue();
	}
	
	public static void setIsPartOfVillage(AnimalEntity animal, boolean value) {
		PART_OF_VILLAGE.get(animal).setValue(value);
	}
	
	public static boolean getCanCollide(Entity animal) {
		return CAN_COLLIDE.get(animal).getValue();
	}
	
	public static void setCanCollide(Entity animal, boolean value) {
		CAN_COLLIDE.get(animal).setValue(value);
	}
}
