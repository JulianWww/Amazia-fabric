package net.denanu.amazia.entities;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.merchants.AmaziaMerchant;
import net.denanu.amazia.entities.merchants.client.MerchantRenderer;
import net.denanu.amazia.entities.village.client.BlacksmithRenderer;
import net.denanu.amazia.entities.village.client.EnchanterRenderer;
import net.denanu.amazia.entities.village.client.FarmerRenderer;
import net.denanu.amazia.entities.village.client.LumberjackRenderer;
import net.denanu.amazia.entities.village.client.MinerRenderer;
import net.denanu.amazia.entities.village.client.RancherRenderer;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.BlacksmithEntity;
import net.denanu.amazia.entities.village.server.EnchanterEntity;
import net.denanu.amazia.entities.village.server.FarmerEntity;
import net.denanu.amazia.entities.village.server.LumberjackEntity;
import net.denanu.amazia.entities.village.server.MinerEntity;
import net.denanu.amazia.entities.village.server.RancherEntity;
import net.denanu.amazia.entities.village.server.goal.rancher.FeedAnimalGoal;
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

	public static final EntityType<MinerEntity> MINER = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "miner"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MinerEntity::new)
			.dimensions(EntityDimensions.fixed(0.4f, 1.5f)).build());

	public static final EntityType<LumberjackEntity> LUMBERJACK = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "lumberjack"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, LumberjackEntity::new)
			.dimensions(EntityDimensions.fixed(0.4f, 1.5f)).build());

	public static final EntityType<RancherEntity> RANCHER = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "rancher"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, RancherEntity::new)
			.dimensions(EntityDimensions.fixed(0.4f, 1.5f)).build());

	public static final EntityType<EnchanterEntity> ENCHANTER = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "enchanter"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, EnchanterEntity::new)
			.dimensions(EntityDimensions.fixed(0.4f, 1.5f)).build());

	public static final EntityType<BlacksmithEntity> BLACKSMITH = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "blacksmith"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BlacksmithEntity::new)
			.dimensions(EntityDimensions.fixed(0.4f, 1.5f)).build());



	public static final EntityType<AmaziaMerchant> MERCHANT = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "merchant"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, AmaziaMerchant::new)
			.dimensions(EntityDimensions.fixed(0.4f, 1.5f)).build());

	public static void registerAttributes() {
		FabricDefaultAttributeRegistry.register(AmaziaEntities.FARMER, AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.MINER, AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.LUMBERJACK, AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.RANCHER, AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.ENCHANTER, AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.BLACKSMITH, AmaziaVillagerEntity.setAttributes());

		FabricDefaultAttributeRegistry.register(AmaziaEntities.MERCHANT, AmaziaMerchant.setAttributes());


		// GOALS
		FeedAnimalGoal.setup();
	}

	public static void registerRenderer() {
		EntityRendererRegistry.register(AmaziaEntities.FARMER, FarmerRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.MINER,  MinerRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.LUMBERJACK, LumberjackRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.RANCHER, RancherRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.ENCHANTER, EnchanterRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.BLACKSMITH, BlacksmithRenderer::new);

		EntityRendererRegistry.register(AmaziaEntities.MERCHANT, MerchantRenderer::new);
	}
}
