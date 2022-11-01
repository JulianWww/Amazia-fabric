package net.denanu.amazia.economy;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.economy.itemEconomy.BaseItemEconomy;
import net.denanu.amazia.economy.offerModifiers.ModifierEconomy;
import net.denanu.amazia.economy.offerModifiers.finalizers.OfferFinalModifer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;

public class Economy extends PersistentState {
	private static Map<String, BaseItemEconomy> salePossibilites = new HashMap<String, BaseItemEconomy>();
	private static Map<String, ArrayList<String>> professionSales = new HashMap<String, ArrayList<String>>();
	
	private static Map<String, ModifierEconomy> modifierPossibilities = new HashMap<String, ModifierEconomy>();
	private static Map<Identifier, OfferFinalModifer> finalizers = new HashMap<Identifier, OfferFinalModifer>();
	
	//private Map<String, IItemEconomy> salePossibilites;
	
	public static Economy fromNbt(NbtCompound nbt) {
		for (Entry<String, BaseItemEconomy> element : salePossibilites.entrySet()) {
			if (nbt.contains(element.getKey())) {
				element.getValue().fromNbt(nbt.getCompound(element.getKey()));
			}
		}
		for (Entry<String, ModifierEconomy> element : modifierPossibilities.entrySet()) {
			if (nbt.contains(element.getKey())) {
				element.getValue().fromNbt(nbt.getCompound(element.getKey()));
			}
		}
		
		return new Economy();
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.put("items", this.itemEconomyToNbt());
		nbt.put("modifiers", this.itemModifiersToNbt());
		return nbt;
	}
	
	private NbtCompound itemEconomyToNbt() {
		NbtCompound nbt = new NbtCompound();
		for (Entry<String, BaseItemEconomy> element : salePossibilites.entrySet()) {
			if (element.getValue().hasNbt()) {
				nbt.put(element.getKey(), element.getValue().toNbt());
			}
		}
		return nbt;
	}
	private NbtCompound itemModifiersToNbt() {
		NbtCompound nbt = new NbtCompound();
		for (Entry<String, ModifierEconomy> element : modifierPossibilities.entrySet()) {
			nbt.put(element.getKey(), element.getValue().toNbt());
		}
		return nbt;
	}
	
	public static void addProfessionItem(final String profession, final String item) {
		if (!professionSales.containsKey(profession)) {
			professionSales.put(profession, new ArrayList<String>());
		}
		professionSales.get(profession).add(item);
	}
	
	public static String registerModifier(final String key, final ModifierEconomy economy) {
		final ModifierEconomy existingValue = modifierPossibilities.put(key, economy);
        if (existingValue != null) {
            throw new InvalidParameterException("Duplicate marketId in economy of key " + key);
        }
        return key;
	}
	
	public static ModifierEconomy getModifierEconomy(final String mod) {
		return Economy.modifierPossibilities.get(mod);
	}
	
	public static void registerFinalizer(final Identifier key, final OfferFinalModifer finalzier) {
		final OfferFinalModifer existingValue = finalizers.put(key, finalzier);
        if (existingValue != null) {
            throw new InvalidParameterException("Duplicate marketId in economy of key " + key);
        }
	}
	
	public static OfferFinalModifer getFinalizer(final Identifier key) {
		return Economy.finalizers.get(key);
	}
	
	public static void trimToSize() {
		for (Entry<String, ArrayList<String>> entry : professionSales.entrySet()) {
			entry.getValue().trimToSize();
		}
	}
	
	public static void addItem(final BaseItemEconomy item) {
		final BaseItemEconomy existingValue = salePossibilites.put(item.getName(), item);
        if (existingValue != null) {
            throw new InvalidParameterException("Duplicate marketId in economy: " + item.getName());
        }
	}
	
	public BaseItemEconomy getItem(final String key) {
		return salePossibilites.get(key);
	}
	
	public AmaziaTradeOfferList buildTrades(IAmaziaMerchant merchant) {
		AmaziaTradeOfferList out = new AmaziaTradeOfferList();
		Collection<String> items = merchant.getTradePossibilities();
		for (String item : items) {
			out.add(salePossibilites.get(item).build(merchant));
		}
		return out;
	}
	
	public static List<String> getTrades(int tries, String profession) {
		return professionSales.get(profession);
	}
	
	@Override
	public boolean isDirty() {
		return true;
	}

	public void reset() {
		for (Entry<String, BaseItemEconomy> entry : salePossibilites.entrySet()) {
			entry.getValue().reset();
		}
		for (Entry<String, ModifierEconomy> entry : Economy.modifierPossibilities.entrySet()) {
			entry.getValue().reset();
		}
	}

	public static Collection<String> getModifierList() {
		return Economy.modifierPossibilities.keySet();
	}
	
	
	public static void update(int tick) {
		if (tick % 1200 == 0) {
			Economy.update();
			return;
		}
	}

	private static void update() {
		for (Entry<String, BaseItemEconomy> entry : Economy.salePossibilites.entrySet()) {
			entry.getValue().update();
		}
		for (Entry<String, ModifierEconomy> entry : Economy.modifierPossibilities.entrySet()) {
			entry.getValue().update();
		}
	}
}
