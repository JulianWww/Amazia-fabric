package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.BlacksmithEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BlacksmithRenderer extends GeoEntityRenderer<BlacksmithEntity> {
    public BlacksmithRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new BlacksmithModel());
        this.shadowRadius = 0.4f;
    }

    @Override
    public Identifier getTextureResource(BlacksmithEntity instance) {
        return new Identifier(Amazia.MOD_ID, "textures/entity/farmer_texture.png");
    }

    @Override
    public RenderLayer getRenderType(BlacksmithEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder,
                                     int packedLightIn, Identifier textureLocation) {
        stack.scale(0.8f, 0.8f, 0.8f);

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}