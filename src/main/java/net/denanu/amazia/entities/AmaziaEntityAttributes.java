package net.denanu.amazia.entities;

import net.denanu.amazia.Amazia;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmaziaEntityAttributes {
	public static EntityAttribute MAX_HUNGER = AmaziaEntityAttributes.register("villager.max_hunger", new ClampedEntityAttribute("amazia:attribute.villager.max_hunger", 100f, 1f, 1024f));


	private static EntityAttribute register(final String id, final EntityAttribute attribute) {
		return Registry.register(Registry.ATTRIBUTE, Identifier.of(Amazia.MOD_ID, id), attribute);
	}

	public static void setup() {

	}
}
