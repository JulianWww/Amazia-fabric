package net.denanu.amazia.entities.village.client;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class AmaziaModel<T extends AmaziaVillagerEntity & IAnimatable> extends AnimatedGeoModel<T> {
	private final Identifier ANIM, MODEL;
	private static final Identifier TEX = Identifier.of(Amazia.MOD_ID, "textures/entity/villagers/villager.png");

	public AmaziaModel(final String name) {
		this(Amazia.MOD_ID, name);
	}

	public AmaziaModel(final String MODID, final String name) {
		this.ANIM = Identifier.of(MODID, "animations/" + name + ".animation.json");
		this.MODEL = Identifier.of(MODID, "geo/" + name + ".geo.json");
	}

	@Override
	public Identifier getAnimationResource(final T animatable) {
		return this.ANIM;
	}

	@Override
	public Identifier getModelResource(final T object) {
		return this.MODEL;
	}

	@Override
	public Identifier getTextureResource(final T object) {
		return AmaziaModel.TEX;
	}

}
