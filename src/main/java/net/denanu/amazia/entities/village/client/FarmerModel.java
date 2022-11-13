package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.FarmerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FarmerModel extends AnimatedGeoModel<FarmerEntity> {
    @Override
    public Identifier getModelResource(FarmerEntity object) {
        return new Identifier(Amazia.MOD_ID, "geo/farmer.geo.json");
    }

    @Override
    public Identifier getTextureResource(FarmerEntity object) {
        return new Identifier(Amazia.MOD_ID, "textures/entity/farmer_tex.png");
    }

    @Override
    public Identifier getAnimationResource(FarmerEntity animatable) {
        return new Identifier(Amazia.MOD_ID, "animations/farmer.anim.json");
    }
}