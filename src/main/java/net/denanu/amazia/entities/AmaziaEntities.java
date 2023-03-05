package net.denanu.amazia.entities;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.furnature.SeatEntity;
import net.denanu.amazia.entities.furnature.SeatRenderer;
import net.denanu.amazia.entities.merchants.AmaziaMerchant;
import net.denanu.amazia.entities.merchants.client.MerchantRenderer;
import net.denanu.amazia.entities.village.client.AmaziaModel;
import net.denanu.amazia.entities.village.client.AmaziaRenderer;
import net.denanu.amazia.entities.village.client.BlacksmithRenderer;
import net.denanu.amazia.entities.village.client.ChefRenderer;
import net.denanu.amazia.entities.village.client.ChildRenderer;
import net.denanu.amazia.entities.village.client.ClericRenderer;
import net.denanu.amazia.entities.village.client.DruidRenderer;
import net.denanu.amazia.entities.village.client.EnchanterRenderer;
import net.denanu.amazia.entities.village.client.FarmerRenderer;
import net.denanu.amazia.entities.village.client.GuardRenderer;
import net.denanu.amazia.entities.village.client.LumberjackRenderer;
import net.denanu.amazia.entities.village.client.MinerRenderer;
import net.denanu.amazia.entities.village.client.NitwitRenderer;
import net.denanu.amazia.entities.village.client.RancherRenderer;
import net.denanu.amazia.entities.village.client.TeacherRenderer;
import net.denanu.amazia.entities.village.merchant.AmaziaVillageMerchant;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.entities.village.server.BardEntity;
import net.denanu.amazia.entities.village.server.BlacksmithEntity;
import net.denanu.amazia.entities.village.server.ChefEntity;
import net.denanu.amazia.entities.village.server.ChildEntity;
import net.denanu.amazia.entities.village.server.ClericEntity;
import net.denanu.amazia.entities.village.server.DruidEntity;
import net.denanu.amazia.entities.village.server.EnchanterEntity;
import net.denanu.amazia.entities.village.server.FarmerEntity;
import net.denanu.amazia.entities.village.server.GuardEntity;
import net.denanu.amazia.entities.village.server.LumberjackEntity;
import net.denanu.amazia.entities.village.server.MinerEntity;
import net.denanu.amazia.entities.village.server.NitwitEntity;
import net.denanu.amazia.entities.village.server.RancherEntity;
import net.denanu.amazia.entities.village.server.TeacherEntity;
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
	private static float getVillagerHeight() {
		return 2.1f;
	}
	private static float getVillagerWidth() {
		return 0.6f;
	}

	public static final EntityType<FarmerEntity> FARMER = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "farmer"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, FarmerEntity::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<MinerEntity> MINER = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "miner"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MinerEntity::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<LumberjackEntity> LUMBERJACK = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "lumberjack"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, LumberjackEntity::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<RancherEntity> RANCHER = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "rancher"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, RancherEntity::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<EnchanterEntity> ENCHANTER = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "enchanter"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, EnchanterEntity::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<BlacksmithEntity> BLACKSMITH = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "blacksmith"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BlacksmithEntity::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<GuardEntity> GUARD = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "guard"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, GuardEntity::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<NitwitEntity> NITWIT = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "nitwit"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NitwitEntity::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<DruidEntity> DRUID = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "druid"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DruidEntity::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<ClericEntity> CLERIC = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "cleric"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ClericEntity::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<ChefEntity> CHEF = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "chef"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ChefEntity::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<BardEntity> BARD = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "bard"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, BardEntity::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<TeacherEntity> TEACHER = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "teacher"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TeacherEntity::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<ChildEntity> CHILD = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "child"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, ChildEntity::new)
			.dimensions(EntityDimensions.fixed(0.2f, 0.75f)).build());



	public static final EntityType<AmaziaMerchant> MERCHANT = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "merchant"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, AmaziaMerchant::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<AmaziaVillageMerchant> VILLAGE_MERCHANT = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(Amazia.MOD_ID, "village_merchant"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, AmaziaVillageMerchant::new)
			.dimensions(EntityDimensions.fixed(AmaziaEntities.getVillagerWidth(), AmaziaEntities.getVillagerHeight())).build());

	public static final EntityType<SeatEntity> SEAT = Registry.register(
			Registry.ENTITY_TYPE, Identifier.of(Amazia.MOD_ID, "seat"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, SeatEntity::new).build()
			);

	public static void registerAttributes() {
		FabricDefaultAttributeRegistry.register(AmaziaEntities.FARMER,				AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.MINER,				AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.LUMBERJACK,			AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.RANCHER,				AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.ENCHANTER,			AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.BLACKSMITH,			AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.GUARD,				GuardEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.NITWIT,				AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.DRUID,				AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.CLERIC,				AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.CHEF, 				AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.BARD, 				AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.TEACHER,				AmaziaVillagerEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.CHILD,				ChildEntity.setAttributes());

		FabricDefaultAttributeRegistry.register(AmaziaEntities.MERCHANT,			AmaziaMerchant.setAttributes());
		FabricDefaultAttributeRegistry.register(AmaziaEntities.VILLAGE_MERCHANT,	AmaziaMerchant.setAttributes());


		// GOALS
		FeedAnimalGoal.setup();
	}

	public static void registerRenderer() {
		EntityRendererRegistry.register(AmaziaEntities.FARMER,				FarmerRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.MINER,				MinerRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.LUMBERJACK,			LumberjackRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.RANCHER,				RancherRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.ENCHANTER,			EnchanterRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.BLACKSMITH,			BlacksmithRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.GUARD,				GuardRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.NITWIT,				NitwitRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.DRUID, 				DruidRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.CLERIC,				ClericRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.CHEF,				ChefRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.BARD,				ctx -> new AmaziaRenderer<>(ctx, new AmaziaModel<>("bard")));
		EntityRendererRegistry.register(AmaziaEntities.TEACHER, 			TeacherRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.CHILD, 				ChildRenderer::new);

		EntityRendererRegistry.register(AmaziaEntities.MERCHANT,			MerchantRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.VILLAGE_MERCHANT,	MerchantRenderer::new);
		EntityRendererRegistry.register(AmaziaEntities.SEAT,				SeatRenderer::new);
	}
}
