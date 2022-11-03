package net.denanu.amazia.economy.offerModifiers.finalizers;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.utils.random.ConstrainedGaussianRandom;
import net.denanu.amazia.utils.scanners.ChunkScanner;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.map.MapIcon;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.StructureKeys;

public class AmaziaFinalModifiers {
	public static MapFinalModifer EXPLORER_MAP_BUILDER =
			new MapFinalModifer(MapIcon.Type.RED_X, "Explorer Map", Identifier.of(Amazia.MOD_ID, "explorer_map_finalzier"))
			.add(StructureKeys.BURIED_TREASURE, 10.0f);
	
	public static SuspiciousStewFinalizer SUSPICOUS_STEW_FINALIZER =
			new SuspiciousStewFinalizer(Identifier.of(Amazia.MOD_ID, "suspicious_stew_finalizer"), new ConstrainedGaussianRandom(1200, 500, 12000, 0))
			.add(StatusEffects.SPEED, 			1.0f)
			.add(StatusEffects.SLOWNESS,		1.0f)
			.add(StatusEffects.HASTE, 			1.0f)
			.add(StatusEffects.MINING_FATIGUE, 	1.0f)
			.add(StatusEffects.STRENGTH, 		1.0f)
			.add(StatusEffects.INSTANT_HEALTH, 	1.0f)
			.add(StatusEffects.INSTANT_DAMAGE, 	1.0f)
			.add(StatusEffects.JUMP_BOOST, 		1.0f)
			.add(StatusEffects.NAUSEA, 			1.0f)
			.add(StatusEffects.REGENERATION, 	1.0f)
			.add(StatusEffects.RESISTANCE, 		1.0f)
			.add(StatusEffects.FIRE_RESISTANCE, 1.0f)
			.add(StatusEffects.WATER_BREATHING, 1.0f)
			.add(StatusEffects.INVISIBILITY, 	1.0f)
			.add(StatusEffects.BLINDNESS, 		1.0f)
			.add(StatusEffects.NIGHT_VISION, 	1.0f)
			.add(StatusEffects.HUNGER, 			1.0f)
			.add(StatusEffects.WEAKNESS, 		1.0f)
			.add(StatusEffects.POISON, 			1.0f)
			.add(StatusEffects.WITHER,			1.0f)
			.add(StatusEffects.HEALTH_BOOST, 	1.0f)
			.add(StatusEffects.ABSORPTION,		1.0f)
			.add(StatusEffects.SATURATION, 		1.0f)
			.add(StatusEffects.GLOWING,			1.0f)
			.add(StatusEffects.LEVITATION, 		1.0f)
			.add(StatusEffects.LUCK, 			1.0f)
			.add(StatusEffects.UNLUCK, 			1.0f)
			.add(StatusEffects.SLOW_FALLING, 	1.0f)
			.add(StatusEffects.CONDUIT_POWER, 	1.0f)
			.add(StatusEffects.DOLPHINS_GRACE, 	1.0f)
			.add(StatusEffects.BAD_OMEN, 		1.0f)
			.add(StatusEffects.DARKNESS, 		1.0f);
	
	
	public static void setup() {
		ChunkScanner.toLookFor.add(StructureKeys.BURIED_TREASURE.getValue());
	}
}
