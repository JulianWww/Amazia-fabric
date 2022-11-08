package net.denanu.amazia.utils.registry;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

public class AmaziaRegistrys {
	public static final AmaziaListRegistry<Item> ANIMAL_FEED_ITEMS = new AmaziaListRegistry<Item>();
	
	public static void setup() {
		setupFeedItems();
	}
	
	public static void setupFeedItems() {
		addFeedItems(EntityType.COW, Items.WHEAT);
		addFeedItems(EntityType.PIG, Items.POTATO);
		addFeedItems(EntityType.PIG, Items.CARROT);
		addFeedItems(EntityType.PIG, Items.BEETROOT);
		addFeedItems(EntityType.SHEEP, Items.WHEAT);
		addFeedItems(EntityType.CHICKEN, Items.WHEAT_SEEDS);
		addFeedItems(EntityType.CHICKEN, Items.MELON_SEEDS);
		addFeedItems(EntityType.CHICKEN, Items.PUMPKIN_SEEDS);
		addFeedItems(EntityType.CHICKEN, Items.BEETROOT_SEEDS);
	}
	
	public static void addFeedItems(EntityType<?> animal, Item itm) {
		ANIMAL_FEED_ITEMS.add(
				Registry.ENTITY_TYPE.getId(animal), 
				itm
			);
	}
}
