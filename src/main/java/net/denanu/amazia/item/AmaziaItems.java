package net.denanu.amazia.item;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.AmaziaEntities;
import net.denanu.amazia.item.custom.IntelligenceBoostingBookItem;
import net.denanu.amazia.item.custom.VillagerTransformationTokenItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
public class AmaziaItems {
	public static final Item RUBY = AmaziaItems.registerItem("ruby",
			new Item(new FabricItemSettings().group(AmaziaItemGroup.VILLAGE)));

	public static final Item BOOK_OF_INTELLIGENCE = AmaziaItems.registerItem("book_of_intelligence",
			new IntelligenceBoostingBookItem(new FabricItemSettings()
					.rarity(Rarity.EPIC)
					.group(AmaziaItemGroup.VILLAGE)
					)
			);

	public static final Item FLUTE = AmaziaItems.registerItem("flute",
			new Item(new FabricItemSettings()
					.rarity(Rarity.UNCOMMON)
					.group(AmaziaItemGroup.VILLAGE)
					)
			);

	private static FabricItemSettings tokenSettings() {
		return new FabricItemSettings()
				.rarity(Rarity.UNCOMMON)
				.group(AmaziaItemGroup.VILLAGE);
	}

	public static final Item BARD_TOKEM = AmaziaItems.registerItem("bard_transformation_token",
			new VillagerTransformationTokenItem<>(AmaziaItems.tokenSettings(),
					AmaziaEntities.BARD
					)
			);

	public static final Item CHEF_TOKEM = AmaziaItems.registerItem("chef_transformation_token",
			new VillagerTransformationTokenItem<>(AmaziaItems.tokenSettings(),
					AmaziaEntities.CHEF
					)
			);

	public static final Item DRUID_TOKEM = AmaziaItems.registerItem("druid_transformation_token",
			new VillagerTransformationTokenItem<>(AmaziaItems.tokenSettings(),
					AmaziaEntities.DRUID
					)
			);

	public static final Item MINER_TOKEM = AmaziaItems.registerItem("miner_transformation_token",
			new VillagerTransformationTokenItem<>(AmaziaItems.tokenSettings(),
					AmaziaEntities.MINER
					)
			);

	public static final Item CLERIC_TOKEM = AmaziaItems.registerItem("cleric_transformation_token",
			new VillagerTransformationTokenItem<>(AmaziaItems.tokenSettings(),
					AmaziaEntities.CLERIC
					)
			);

	public static final Item FARMER_TOKEM = AmaziaItems.registerItem("farmer_transformation_token",
			new VillagerTransformationTokenItem<>(AmaziaItems.tokenSettings(),
					AmaziaEntities.FARMER
					)
			);

	public static final Item RANCHER_TOKEM = AmaziaItems.registerItem("rancher_transformation_token",
			new VillagerTransformationTokenItem<>(AmaziaItems.tokenSettings(),
					AmaziaEntities.RANCHER
					)
			);


	public static final Item TEACHER_TOKEM = AmaziaItems.registerItem("teacher_transformation_token",
			new VillagerTransformationTokenItem<>(AmaziaItems.tokenSettings(),
					AmaziaEntities.TEACHER
					)
			);

	public static final Item ENCHANTER_TOKEM = AmaziaItems.registerItem("enchanter_transformation_token",
			new VillagerTransformationTokenItem<>(AmaziaItems.tokenSettings(),
					AmaziaEntities.ENCHANTER
					)
			);

	public static final Item BLACKSMITH_TOKEM = AmaziaItems.registerItem("blacksmith_transformation_token",
			new VillagerTransformationTokenItem<>(AmaziaItems.tokenSettings(),
					AmaziaEntities.BLACKSMITH
					)
			);

	public static final Item LUMBERJACK_TOKEM = AmaziaItems.registerItem("lumberjack_transformation_token",
			new VillagerTransformationTokenItem<>(AmaziaItems.tokenSettings(),
					AmaziaEntities.LUMBERJACK
					)
			);

	private static Item registerItem(final String name, final Item item) {
		return Registry.register(Registry.ITEM, new Identifier(Amazia.MOD_ID, name), item);
	}

	public static void registerModItems() {
		Amazia.LOGGER.debug("Registering Mod Items for " + Amazia.MOD_ID);
	}
}
