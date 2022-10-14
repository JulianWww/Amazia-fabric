package net.denanu.amazia.entities;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.client.FarmerRenderer;
import net.denanu.amazia.entities.village.server.FarmerEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmaziaEntities {
	 public static final EntityType<FarmerEntity> FARMER = Registry.register(
	            Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "farmer"),
	            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, FarmerEntity::new)
	                    .dimensions(EntityDimensions.fixed(0.4f, 1.5f)).build());
	 
	 public static void registerAttributes() {
		 FabricDefaultAttributeRegistry.register(FARMER, FarmerEntity.setAttributes());
	 }
	 
	 public static void registerRenderer() {
		 EntityRendererRegistry.register(FARMER, FarmerRenderer::new);
	 }
}
