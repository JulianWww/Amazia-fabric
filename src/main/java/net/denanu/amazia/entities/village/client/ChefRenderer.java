package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.ChefEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class ChefRenderer extends GeoEntityRenderer<ChefEntity> {
	public ChefRenderer(final EntityRendererFactory.Context ctx) {
		super(ctx, new ChefModel());
		this.shadowRadius = 0.4f;
	}

	@Override
	public Identifier getTextureResource(final ChefEntity instance) {
		return new Identifier(Amazia.MOD_ID, "textures/entity/farmer_texture.png");
	}

	@Override
	public RenderLayer getRenderType(final ChefEntity animatable, final float partialTicks, final MatrixStack stack,
			final VertexConsumerProvider renderTypeBuffer, final VertexConsumer vertexBuilder,
			final int packedLightIn, final Identifier textureLocation) {

		return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}
}