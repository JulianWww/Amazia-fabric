package net.denanu.amazia.economy.offerModifiers.finalizers;

import net.denanu.amazia.Amazia;
import net.minecraft.item.map.MapIcon;
import net.minecraft.tag.StructureTags;
import net.minecraft.util.Identifier;

public class AmaziaFinalModifiers {
	public static MapFinalModifer EXPLORER_MAP_BUILDER = new MapFinalModifer(MapIcon.Type.RED_X, "Explorer Map", Identifier.of(Amazia.MOD_ID, "explorer_map_finalzier"))
			.add(StructureTags.ON_TREASURE_MAPS, 10.0f);
	
	
	public static void setup() {
		// dummy function to force java to load this class
	}
}
