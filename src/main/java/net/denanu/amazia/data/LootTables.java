package net.denanu.amazia.data;

import com.google.common.collect.ImmutableList;

import net.denanu.amazia.item.AmaziaItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;

public final class LootTables {
	private static final ImmutableList<Identifier> INTELLIGENCE_SPOTS = ImmutableList.of(
			new Identifier("chests/bastion_treasure"),
			new Identifier("chests/ancient_city"),
			new Identifier("chests/end_city_treasure"),
			new Identifier("chests/spawn_bonus_chest")
			);

	public static void setup() {
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (source.isBuiltin() && LootTables.INTELLIGENCE_SPOTS.contains(id)) {
				final LootPool.Builder poolBuilder = LootPool.builder()
						.with(ItemEntry.builder(AmaziaItems.BOOK_OF_INTELLIGENCE));

				tableBuilder.pool(poolBuilder);
			}
		});
	}
}
