package net.denanu.amazia.compat.mc.worldgen;

import net.denanu.amazia.Amazia;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

public class Worldgen {

	// PYTHON GENERATOR BEGIN
	public static final RegistryKey<PlacedFeature> GIANT_RUBY_ORE = RegistryKey.of(Registry.PLACED_FEATURE_KEY, Identifier.of(Amazia.MOD_ID, "giant_ruby_ore"));
	public static final RegistryKey<PlacedFeature> LARGE_RUBY_ORE = RegistryKey.of(Registry.PLACED_FEATURE_KEY, Identifier.of(Amazia.MOD_ID, "large_ruby_ore"));
	public static final RegistryKey<PlacedFeature> MEDIUM_RUBY_ORE = RegistryKey.of(Registry.PLACED_FEATURE_KEY, Identifier.of(Amazia.MOD_ID, "medium_ruby_ore"));
	public static final RegistryKey<PlacedFeature> SMALL_RUBY_ORE = RegistryKey.of(Registry.PLACED_FEATURE_KEY, Identifier.of(Amazia.MOD_ID, "small_ruby_ore"));
	public static final RegistryKey<PlacedFeature> TINY_RUBY_ORE = RegistryKey.of(Registry.PLACED_FEATURE_KEY, Identifier.of(Amazia.MOD_ID, "tiny_ruby_ore"));
	
	
	public static void setup() {
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, Worldgen.GIANT_RUBY_ORE);
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, Worldgen.LARGE_RUBY_ORE);
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, Worldgen.MEDIUM_RUBY_ORE);
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, Worldgen.SMALL_RUBY_ORE);
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, Worldgen.TINY_RUBY_ORE);
	}
	// PYTHON GENERATOR END
}
