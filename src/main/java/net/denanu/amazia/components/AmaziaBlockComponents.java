package net.denanu.amazia.components;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.denanu.amazia.Amazia;
import net.denanu.amazia.components.components.IVillageComponent;
import net.denanu.amazia.components.components.VillageComponent;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.util.Identifier;

public class AmaziaBlockComponents implements BlockComponentInitializer {
	public static final ComponentKey<IVillageComponent> VILLAGES = ComponentRegistry.getOrCreate(Identifier.of(Amazia.MOD_ID, "villages"), IVillageComponent.class);

	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
		registry.registerFor(AbstractFurnaceBlockEntity.class, AmaziaBlockComponents.VILLAGES, e -> new VillageComponent());
	}
}
