package net.denanu.amazia;

import net.denanu.amazia.block.AmaziaBlocks;
import net.denanu.amazia.block.entity.AmaziaBlockEntities;
import net.denanu.amazia.entities.AmaziaEntities;
import net.denanu.amazia.item.AmaziaItems;
import net.fabricmc.api.ModInitializer;
import software.bernie.geckolib3.GeckoLib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Amazia implements ModInitializer {
	public static final String MOD_ID = "amazia";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		AmaziaItems.registerModItems();
		AmaziaBlocks.registerModBlocks();
		
		AmaziaEntities.registerAttributes();
		
		GeckoLib.initialize();
		
		AmaziaBlockEntities.registerBlockEntities();
	}
}
