package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.entities.village.client.features.AmaziaVillagerClothingFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import software.bernie.example.client.DefaultBipedBoneIdents;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class AmaziaRenderer<T extends PassiveEntity & AmaziaModelEntityI & IAnimatable> extends ExtendedGeoEntityRenderer<T> {
	public AmaziaRenderer(final EntityRendererFactory.Context ctx, final AnimatedGeoModel<T> model) {
		this(ctx, true, model);
	}

	public AmaziaRenderer(final EntityRendererFactory.Context ctx, final boolean biomeSpecifics, final AnimatedGeoModel<T> model) {
		super(ctx, model);
		this.shadowRadius = 0.4f;
		this.addLayer(new AmaziaVillagerClothingFeatureRenderer<>(this, biomeSpecifics));
	}

	@Override
	protected ItemStack getHeldItemForBone(final String boneName, final T currentEntity) {
		switch (boneName) {
		case DefaultBipedBoneIdents.LEFT_HAND_BONE_IDENT:
			return currentEntity.isLeftHanded() ? this.mainHand : this.offHand;
		case DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT:
			return currentEntity.isLeftHanded() ? this.offHand : this.mainHand;
		case DefaultBipedBoneIdents.POTION_BONE_IDENT:
			break;
		}
		return null;
	}

	@Override
	protected Mode getCameraTransformForItemAtBone(final ItemStack boneItem, final String boneName) {
		return switch (boneName) {
		case DefaultBipedBoneIdents.LEFT_HAND_BONE_IDENT -> Mode.THIRD_PERSON_RIGHT_HAND;
		case DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT -> Mode.THIRD_PERSON_RIGHT_HAND;
		default -> Mode.NONE;
		};
	}

	@Override
	protected void preRenderItem(final MatrixStack stack, final ItemStack item, final String boneName, final T currentEntity,
			final IBone bone) {
		if (item == this.mainHand || item == this.offHand) {
			stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
			final boolean shieldFlag = item.getItem() instanceof ShieldItem;
			if (item == this.mainHand) {
				if (shieldFlag) {
					stack.translate(0.0, 0.125, -0.25);
				} else {

				}
			}
			else if (shieldFlag) {
				stack.translate(0, 0.125, 0.25);
				stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
			} else {

			}
		}
	}

	@Override
	protected void postRenderItem(final MatrixStack PoseStack, final ItemStack item, final String boneName,
			final T currentEntity, final IBone bone) {

	}

	@Override
	protected ItemStack getArmorForBone(final String boneName, final T currentEntity) {
		return switch (boneName) {
		case "armorBipedLeftFoot", "armorBipedRightFoot", "armorBipedLeftFoot2", "armorBipedRightFoot2" -> this.boots;
		case "armorBipedLeftLeg", "armorBipedRightLeg", "armorBipedLeftLeg2", "armorBipedRightLeg2" -> this.leggings;
		case "armorBipedBody", "armorBipedRightArm", "armorBipedLeftArm" -> this.chestplate;
		case "armorBipedHead" -> this.helmet;
		default -> ItemStack.EMPTY;
		};
	}

	@Override
	protected EquipmentSlot getEquipmentSlotForArmorBone(final String boneName, final T currentEntity) {
		return switch (boneName) {
		case "armorBipedLeftFoot", "armorBipedRightFoot", "armorBipedLeftFoot2", "armorBipedRightFoot2" -> EquipmentSlot.FEET;
		case "armorBipedLeftLeg", "armorBipedRightLeg", "armorBipedLeftLeg2", "armorBipedRightLeg2" -> EquipmentSlot.LEGS;
		case "armorBipedRightArm" -> !currentEntity.isLeftHanded() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
		case "armorBipedLeftArm" -> currentEntity.isLeftHanded() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
		case "armorBipedBody" -> EquipmentSlot.CHEST;
		case "armorBipedHead" -> EquipmentSlot.HEAD;
		default -> null;
		};
	}

	@Override
	protected ModelPart getArmorPartForBone(final String name, final BipedEntityModel<?> armorModel) {
		return switch (name) {
		case "armorBipedLeftFoot", "armorBipedLeftLeg", "armorBipedLeftFoot2", "armorBipedLeftLeg2" -> armorModel.leftLeg;
		case "armorBipedRightFoot", "armorBipedRightLeg", "armorBipedRightFoot2", "armorBipedRightLeg2" -> armorModel.rightLeg;
		case "armorBipedRightArm" -> armorModel.rightArm;
		case "armorBipedLeftArm" -> armorModel.leftArm;
		case "armorBipedBody" -> armorModel.body;
		case "armorBipedHead" -> armorModel.head;
		default -> null;
		};
	}

	@Override
	protected BlockState getHeldBlockForBone(final String boneName, final T currentEntity) {
		return null;
	}

	@Override
	protected void preRenderBlock(final MatrixStack stack, final BlockState block, final String boneName,
			final T currentEntity) {

	}

	@Override
	protected void postRenderBlock(final MatrixStack stack, final BlockState block, final String boneName,
			final T currentEntity) {
	}

	protected final Identifier CAPE_TEXTURE = new Identifier(GeckoLib.ModID,
			"textures/entity/extendedrendererentity_cape.png");

	@Override
	protected Identifier getTextureForBone(final String boneName, final T currentEntity) {
		return switch (boneName) {
		case "bipedCape" -> this.CAPE_TEXTURE;
		default -> null;
		};
	}

	@Override
	protected boolean isArmorBone(final GeoBone bone) {
		return bone.getName().startsWith("armor");
	}
}