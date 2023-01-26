package net.denanu.amazia.block;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.block.custom.AmaziaTroughBlock;
import net.denanu.amazia.block.custom.TeachersDeskBlock;
import net.denanu.amazia.block.custom.VillageCoreBlock;
import net.denanu.amazia.block.custom.VillageMarkerBlock;
import net.denanu.amazia.item.AmaziaItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;

public class AmaziaBlocks {
	public static final Block RUBY_BLOCK = AmaziaBlocks.registerBlock("ruby_block",
			new Block(FabricBlockSettings.of(Material.METAL).strength(2f)
					), AmaziaItemGroup.VILLAGE);

	public static final Block RUBY_ORE = AmaziaBlocks.registerBlock("ruby_ore",
			new OreBlock(FabricBlockSettings.of(Material.STONE).strength(1f),
					UniformIntProvider.create(3, 7)), AmaziaItemGroup.VILLAGE);
	public static final Block DEEPSLATE_RUBY_ORE = AmaziaBlocks.registerBlock("deepslate_ruby_ore",
			new OreBlock(FabricBlockSettings.of(Material.STONE).strength(1f),
					UniformIntProvider.create(3, 7)), AmaziaItemGroup.VILLAGE);


	public static final Block VILLAGE_CORE = AmaziaBlocks.registerBlock("village_core",
			new VillageCoreBlock(FabricBlockSettings.of(Material.METAL).strength(5f).nonOpaque()
					), AmaziaItemGroup.VILLAGE);

	public static final Block TEACHER_LECTERN = AmaziaBlocks.registerBlock("teacher_lectern",
			new TeachersDeskBlock(FabricBlockSettings.of(Material.METAL).strength(5f).nonOpaque()), AmaziaItemGroup.VILLAGE);

	public static final Block MINE_MARKER = AmaziaBlocks.registerBlock("mine_marker",
			new VillageMarkerBlock(FabricBlockSettings.of(Material.STONE).strength(1f).nonOpaque()
					), AmaziaItemGroup.VILLAGE);

	public static final Block TREE_FARM_MARKER = AmaziaBlocks.registerBlock("tree_school",
			new Block(FabricBlockSettings.of(Material.STONE).strength(1f).nonOpaque()
					), AmaziaItemGroup.VILLAGE);

	public static final Block SHEEP_TROUGH = AmaziaBlocks.registerBlock("sheep_trough",
			new AmaziaTroughBlock(AbstractBlock.Settings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0f).nonOpaque(), EntityType.SHEEP
					), AmaziaItemGroup.VILLAGE);

	public static final Block COW_TROUGH = AmaziaBlocks.registerBlock("cow_trough",
			new AmaziaTroughBlock(AbstractBlock.Settings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0f).nonOpaque(), EntityType.COW
					), AmaziaItemGroup.VILLAGE);

	public static final Block CHICKEN_TROUGH = AmaziaBlocks.registerBlock("chicken_trough",
			new AmaziaTroughBlock(AbstractBlock.Settings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0f).nonOpaque(), EntityType.CHICKEN
					), AmaziaItemGroup.VILLAGE);

	public static final Block PIG_TROUGH = AmaziaBlocks.registerBlock("pig_trough",
			new AmaziaTroughBlock(AbstractBlock.Settings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0f).nonOpaque(), EntityType.PIG
					), AmaziaItemGroup.VILLAGE);


	private static Block registerBlock(final String name, final Block block, final ItemGroup tab) {
		AmaziaBlocks.registerBlockItem(name, block, tab);
		return Registry.register(Registry.BLOCK, new Identifier(Amazia.MOD_ID, name), block);
	}

	private static Item registerBlockItem(final String name, final Block block, final ItemGroup tab) {
		return Registry.register(Registry.ITEM, new Identifier(Amazia.MOD_ID, name),
				new BlockItem(block, new FabricItemSettings().group(tab)));
	}

	public static void registerModBlocks() {
		Amazia.LOGGER.debug("Registering ModBlocks for " + Amazia.MOD_ID);
	}
}
