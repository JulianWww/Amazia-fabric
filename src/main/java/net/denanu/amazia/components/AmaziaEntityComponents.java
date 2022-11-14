package net.denanu.amazia.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.denanu.amazia.Amazia;
import net.denanu.amazia.components.components.BooleanCompnent;
import net.denanu.amazia.components.components.IBooleanCompnent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;

public class AmaziaEntityComponents implements EntityComponentInitializer {
	public static final ComponentKey<IBooleanCompnent> PART_OF_VILLAGE = ComponentRegistry.getOrCreate(Identifier.of(Amazia.MOD_ID, "is-part-of-village"), IBooleanCompnent.class);
	public static final ComponentKey<IBooleanCompnent> CAN_COLLIDE = ComponentRegistry.getOrCreate(Identifier.of(Amazia.MOD_ID, "can-collide"), IBooleanCompnent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(AnimalEntity.class, AmaziaEntityComponents.PART_OF_VILLAGE, e -> new BooleanCompnent(false));
		registry.registerFor(Entity.class,       AmaziaEntityComponents.CAN_COLLIDE,     e -> new BooleanCompnent(true));
	}

	public static boolean getIsPartOfVillage(AnimalEntity animal) {
		return AmaziaEntityComponents.PART_OF_VILLAGE.get(animal).getValue();
	}

	public static void setIsPartOfVillage(AnimalEntity animal, boolean value) {
		AmaziaEntityComponents.PART_OF_VILLAGE.get(animal).setValue(value);
	}

	public static boolean getCanCollide(Entity animal) {
		return AmaziaEntityComponents.CAN_COLLIDE.get(animal).getValue();
	}

	public static void setCanCollide(Entity animal, boolean value) {
		AmaziaEntityComponents.CAN_COLLIDE.get(animal).setValue(value);
	}
}
