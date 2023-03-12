package net.denanu.amazia.components;

import java.util.HashSet;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.denanu.amazia.Amazia;
import net.denanu.amazia.components.components.IVillageComponent;
import net.denanu.amazia.components.components.VillageComponent;
import net.denanu.amazia.village.Village;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class AmaziaBlockComponents implements BlockComponentInitializer {
	private static final ComponentKey<IVillageComponent> VILLAGES = ComponentRegistry.getOrCreate(Identifier.of(Amazia.MOD_ID, "villages"), IVillageComponent.class);


	@Override
	public void registerBlockComponentFactories(final BlockComponentFactoryRegistry registry) {
		registry.registerFor(AbstractFurnaceBlockEntity.class, 	AmaziaBlockComponents.VILLAGES, e -> new VillageComponent());
		registry.registerFor(BedBlockEntity.class, 	AmaziaBlockComponents.VILLAGES, e -> new VillageComponent());
	}

	public static HashSet<BlockPos> getVillages(final Object provider) {
		return AmaziaBlockComponents.VILLAGES.get(provider).get();
	}

	public static void addVillage(final Object provider, final Village village) {
		AmaziaBlockComponents.VILLAGES.get(provider).add(village.getOrigin());
	}

	public static void removeVillage(final Object provider, final Village village) {
		AmaziaBlockComponents.VILLAGES.get(provider).remove(village.getOrigin());
	}
}
