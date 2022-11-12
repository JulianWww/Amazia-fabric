package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.EnchanterEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EnchanterModel extends AnimatedGeoModel<EnchanterEntity> {
    @Override
    public Identifier getModelResource(EnchanterEntity object) {
        return new Identifier(Amazia.MOD_ID, "geo/farmer.geo.json");
    }

    @Override
    public Identifier getTextureResource(EnchanterEntity object) {
        return new Identifier(Amazia.MOD_ID, "textures/entity/farmer_tex.png");
    }

    @Override
    public Identifier getAnimationResource(EnchanterEntity animatable) {
        return new Identifier(Amazia.MOD_ID, "animations/farmer.anim.json");
    }
}