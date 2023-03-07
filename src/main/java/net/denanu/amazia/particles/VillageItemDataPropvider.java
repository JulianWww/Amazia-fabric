package net.denanu.amazia.particles;

import net.minecraft.item.Item;
import net.minecraft.particle.DefaultParticleType;

public class VillageItemDataPropvider {
	public final Item itm;
	public final DefaultParticleType particles;

	VillageItemDataPropvider(final Item itm, final DefaultParticleType particles) {
		this.itm = itm;
		this.particles = particles;
	}

	public static VillageItemDataPropvider of(final Item itm, final DefaultParticleType particles) {
		return new VillageItemDataPropvider(itm, particles);
	}


	@Override
	public boolean equals(final Object other) {
		return this.itm.equals(other);
	}

}
