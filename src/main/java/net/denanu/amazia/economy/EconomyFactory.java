package net.denanu.amazia.economy;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.economy.offerModifiers.item.EnchantmentModifier;
import net.denanu.amazia.utils.random.ConstantValue;
import net.denanu.amazia.utils.random.ConstrainedGaussianRandom;
import net.denanu.amazia.utils.random.LinearRandom;
import net.denanu.amazia.utils.random.RandomnessFactory;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class EconomyFactory {
	public final static EnchantmentModifier HELMET_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.8f)
			.add(Enchantments.FIRE_PROTECTION, 			1.0f)
			.add(Enchantments.PROJECTILE_PROTECTION, 	1.0f)
			.add(Enchantments.BLAST_PROTECTION,			1.0f)
			.add(Enchantments.PROTECTION,				1.0f)
			.add(Enchantments.UNBREAKING, 				1.0f)
			.add(Enchantments.RESPIRATION, 				1.0f)
			.add(Enchantments.AQUA_AFFINITY,			1.0f)
			.add(Enchantments.THORNS, 					1.0f)
			.add(Enchantments.MENDING, 					0.001f)
			.add(Enchantments.BINDING_CURSE, 			0.2f)
			.add(Enchantments.VANISHING_CURSE, 			0.2f);
	
	public final static EnchantmentModifier CHESTPLATE_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.8f)
			.add(Enchantments.FIRE_PROTECTION, 			1.0f)
			.add(Enchantments.PROJECTILE_PROTECTION, 	1.0f)
			.add(Enchantments.BLAST_PROTECTION,			1.0f)
			.add(Enchantments.PROTECTION,				1.0f)
			.add(Enchantments.UNBREAKING, 				1.0f)
			.add(Enchantments.THORNS, 					1.0f)
			.add(Enchantments.MENDING, 					0.001f)
			.add(Enchantments.BINDING_CURSE, 			0.2f)
			.add(Enchantments.VANISHING_CURSE, 			0.2f);
	
	public final static EnchantmentModifier LEGGINS_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.8f)
			.add(Enchantments.FIRE_PROTECTION, 			1.0f)
			.add(Enchantments.PROJECTILE_PROTECTION, 	1.0f)
			.add(Enchantments.BLAST_PROTECTION,			1.0f)
			.add(Enchantments.PROTECTION,				1.0f)
			.add(Enchantments.UNBREAKING, 				1.0f)
			.add(Enchantments.THORNS, 					1.0f)
			.add(Enchantments.MENDING, 					0.001f)
			.add(Enchantments.SWIFT_SNEAK, 				1.0f)
			.add(Enchantments.BINDING_CURSE, 			0.2f)
			.add(Enchantments.VANISHING_CURSE, 			0.2f);
	
	public final static EnchantmentModifier BOOTS_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.8f)
			.add(Enchantments.FIRE_PROTECTION, 			1.0f)
			.add(Enchantments.PROJECTILE_PROTECTION, 	1.0f)
			.add(Enchantments.BLAST_PROTECTION,			1.0f)
			.add(Enchantments.PROTECTION,				1.0f)
			.add(Enchantments.FEATHER_FALLING,			1.0f)
			.add(Enchantments.UNBREAKING, 				1.0f)
			.add(Enchantments.THORNS, 					1.0f)
			.add(Enchantments.DEPTH_STRIDER, 			1.0f)
			.add(Enchantments.SOUL_SPEED, 				1.0f)
			.add(Enchantments.FROST_WALKER, 			1.0f)
			.add(Enchantments.MENDING, 					0.001f)
			.add(Enchantments.BINDING_CURSE, 			0.2f)
			.add(Enchantments.VANISHING_CURSE, 			0.2f);
	
	public final static EnchantmentModifier SHIELD_ENCHANTMENT_MODIFIER = new EnchantmentModifier(0.3f, 0.5f)
			.add(Enchantments.UNBREAKING,				1.0f)
			.add(Enchantments.MENDING, 					0.001f)
			.add(Enchantments.VANISHING_CURSE, 			0.2f);
			
	
	public static void setup() {         // value       volatility  return rate | stackSize generator							professions
		register(Items.COAL, 					0.0666f, 	0.0001f, 	0.01f,	new ConstrainedGaussianRandom(20f, 8f, 64, 1), 	ImmutableSet.of("armorer"));
		register(Items.IRON_HELMET, 			5, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(HELMET_ENCHANTMENT_MODIFIER);
		register(Items.IRON_CHESTPLATE, 		9, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(CHESTPLATE_ENCHANTMENT_MODIFIER);
		register(Items.IRON_LEGGINGS, 			7, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(LEGGINS_ENCHANTMENT_MODIFIER);
		register(Items.IRON_BOOTS, 				4, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(BOOTS_ENCHANTMENT_MODIFIER);
		register(Items.IRON_INGOT, 				0.25f,		0.001f, 	0.01f, 	new LinearRandom(1, 64), 						ImmutableSet.of("armorer"));
		register(Items.BELL, 					36, 		2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer"));
		register(Items.LAVA_BUCKET, 			1, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer"));
		register(Items.DIAMOND, 				10,			0.01f, 		0.001f, new ConstrainedGaussianRandom(16f, 4f, 32, 1), 	ImmutableSet.of("armorer"));
		register(Items.CHAINMAIL_HELMET,		1, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(HELMET_ENCHANTMENT_MODIFIER);
		register(Items.CHAINMAIL_CHESTPLATE,	4, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(CHESTPLATE_ENCHANTMENT_MODIFIER);
		register(Items.CHAINMAIL_LEGGINGS, 		3, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(LEGGINS_ENCHANTMENT_MODIFIER);
		register(Items.CHAINMAIL_BOOTS, 		1, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(BOOTS_ENCHANTMENT_MODIFIER);
		register(Items.SHIELD, 					5, 			2f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(SHIELD_ENCHANTMENT_MODIFIER);
		register(Items.DIAMOND_HELMET, 			27, 		4f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(HELMET_ENCHANTMENT_MODIFIER);
		register(Items.DIAMOND_CHESTPLATE, 		35, 		4f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(CHESTPLATE_ENCHANTMENT_MODIFIER);
		register(Items.DIAMOND_LEGGINGS, 		33, 		4f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(LEGGINS_ENCHANTMENT_MODIFIER);
		register(Items.DIAMOND_BOOTS, 			27, 		4f, 		0.001f, new ConstantValue<Integer>(1), 					ImmutableSet.of("armorer")).modify(BOOTS_ENCHANTMENT_MODIFIER);
	}
	
	private static ItemEconomyFactory register(Item itm, float baseValue, float volatility, float returnRate, RandomnessFactory<Integer> stackSizeFactory, Collection<String> professions) {
		return register(Amazia.MOD_ID, itm, baseValue, volatility, returnRate, stackSizeFactory, professions);
	}
	
	public static ItemEconomyFactory register(String modid, Item itm, float baseValue, float volatility, float returnRate, RandomnessFactory<Integer> stackSizeFactory, Collection<String> professions) {
		ItemEconomyFactory factory = new ItemEconomyFactory(itm, baseValue, volatility, returnRate, stackSizeFactory);
		Economy.addItem(factory);
		
		for (String profession : professions) {
			Economy.addProfessionItem(ProfessionFactory.buildProfession(modid, profession), itm.getTranslationKey());
		}
		return factory;
	}
}
