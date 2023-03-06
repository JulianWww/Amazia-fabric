package net.denanu.amazia.entities.merchants.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.merchants.AmaziaMerchant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class MerchantModel extends AnimatedGeoModel<AmaziaMerchant> {
	@Override
	public Identifier getModelResource(final AmaziaMerchant object) {
		return new Identifier(Amazia.MOD_ID, "geo/farmer.geo.json");
	}

	@Override
	public Identifier getTextureResource(final AmaziaMerchant object) {
		return new Identifier(Amazia.MOD_ID, "textures/entity/farmer_tex.png");
	}

	@Override
	public Identifier getAnimationResource(final AmaziaMerchant animatable) {
		return new Identifier(Amazia.MOD_ID, "animations/farmer.anim.json");
	}
}