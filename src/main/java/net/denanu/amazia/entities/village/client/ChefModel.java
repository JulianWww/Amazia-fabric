package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.ChefEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ChefModel extends AnimatedGeoModel<ChefEntity> {
	@Override
	public Identifier getModelResource(final ChefEntity object) {
		return new Identifier(Amazia.MOD_ID, "geo/farmer.geo.json");
	}

	@Override
	public Identifier getTextureResource(final ChefEntity object) {
		return new Identifier(Amazia.MOD_ID, "textures/entity/farmer_tex.png");
	}

	@Override
	public Identifier getAnimationResource(final ChefEntity animatable) {
		return new Identifier(Amazia.MOD_ID, "animations/farmer.anim.json");
	}
}