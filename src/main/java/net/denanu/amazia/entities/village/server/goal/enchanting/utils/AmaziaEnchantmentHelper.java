package net.denanu.amazia.entities.village.server.goal.enchanting.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Weighting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;

public class AmaziaEnchantmentHelper {
	/**
     * Gets all the possible entries for enchanting the {@code stack} at the
     * given {@code power}.
     */
    public static List<EnchantmentLevelEntry> getPossibleEntries(int power, ItemStack stack, boolean treasureAllowed) {
        ArrayList<EnchantmentLevelEntry> list = Lists.newArrayList();
        Item item = stack.getItem();
        boolean bl = stack.isOf(Items.BOOK);
        enchantmentLoop: for (Enchantment enchantment : Registry.ENCHANTMENT) {
            if (enchantment.isTreasure() && !treasureAllowed || !enchantment.isAvailableForRandomSelection() || !enchantment.type.isAcceptableItem(item) && !bl) continue;
            for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                if (power < enchantment.getMinPower(i)) continue;
                list.add(new EnchantmentLevelEntry(enchantment, i));
                continue enchantmentLoop;
            }
        }
        return list;
    }

	/**
     * Generate the enchantments for enchanting the {@code stack}.
     */
    public static List<EnchantmentLevelEntry> generateEnchantments(Random random, ItemStack stack, int level, boolean treasureAllowed) {
        ArrayList<EnchantmentLevelEntry> list = Lists.newArrayList();
        Item item = stack.getItem();
        int i = item.getEnchantability();
        if (i <= 0) {
            return list;
        }
        level += 1 + random.nextInt(i / 4 + 1) + random.nextInt(i / 4 + 1);
        float f = (random.nextFloat() + random.nextFloat() - 1.0f) * 0.15f;
        List<EnchantmentLevelEntry> list2 = AmaziaEnchantmentHelper.getPossibleEntries(level = MathHelper.clamp(Math.round((float)level + (float)level * f), 1, Integer.MAX_VALUE), stack, treasureAllowed);
        
		for (Enchantment enchant : EnchantmentHelper.get(stack).keySet()) {
			AmaziaEnchantmentHelper.removeConflicts(list2, enchant);
		}
        
        if (!list2.isEmpty()) {
            Weighting.getRandom(random, list2).ifPresent(list::add);
        }
        return list;
    }
    
    /**
     * Enchants the {@code target} item stack and returns it.
     * 
     * @param treasureAllowed whether treasure enchantments may appear
     * @param level the experience level
     * @param target the item stack to enchant
     */
    public static ItemStack enchant(ItemStack target, List<EnchantmentLevelEntry> list) {
        boolean bl = target.isOf(Items.BOOK);
        if (bl) {
            target = new ItemStack(Items.ENCHANTED_BOOK);
        }
        for (EnchantmentLevelEntry enchantmentLevelEntry : list) {
            if (bl) {
                EnchantedBookItem.addEnchantment(target, enchantmentLevelEntry);
                continue;
            }
            target.addEnchantment(enchantmentLevelEntry.enchantment, enchantmentLevelEntry.level);
        }
        return target;
    }
    
    /**
     * Remove entries conflicting with the picked entry from the possible
     * entries.
     * 
     * @param possibleEntries the possible entries
     * @param pickedEntry the picked entry
     */
    public static void removeConflicts(List<EnchantmentLevelEntry> possibleEntries, Enchantment enchantment) {
        Iterator<EnchantmentLevelEntry> iterator = possibleEntries.iterator();
        while (iterator.hasNext()) {
            if (enchantment.canCombine(iterator.next().enchantment)) continue;
            iterator.remove();
        }
    }
}
