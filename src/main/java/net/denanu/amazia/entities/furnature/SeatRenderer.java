package net.denanu.amazia.entities.furnature;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class SeatRenderer extends EntityRenderer<SeatEntity> {
	public SeatRenderer(final Context ctx) {
		super(ctx);
	}

	@Override
	public Identifier getTexture(final SeatEntity var1) {
		return null;
	}

}