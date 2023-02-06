package net.denanu.amazia.data;

import net.denanu.amazia.Amazia;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmaziaStatusEffects {
	public static final StatusEffect BLESSING = AmaziaStatusEffects.register("blessing", new ClericBlessStatusEffect());

	private static StatusEffect register(final String id, final StatusEffect effect) {
		return AmaziaStatusEffects.register(Identifier.of(Amazia.MOD_ID, id), effect);
	}

	public static StatusEffect register(final Identifier id, final StatusEffect effect) {
		return Registry.register(Registry.STATUS_EFFECT, id, effect);
	}

	public static void setup() {
	}
}
