package net.denanu.amazia.block;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.block.custom.MineMarkerBlock;
import net.denanu.amazia.block.custom.VillageCoreBlock;
import net.denanu.amazia.item.AmaziaItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;

public class AmaziaBlocks {
	public static final Block RUBY_BLOCK = registerBlock("ruby_block",
            new Block(FabricBlockSettings.of(Material.METAL).strength(2f)
            		), AmaziaItemGroup.VILLAGE);

    public static final Block RUBY_ORE = registerBlock("ruby_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(1f),
                    UniformIntProvider.create(3, 7)), AmaziaItemGroup.VILLAGE);
    public static final Block DEEPSLATE_RUBY_ORE = registerBlock("deepslate_ruby_ore",
            new OreBlock(FabricBlockSettings.of(Material.STONE).strength(1f),
                    UniformIntProvider.create(3, 7)), AmaziaItemGroup.VILLAGE);
    
    
    public static final Block VILLAGE_CORE = registerBlock("village_core",
    		new VillageCoreBlock(FabricBlockSettings.of(Material.METAL).strength(5f).nonOpaque()
    				), AmaziaItemGroup.VILLAGE);
    
    public static final Block MINE_MARKER = registerBlock("mine_marker",
    		new MineMarkerBlock(FabricBlockSettings.of(Material.STONE).strength(1f).nonOpaque()
    				), AmaziaItemGroup.VILLAGE);
    
    
	@SuppressWarnings("unused")
	private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(Amazia.MOD_ID, name), block);
    }

    private static Block registerBlock(String name, Block block, ItemGroup tab) {
        registerBlockItem(name, block, tab);
        return Registry.register(Registry.BLOCK, new Identifier(Amazia.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup tab) {
        return Registry.register(Registry.ITEM, new Identifier(Amazia.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(tab)));
    }

    public static void registerModBlocks() {
    	Amazia.LOGGER.debug("Registering ModBlocks for " + Amazia.MOD_ID);
    }
}
