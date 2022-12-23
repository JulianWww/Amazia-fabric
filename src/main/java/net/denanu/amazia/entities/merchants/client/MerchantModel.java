package net.denanu.amazia.entities.merchants.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.merchants.AmaziaMerchant;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MerchantModel extends AnimatedGeoModel<AmaziaMerchant> {
    @Override
    public Identifier getModelResource(AmaziaMerchant object) {
        return new Identifier(Amazia.MOD_ID, "geo/farmer.geo.json");
    }

    @Override
    public Identifier getTextureResource(AmaziaMerchant object) {
        return new Identifier(Amazia.MOD_ID, "textures/entity/farmer_tex.png");
    }

    @Override
    public Identifier getAnimationResource(AmaziaMerchant animatable) {
        return new Identifier(Amazia.MOD_ID, "animations/farmer.anim.json");
    }
}