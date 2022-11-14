package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.BlacksmithEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BlacksmithModel extends AnimatedGeoModel<BlacksmithEntity> {
    @Override
    public Identifier getModelResource(BlacksmithEntity object) {
        return new Identifier(Amazia.MOD_ID, "geo/farmer.geo.json");
    }

    @Override
    public Identifier getTextureResource(BlacksmithEntity object) {
        return new Identifier(Amazia.MOD_ID, "textures/entity/farmer_tex.png");
    }

    @Override
    public Identifier getAnimationResource(BlacksmithEntity animatable) {
        return new Identifier(Amazia.MOD_ID, "animations/farmer.anim.json");
    }
}