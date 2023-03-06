package net.denanu.amazia.entities.merchants.client;


import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.merchants.AmaziaMerchant;
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
public class MerchantRenderer extends GeoEntityRenderer<AmaziaMerchant> {
	public MerchantRenderer(final EntityRendererFactory.Context ctx) {
		super(ctx, new MerchantModel());
		this.shadowRadius = 0.4f;
	}

	@Override
	public Identifier getTextureResource(final AmaziaMerchant instance) {
		return new Identifier(Amazia.MOD_ID, "textures/entity/farmer_texture.png");
	}

	@Override
	public RenderLayer getRenderType(final AmaziaMerchant animatable, final float partialTicks, final MatrixStack stack,
			final VertexConsumerProvider renderTypeBuffer, final VertexConsumer vertexBuilder,
			final int packedLightIn, final Identifier textureLocation) {
		stack.scale(0.8f, 0.8f, 0.8f);

		return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}
}