package net.denanu.amazia.entities;


import net.denanu.amazia.entities.furnature.SeatRenderer;
import net.denanu.amazia.entities.village.client.AmaziaModel;
import net.denanu.amazia.entities.village.client.AmaziaRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class ClientEntities {
	public static void registerRenderer() {
		EntityRendererRegistry.register(AmaziaEntities.FARMER,				ctx -> new AmaziaRenderer<>(ctx, true,  new AmaziaModel<>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.MINER,				ctx -> new AmaziaRenderer<>(ctx, true,  new AmaziaModel<>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.LUMBERJACK,			ctx -> new AmaziaRenderer<>(ctx, true,  new AmaziaModel<>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.RANCHER,				ctx -> new AmaziaRenderer<>(ctx, true,  new AmaziaModel<>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.ENCHANTER,			ctx -> new AmaziaRenderer<>(ctx, true,  new AmaziaModel<>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.BLACKSMITH,			ctx -> new AmaziaRenderer<>(ctx, true,  new AmaziaModel<>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.GUARD,				ctx -> new AmaziaRenderer<>(ctx, true,  new AmaziaModel<>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.NITWIT,				ctx -> new AmaziaRenderer<>(ctx, true,  new AmaziaModel<>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.DRUID, 				ctx -> new AmaziaRenderer<>(ctx, true,  new AmaziaModel<>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.CLERIC,				ctx -> new AmaziaRenderer<>(ctx, true,  new AmaziaModel<>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.CHEF,				ctx -> new AmaziaRenderer<>(ctx, true,  new AmaziaModel<>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.BARD,				ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<>("bard")));
		EntityRendererRegistry.register(AmaziaEntities.TEACHER, 			ctx -> new AmaziaRenderer<>(ctx, true,  new AmaziaModel<>("villager")));
		EntityRendererRegistry.register(AmaziaEntities.CHILD, 				ctx -> new AmaziaRenderer<>(ctx, true,  new AmaziaModel<>("villager")));

		EntityRendererRegistry.register(AmaziaEntities.MERCHANT,			ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<>("merchant")));
		EntityRendererRegistry.register(AmaziaEntities.VILLAGE_MERCHANT,	ctx -> new AmaziaRenderer<>(ctx, false, new AmaziaModel<>("merchant")));
		EntityRendererRegistry.register(AmaziaEntities.SEAT,				SeatRenderer::new);
	}
}
