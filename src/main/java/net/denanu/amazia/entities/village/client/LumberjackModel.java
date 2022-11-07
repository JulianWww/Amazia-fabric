package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.FarmerEntity;
import net.denanu.amazia.entities.village.server.LumberjackEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LumberjackModel extends AnimatedGeoModel<LumberjackEntity> {
    @Override
    public Identifier getModelResource(LumberjackEntity object) {
        return new Identifier(Amazia.MOD_ID, "models/entity/lumberjack.geo.json");
    }

    @Override
    public Identifier getTextureResource(LumberjackEntity object) {
        return new Identifier(Amazia.MOD_ID, "textures/entity/farmer_tex.png");
    }

    @Override
    public Identifier getAnimationResource(LumberjackEntity animatable) {
        return new Identifier(Amazia.MOD_ID, "animations/farmer.anim.json");
    }
}
