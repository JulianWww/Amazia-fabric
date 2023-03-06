package net.denanu.amazia.economy;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.denanu.amazia.economy.itemEconomy.BaseItemEconomy;
import net.denanu.amazia.economy.offerModifiers.ModifierEconomy;
import net.denanu.amazia.economy.offerModifiers.finalizers.OfferFinalModifer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class Economy {
	private static Map<String, BaseItemEconomy> salePossibilites = new HashMap<>();
	private static Map<String, ArrayList<String>> professionSales = new HashMap<>();

	private static Map<String, ModifierEconomy> modifierPossibilities = new HashMap<>();
	private static Map<Identifier, OfferFinalModifer> finalizers = new HashMap<>();

	public static void fromNbt(final NbtCompound nbt) {
		for (final Entry<String, BaseItemEconomy> element : Economy.salePossibilites.entrySet()) {
			if (nbt.contains(element.getKey())) {
				element.getValue().fromNbt(nbt.getCompound(element.getKey()));
			}
		}
		for (final Entry<String, ModifierEconomy> element : Economy.modifierPossibilities.entrySet()) {
			if (nbt.contains(element.getKey())) {
				element.getValue().fromNbt(nbt.getCompound(element.getKey()));
			}
		}
	}

	public static NbtCompound writeNbt() {
		return Economy.writeNbt(new NbtCompound());
	}

	public static NbtCompound writeNbt(final NbtCompound nbt) {
		nbt.put("items", Economy.itemEconomyToNbt());
		nbt.put("modifiers", Economy.itemModifiersToNbt());
		return nbt;
	}

	private static NbtCompound itemEconomyToNbt() {
		final NbtCompound nbt = new NbtCompound();
		for (final Entry<String, BaseItemEconomy> element : Economy.salePossibilites.entrySet()) {
			if (element.getValue().hasNbt()) {
				nbt.put(element.getKey(), element.getValue().toNbt());
			}
		}
		return nbt;
	}
	private static NbtCompound itemModifiersToNbt() {
		final NbtCompound nbt = new NbtCompound();
		for (final Entry<String, ModifierEconomy> element : Economy.modifierPossibilities.entrySet()) {
			nbt.put(element.getKey(), element.getValue().toNbt());
		}
		return nbt;
	}

	public static void addProfessionItem(final String profession, final String item) {
		if (!Economy.professionSales.containsKey(profession)) {
			Economy.professionSales.put(profession, new ArrayList<String>());
		}
		Economy.professionSales.get(profession).add(item);
	}

	public static String registerModifier(final String key, final ModifierEconomy economy) {
		final ModifierEconomy existingValue = Economy.modifierPossibilities.put(key, economy);
		if (existingValue != null) {
			throw new InvalidParameterException("Duplicate marketId in economy of key " + key);
		}
		return key;
	}

	public static ModifierEconomy getModifierEconomy(final String mod) {
		return Economy.modifierPossibilities.get(mod);
	}

	public static void registerFinalizer(final Identifier key, final OfferFinalModifer finalzier) {
		final OfferFinalModifer existingValue = Economy.finalizers.put(key, finalzier);
		if (existingValue != null) {
			throw new InvalidParameterException("Duplicate marketId in economy of key " + key);
		}
	}

	public static OfferFinalModifer getFinalizer(final Identifier key) {
		return Economy.finalizers.get(key);
	}

	public static void trimToSize() {
		for (final Entry<String, ArrayList<String>> entry : Economy.professionSales.entrySet()) {
			entry.getValue().trimToSize();
		}
	}

	public static void addItem(final BaseItemEconomy item) {
		final BaseItemEconomy existingValue = Economy.salePossibilites.put(item.getName(), item);
		if (existingValue != null) {
			throw new InvalidParameterException("Duplicate marketId in economy: " + item.getName());
		}
	}

	public static BaseItemEconomy getItem(final String key) {
		return Economy.salePossibilites.get(key);
	}

	public static AmaziaTradeOfferList buildTrades(final IAmaziaMerchant merchant) {
		final AmaziaTradeOfferList out = new AmaziaTradeOfferList();
		final Collection<String> items = merchant.getTradePossibilities();
		for (final String item : items) {
			final BaseItemEconomy economy = Economy.salePossibilites.get(item);
			for (int idx=0; idx < economy.getCount(); idx++) {
				out.add(economy.build(merchant));
			}
		}
		return out;
	}

	public static List<String> getTrades(final int tries, final String profession) {
		return Economy.professionSales.get(profession);
	}

	public static void reset() {
		for (final Entry<String, BaseItemEconomy> entry : Economy.salePossibilites.entrySet()) {
			entry.getValue().reset();
		}
		for (final Entry<String, ModifierEconomy> entry : Economy.modifierPossibilities.entrySet()) {
			entry.getValue().reset();
		}
	}

	public static Collection<String> getModifierList() {
		return Economy.modifierPossibilities.keySet();
	}


	public static void update(final int tick) {
		if (tick % 1200 == 0) {
			Economy.update();
		}
	}

	private static void update() {
		for (final Entry<String, BaseItemEconomy> entry : Economy.salePossibilites.entrySet()) {
			entry.getValue().update();
		}
		for (final Entry<String, ModifierEconomy> entry : Economy.modifierPossibilities.entrySet()) {
			entry.getValue().update();
		}
	}
}
