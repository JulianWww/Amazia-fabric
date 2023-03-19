package net.denanu.amazia.entities;


import net.denanu.amazia.entities.furnature.SeatRenderer;
import net.denanu.amazia.entities.village.client.AmaziaModel;
import net.denanu.amazia.entities.village.client.AmaziaRenderer;
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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class ClientEntities {
	public static void registerRenderer() {
		EntityRendererRegistry.register(AmaziaEntities.FARMER,				ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<FarmerEntity>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.MINER,				ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<MinerEntity>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.LUMBERJACK,			ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<LumberjackEntity>("bard")));
		EntityRendererRegistry.register(AmaziaEntities.RANCHER,				ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<RancherEntity>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.ENCHANTER,			ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<EnchanterEntity>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.BLACKSMITH,			ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<BlacksmithEntity>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.GUARD,				ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<GuardEntity>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.NITWIT,				ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<NitwitEntity>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.DRUID, 				ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<DruidEntity>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.CLERIC,				ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<ClericEntity>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.CHEF,				ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<ChefEntity>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.BARD,				ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<BardEntity>("bard")));
		EntityRendererRegistry.register(AmaziaEntities.TEACHER, 			ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<TeacherEntity>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.CHILD, 				ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<ChildEntity>("villager")));

		EntityRendererRegistry.register(AmaziaEntities.MERCHANT,			ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<>("merchant")));
		EntityRendererRegistry.register(AmaziaEntities.VILLAGE_MERCHANT,	ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<>("merchant")));
		EntityRendererRegistry.register(AmaziaEntities.SEAT,				SeatRenderer::new);
	}
}
