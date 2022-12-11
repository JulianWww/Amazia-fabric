package net.denanu.amazia.item;

import net.denanu.amazia.Amazia;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
public class AmaziaItems {
	 public static final Item RUBY = registerItem("ruby",
	            new Item(new FabricItemSettings().group(AmaziaItemGroup.VILLAGE)));

	private static Item registerItem(String name, Item item) {
		 return Registry.register(Registry.ITEM, new Identifier(Amazia.MOD_ID, name), item);
	}

	public static void registerModItems() {
		Amazia.LOGGER.debug("Registering Mod Items for " + Amazia.MOD_ID);
	}
}
