package net.denanu.amazia.economy.offerModifiers.finalizers;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.utils.scanners.ChunkScanner;
import net.minecraft.item.map.MapIcon;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.StructureKeys;

public class AmaziaFinalModifiers {
	public static MapFinalModifer EXPLORER_MAP_BUILDER = new MapFinalModifer(MapIcon.Type.RED_X, "Explorer Map", Identifier.of(Amazia.MOD_ID, "explorer_map_finalzier"))
			.add(StructureKeys.BURIED_TREASURE, 10.0f);
	
	
	public static void setup() {
		ChunkScanner.toLookFor.add(StructureKeys.BURIED_TREASURE.getValue());
	}
}
