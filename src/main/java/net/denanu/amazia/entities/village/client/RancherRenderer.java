package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.RancherEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RancherRenderer extends GeoEntityRenderer<RancherEntity> {
    public RancherRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new RancherModel());
        this.shadowRadius = 0.4f;
    }

    @Override
    public Identifier getTextureResource(RancherEntity instance) {
        return new Identifier(Amazia.MOD_ID, "textures/entity/farmer_texture.png");
    }

    @Override
    public RenderLayer getRenderType(RancherEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder,
                                     int packedLightIn, Identifier textureLocation) {
        stack.scale(0.8f, 0.8f, 0.8f);

        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}