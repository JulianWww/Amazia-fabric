package net.denanu.amazia.entities.village.client.features;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class AmaziaVillerHeldItemFeatureRenderer<T extends AmaziaVillagerEntity & IAnimatable> extends GeoLayerRenderer<T> {
	private final HeldItemRenderer heldItemRenderer;

	public AmaziaVillerHeldItemFeatureRenderer(final IGeoRenderer<T> entityRendererIn, final HeldItemRenderer renderer) {
		super(entityRendererIn);
		this.heldItemRenderer = renderer;
	}

	@Override
	public void render(final MatrixStack matrixStack, final VertexConsumerProvider bufferIn, final int packedLightIn,
			final T entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks,
			final float netHeadYaw, final float headPitch) {
		matrixStack.push();
		matrixStack.translate(0.0, 0.4f, -0.4f);
		matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180.0f));
		final ItemStack itemStack = ((LivingEntity)entitylivingbaseIn).getEquippedStack(EquipmentSlot.MAINHAND);
		this.heldItemRenderer.renderItem(entitylivingbaseIn, itemStack, ModelTransformation.Mode.GROUND, false, matrixStack, bufferIn, packedLightIn);
		matrixStack.pop();
	}

}
