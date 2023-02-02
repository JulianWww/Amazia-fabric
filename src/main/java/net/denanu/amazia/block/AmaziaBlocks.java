package net.denanu.amazia.block;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.block.custom.AmaziaTroughBlock;
import net.denanu.amazia.block.custom.ChairBlock;
import net.denanu.amazia.block.custom.TeachersDeskBlock;
import net.denanu.amazia.block.custom.TeachersDeskBlockCabinet;
import net.denanu.amazia.block.custom.VillageCoreBlock;
import net.denanu.amazia.block.custom.VillageMarkerBlock;
import net.denanu.amazia.item.AmaziaItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
					));

	public static final Block RUBY_ORE = AmaziaBlocks.registerBlock("ruby_ore",
			new OreBlock(FabricBlockSettings.of(Material.STONE).strength(1f),
					UniformIntProvider.create(3, 7)));
	public static final Block DEEPSLATE_RUBY_ORE = AmaziaBlocks.registerBlock("deepslate_ruby_ore",
			new OreBlock(FabricBlockSettings.of(Material.STONE).strength(1f),
					UniformIntProvider.create(3, 7)));


	public static final Block VILLAGE_CORE = AmaziaBlocks.registerBlock("village_core",
			new VillageCoreBlock(FabricBlockSettings.of(Material.METAL).strength(5f).nonOpaque()
					));

	public static final Block DESK_OAK 					= AmaziaBlocks.registerBlock("oak_desk", 				new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.OAK_PLANKS)));
	public static final Block DESK_SPRUCE				= AmaziaBlocks.registerBlock("spruce_desk", 			new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block DESK_BIRCH 				= AmaziaBlocks.registerBlock("birch_desk", 				new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.BIRCH_PLANKS)));
	public static final Block DESK_JUNGLE 				= AmaziaBlocks.registerBlock("jungle_desk", 			new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block DESK_ACACIA 				= AmaziaBlocks.registerBlock("acacia_desk", 			new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.ACACIA_PLANKS)));
	public static final Block DESK_DARK_OAK 			= AmaziaBlocks.registerBlock("dark_oak_desk", 			new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block DESK_CRIMSON 				= AmaziaBlocks.registerBlock("crimson_desk", 			new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.CRIMSON_PLANKS)));
	public static final Block DESK_WARPED 				= AmaziaBlocks.registerBlock("warped_desk", 			new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.WARPED_PLANKS)));
	public static final Block DESK_MANGROVE 			= AmaziaBlocks.registerBlock("mangrove_desk", 			new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.MANGROVE_PLANKS)));
	public static final Block DESK_STRIPPED_OAK 		= AmaziaBlocks.registerBlock("stripped_oak_desk", 		new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.OAK_PLANKS)));
	public static final Block DESK_STRIPPED_SPRUCE 		= AmaziaBlocks.registerBlock("stripped_spruce_desk", 	new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block DESK_STRIPPED_BIRCH 		= AmaziaBlocks.registerBlock("stripped_birch_desk", 	new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.BIRCH_PLANKS)));
	public static final Block DESK_STRIPPED_JUNGLE 		= AmaziaBlocks.registerBlock("stripped_jungle_desk", 	new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block DESK_STRIPPED_ACACIA 		= AmaziaBlocks.registerBlock("stripped_acacia_desk", 	new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.ACACIA_PLANKS)));
	public static final Block DESK_STRIPPED_DARK_OAK 	= AmaziaBlocks.registerBlock("stripped_dark_oak_desk", 	new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block DESK_STRIPPED_CRIMSON 	= AmaziaBlocks.registerBlock("stripped_crimson_desk", 	new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.CRIMSON_PLANKS)));
	public static final Block DESK_STRIPPED_WARPED 		= AmaziaBlocks.registerBlock("stripped_warped_desk", 	new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.WARPED_PLANKS)));
	public static final Block DESK_STRIPPED_MANGROVE 	= AmaziaBlocks.registerBlock("stripped_mangrove_desk", 	new TeachersDeskBlock(FabricBlockSettings.copy(Blocks.MANGROVE_PLANKS)));

	public static final Block DESK_OAK_CABINET 					= AmaziaBlocks.registerBlock("oak_desk_cabinet", 				new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.OAK_PLANKS)));
	public static final Block DESK_SPRUCE_CABINET				= AmaziaBlocks.registerBlock("spruce_desk_cabinet", 			new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block DESK_BIRCH_CABINET 				= AmaziaBlocks.registerBlock("birch_desk_cabinet", 				new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.BIRCH_PLANKS)));
	public static final Block DESK_JUNGLE_CABINET 				= AmaziaBlocks.registerBlock("jungle_desk_cabinet", 			new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block DESK_ACACIA_CABINET 				= AmaziaBlocks.registerBlock("acacia_desk_cabinet", 			new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.ACACIA_PLANKS)));
	public static final Block DESK_DARK_OAK_CABINET 			= AmaziaBlocks.registerBlock("dark_oak_desk_cabinet", 			new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block DESK_CRIMSON_CABINET 				= AmaziaBlocks.registerBlock("crimson_desk_cabinet", 			new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.CRIMSON_PLANKS)));
	public static final Block DESK_WARPED_CABINET 				= AmaziaBlocks.registerBlock("warped_desk_cabinet", 			new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.WARPED_PLANKS)));
	public static final Block DESK_MANGROVE_CABINET 			= AmaziaBlocks.registerBlock("mangrove_desk_cabinet", 			new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.MANGROVE_PLANKS)));
	public static final Block DESK_STRIPPED_OAK_CABINET 		= AmaziaBlocks.registerBlock("stripped_oak_desk_cabinet", 		new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.OAK_PLANKS)));
	public static final Block DESK_STRIPPED_SPRUCE_CABINET 		= AmaziaBlocks.registerBlock("stripped_spruce_desk_cabinet", 	new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block DESK_STRIPPED_BIRCH_CABINET 		= AmaziaBlocks.registerBlock("stripped_birch_desk_cabinet", 	new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.BIRCH_PLANKS)));
	public static final Block DESK_STRIPPED_JUNGLE_CABINET 		= AmaziaBlocks.registerBlock("stripped_jungle_desk_cabinet", 	new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block DESK_STRIPPED_ACACIA_CABINET 		= AmaziaBlocks.registerBlock("stripped_acacia_desk_cabinet", 	new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.ACACIA_PLANKS)));
	public static final Block DESK_STRIPPED_DARK_OAK_CABINET 	= AmaziaBlocks.registerBlock("stripped_dark_oak_desk_cabinet", 	new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block DESK_STRIPPED_CRIMSON_CABINET 	= AmaziaBlocks.registerBlock("stripped_crimson_desk_cabinet", 	new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.CRIMSON_PLANKS)));
	public static final Block DESK_STRIPPED_WARPED_CABINET 		= AmaziaBlocks.registerBlock("stripped_warped_desk_cabinet", 	new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.WARPED_PLANKS)));
	public static final Block DESK_STRIPPED_MANGROVE_CABINET	= AmaziaBlocks.registerBlock("stripped_mangrove_desk_cabinet", 	new TeachersDeskBlockCabinet(FabricBlockSettings.copy(Blocks.MANGROVE_PLANKS)));

	public static final Block CHAIR_OAK = AmaziaBlocks.registerBlock("oak_chair",								new ChairBlock(FabricBlockSettings.copy(Blocks.OAK_PLANKS)));
	public static final Block CHAIR_SPRUCE = AmaziaBlocks.registerBlock("spruce_chair",							new ChairBlock(FabricBlockSettings.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block CHAIR_BIRCH = AmaziaBlocks.registerBlock("birch_chair",							new ChairBlock(FabricBlockSettings.copy(Blocks.BIRCH_PLANKS)));
	public static final Block CHAIR_JUNGLE = AmaziaBlocks.registerBlock("jungle_chair",							new ChairBlock(FabricBlockSettings.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block CHAIR_ACACIA = AmaziaBlocks.registerBlock("acacia_chair",							new ChairBlock(FabricBlockSettings.copy(Blocks.ACACIA_PLANKS)));
	public static final Block CHAIR_DARK_OAK = AmaziaBlocks.registerBlock("dark_oak_chair",						new ChairBlock(FabricBlockSettings.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block CHAIR_CRIMSON = AmaziaBlocks.registerBlock("crimson_chair",						new ChairBlock(FabricBlockSettings.copy(Blocks.CRIMSON_PLANKS)));
	public static final Block CHAIR_WARPED = AmaziaBlocks.registerBlock("warped_chair",							new ChairBlock(FabricBlockSettings.copy(Blocks.WARPED_PLANKS)));
	public static final Block CHAIR_MANGROVE = AmaziaBlocks.registerBlock("mangrove_chair",						new ChairBlock(FabricBlockSettings.copy(Blocks.MANGROVE_PLANKS)));
	public static final Block CHAIR_STRIPPED_OAK = AmaziaBlocks.registerBlock("stripped_oak_chair",				new ChairBlock(FabricBlockSettings.copy(Blocks.OAK_PLANKS)));
	public static final Block CHAIR_STRIPPED_SPRUCE = AmaziaBlocks.registerBlock("stripped_spruce_chair",		new ChairBlock(FabricBlockSettings.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block CHAIR_STRIPPED_BIRCH = AmaziaBlocks.registerBlock("stripped_birch_chair",			new ChairBlock(FabricBlockSettings.copy(Blocks.BIRCH_PLANKS)));
	public static final Block CHAIR_STRIPPED_JUNGLE = AmaziaBlocks.registerBlock("stripped_jungle_chair",		new ChairBlock(FabricBlockSettings.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block CHAIR_STRIPPED_ACACIA = AmaziaBlocks.registerBlock("stripped_acacia_chair",		new ChairBlock(FabricBlockSettings.copy(Blocks.ACACIA_PLANKS)));
	public static final Block CHAIR_STRIPPED_DARK_OAK = AmaziaBlocks.registerBlock("stripped_dark_oak_chair", 	new ChairBlock(FabricBlockSettings.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block CHAIR_STRIPPED_CRIMSON = AmaziaBlocks.registerBlock("stripped_crimson_chair",		new ChairBlock(FabricBlockSettings.copy(Blocks.CRIMSON_PLANKS)));
	public static final Block CHAIR_STRIPPED_WARPED = AmaziaBlocks.registerBlock("stripped_warped_chair",		new ChairBlock(FabricBlockSettings.copy(Blocks.WARPED_PLANKS)));
	public static final Block CHAIR_STRIPPED_MANGROVE = AmaziaBlocks.registerBlock("stripped_mangrove_chair",	new ChairBlock(FabricBlockSettings.copy(Blocks.MANGROVE_PLANKS)));

	public static final Block MINE_MARKER = AmaziaBlocks.registerBlock("mine_marker",
			new VillageMarkerBlock(FabricBlockSettings.of(Material.STONE).strength(1f).nonOpaque()
					));

	public static final Block TREE_FARM_MARKER = AmaziaBlocks.registerBlock("tree_school",
			new Block(FabricBlockSettings.of(Material.STONE).strength(1f).nonOpaque()
					));

	public static final Block SHEEP_TROUGH = AmaziaBlocks.registerBlock("sheep_trough",
			new AmaziaTroughBlock(AbstractBlock.Settings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0f).nonOpaque(), EntityType.SHEEP
					));

	public static final Block COW_TROUGH = AmaziaBlocks.registerBlock("cow_trough",
			new AmaziaTroughBlock(AbstractBlock.Settings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0f).nonOpaque(), EntityType.COW
					));

	public static final Block CHICKEN_TROUGH = AmaziaBlocks.registerBlock("chicken_trough",
			new AmaziaTroughBlock(AbstractBlock.Settings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0f).nonOpaque(), EntityType.CHICKEN
					));

	public static final Block PIG_TROUGH = AmaziaBlocks.registerBlock("pig_trough",
			new AmaziaTroughBlock(AbstractBlock.Settings.of(Material.METAL, MapColor.STONE_GRAY).requiresTool().strength(2.0f).nonOpaque(), EntityType.PIG
					));

	private static Block registerBlock(final String name, final Block block) {
		return AmaziaBlocks.registerBlock(name, block, AmaziaItemGroup.VILLAGE);
	}

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
		AmaziaBlockProperties.setup();
	}
}
