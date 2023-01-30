package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.BardEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BardModel extends AnimatedGeoModel<BardEntity> {
	@Override
	public Identifier getModelResource(final BardEntity object) {
		return new Identifier(Amazia.MOD_ID, "geo/villager.geo.json");
	}

	@Override
	public Identifier getTextureResource(final BardEntity object) {
		return new Identifier(Amazia.MOD_ID, "textures/entity/villagers/villager.png");
	}

	@Override
	public Identifier getAnimationResource(final BardEntity animatable) {
		return new Identifier(Amazia.MOD_ID, "animations/villager.animation.json");
	}
}