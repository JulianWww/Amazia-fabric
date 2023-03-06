package net.denanu.amazia.entities;

import net.denanu.amazia.entities.furnature.SeatRenderer;
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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class ClientEntities {
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
