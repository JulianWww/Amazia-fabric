package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.DruidEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class DruidModel extends AnimatedGeoModel<DruidEntity> {
	@Override
	public Identifier getModelResource(final DruidEntity object) {
		return new Identifier(Amazia.MOD_ID, "geo/farmer.geo.json");
	}

	@Override
	public Identifier getTextureResource(final DruidEntity object) {
		return new Identifier(Amazia.MOD_ID, "textures/entity/farmer_tex.png");
	}

	@Override
	public Identifier getAnimationResource(final DruidEntity animatable) {
		return new Identifier(Amazia.MOD_ID, "animations/farmer.anim.json");
	}
}