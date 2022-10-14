package net.denanu.amazia.block.entity;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.block.AmaziaBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmaziaBlockEntities {
	public static BlockEntityType<VillageCoreBlockEntity> VILLAGE_CORE;
	
	public static void registerBlockEntities() {
		VILLAGE_CORE = Registry.register(Registry.BLOCK_ENTITY_TYPE, 
				new Identifier(Amazia.MOD_ID, "village_core"), 
				FabricBlockEntityTypeBuilder.create(VillageCoreBlockEntity::new, 
						AmaziaBlocks.VILLAGE_CORE).build(null)
			);
	}
}
