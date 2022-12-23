package net.denanu.amazia.block.custom;

import net.denanu.amazia.block.custom.api.AmaziaTrough;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Identifier;

public class AmaziaTroughBlock extends Block implements AmaziaTrough {
	private EntityType<? extends AnimalEntity> entityType;

	public AmaziaTroughBlock(Settings settings, EntityType<? extends AnimalEntity> entityType) {
		super(settings);
		this.entityType = entityType;
	}

	@Override
	public Identifier getEntityType() {
		return EntityType.getId(this.entityType);
	}

}
