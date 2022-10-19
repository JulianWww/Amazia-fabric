package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.FarmerEntity;
import net.denanu.amazia.entities.village.server.MinerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MinerModel extends AnimatedGeoModel<MinerEntity> {
    @Override
    public Identifier getModelResource(MinerEntity object) {
        return new Identifier(Amazia.MOD_ID, "geo/farmer.geo.json");
    }

    @Override
    public Identifier getTextureResource(MinerEntity object) {
        return new Identifier(Amazia.MOD_ID, "textures/entity/farmer_tex.png");
    }

    @Override
    public Identifier getAnimationResource(MinerEntity animatable) {
        return new Identifier(Amazia.MOD_ID, "animations/farmer.anim.json");
    }
}