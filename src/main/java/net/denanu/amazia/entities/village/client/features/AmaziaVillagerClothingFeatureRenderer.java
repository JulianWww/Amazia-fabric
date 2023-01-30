/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.denanu.amazia.entities.village.client.features;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@Environment(value=EnvType.CLIENT)
public class AmaziaVillagerClothingFeatureRenderer<T extends AmaziaVillagerEntity & IAnimatable> extends GeoLayerRenderer<T> {
	public AmaziaVillagerClothingFeatureRenderer(final IGeoRenderer<T> entityRendererIn) {
		super(entityRendererIn);
	}

	@Override
	public void render(final MatrixStack PoseStackIn, final VertexConsumerProvider bufferIn, final int packedLightIn, final T villager, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch) {
		this.renderPart(AmaziaVillagerClothingFeatureRenderer.getTypeId(villager.getData().getType()), PoseStackIn, bufferIn, packedLightIn, villager, partialTicks);
		this.renderPart(AmaziaVillagerClothingFeatureRenderer.getProfessionId(villager.getProfession()), PoseStackIn, bufferIn, packedLightIn, villager, partialTicks);
	}

	@SuppressWarnings("unchecked")
	private void renderPart(final Identifier id, final MatrixStack PoseStackIn, final VertexConsumerProvider bufferIn, final int packedLightIn, final T villager, final float partialTicks) {
		final RenderLayer cameo = RenderLayer.getArmorCutoutNoCull(id);
		this.getRenderer().render(this.getEntityModel().getModel(this.getRenderer().getGeoModelProvider().getModelResource(villager)), villager, partialTicks, cameo,
				PoseStackIn, bufferIn, bufferIn.getBuffer(cameo), packedLightIn, OverlayTexture.DEFAULT_UV, 1f, 1f,
				1f, 1f);
	}

	protected static Identifier getProfessionId(final Identifier id) {
		return AmaziaVillagerClothingFeatureRenderer.getTextureId(id, "professions");
	}

	protected static Identifier getTypeId(final Identifier id) {
		return AmaziaVillagerClothingFeatureRenderer.getTextureId(id, "type");
	}

	private static Identifier getTextureId(final Identifier id, final String type) {
		return Identifier.of(id.getNamespace(), "textures/entity/villagers/" + type + "/" + id.getPath() + ".png");
	}
}

