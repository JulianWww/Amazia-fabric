package net.denanu.amazia.particles;

import java.util.HashMap;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.DefaultParticleType;

public class AmaziaItemParticleMap {
	private static final HashMap<Item, DefaultParticleType> itemParticleMap = new HashMap<>();

	@Nullable
	public static DefaultParticleType get(final Item itm) {
		return AmaziaItemParticleMap.itemParticleMap.get(itm);
	}

	public static void setup() {
		AmaziaItemParticleMap.itemParticleMap.clear();
		// PYTHON GENERATOR BEGIN
		AmaziaItemParticleMap.itemParticleMap.put(Items.WOODEN_AXE, AmaziaParticles.AXE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.STONE_AXE, AmaziaParticles.AXE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.IRON_AXE, AmaziaParticles.AXE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.GOLDEN_AXE, AmaziaParticles.AXE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.DIAMOND_AXE, AmaziaParticles.AXE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.NETHERITE_AXE, AmaziaParticles.AXE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.BEETROOT, AmaziaParticles.BEETROOT);
		AmaziaItemParticleMap.itemParticleMap.put(Items.BEETROOT_SEEDS, AmaziaParticles.BEETROOT_SEEDS);
		AmaziaItemParticleMap.itemParticleMap.put(Items.CARROT, AmaziaParticles.CARROT);
		AmaziaItemParticleMap.itemParticleMap.put(Items.COAL, AmaziaParticles.COAL);
		AmaziaItemParticleMap.itemParticleMap.put(Items.CHARCOAL, AmaziaParticles.COAL);
		AmaziaItemParticleMap.itemParticleMap.put(Items.WOODEN_HOE, AmaziaParticles.HOE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.STONE_HOE, AmaziaParticles.HOE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.IRON_HOE, AmaziaParticles.HOE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.GOLDEN_HOE, AmaziaParticles.HOE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.DIAMOND_HOE, AmaziaParticles.HOE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.NETHERITE_HOE, AmaziaParticles.HOE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.LAPIS_LAZULI, AmaziaParticles.LAPIS_LAZULI);
		AmaziaItemParticleMap.itemParticleMap.put(Items.WOODEN_PICKAXE, AmaziaParticles.PICKAXE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.STONE_PICKAXE, AmaziaParticles.PICKAXE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.IRON_PICKAXE, AmaziaParticles.PICKAXE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.GOLDEN_PICKAXE, AmaziaParticles.PICKAXE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.DIAMOND_PICKAXE, AmaziaParticles.PICKAXE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.NETHERITE_PICKAXE, AmaziaParticles.PICKAXE);
		AmaziaItemParticleMap.itemParticleMap.put(Items.POTATO, AmaziaParticles.POTATO);
		AmaziaItemParticleMap.itemParticleMap.put(Items.OAK_SAPLING, AmaziaParticles.SAPLING);
		AmaziaItemParticleMap.itemParticleMap.put(Items.SPRUCE_SAPLING, AmaziaParticles.SAPLING);
		AmaziaItemParticleMap.itemParticleMap.put(Items.BIRCH_SAPLING, AmaziaParticles.SAPLING);
		AmaziaItemParticleMap.itemParticleMap.put(Items.JUNGLE_SAPLING, AmaziaParticles.SAPLING);
		AmaziaItemParticleMap.itemParticleMap.put(Items.ACACIA_SAPLING, AmaziaParticles.SAPLING);
		AmaziaItemParticleMap.itemParticleMap.put(Items.DARK_OAK_SAPLING, AmaziaParticles.SAPLING);
		AmaziaItemParticleMap.itemParticleMap.put(Items.WOODEN_SWORD, AmaziaParticles.SWORD);
		AmaziaItemParticleMap.itemParticleMap.put(Items.STONE_SWORD, AmaziaParticles.SWORD);
		AmaziaItemParticleMap.itemParticleMap.put(Items.IRON_SWORD, AmaziaParticles.SWORD);
		AmaziaItemParticleMap.itemParticleMap.put(Items.GOLDEN_SWORD, AmaziaParticles.SWORD);
		AmaziaItemParticleMap.itemParticleMap.put(Items.DIAMOND_SWORD, AmaziaParticles.SWORD);
		AmaziaItemParticleMap.itemParticleMap.put(Items.NETHERITE_SWORD, AmaziaParticles.SWORD);
		AmaziaItemParticleMap.itemParticleMap.put(Items.TORCH, AmaziaParticles.TORCH);
		AmaziaItemParticleMap.itemParticleMap.put(Items.WHEAT, AmaziaParticles.WHEAT);
		AmaziaItemParticleMap.itemParticleMap.put(Items.WHEAT_SEEDS, AmaziaParticles.WHEAT_SEEDS);
		// PYTHON GENERATOR END
	}

}
