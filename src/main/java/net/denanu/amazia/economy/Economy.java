package net.denanu.amazia.economy;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.denanu.amazia.JJUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

public class Economy extends PersistentState {
	private static Map<String, ItemEconomyFactory> salePossiblitiesFactory = new HashMap<String, ItemEconomyFactory>();
	private static Map<String, ArrayList<String>> professionSales = new HashMap<String, ArrayList<String>>();
	
	private Map<String, ItemEconomy> salePossibilites;
	
	public Economy() {
		this.build();
	}
	
	public static Economy fromNbt(NbtCompound nbt) {
		Economy out = new Economy();
		
		for (Entry<String, ItemEconomy> element : out.salePossibilites.entrySet()) {
			element.getValue().fromNbt(nbt.getCompound(element.getKey()));
		}
		
		return out;
	}
	
	public static void addProfessionItem(final String profession, final String item) {
		if (!professionSales.containsKey(profession)) {
			professionSales.put(profession, new ArrayList<String>());
		}
		professionSales.get(profession).add(item);
	}
	
	public static void trimToSize() {
		for (Entry<String, ArrayList<String>> entry : professionSales.entrySet()) {
			entry.getValue().trimToSize();
		}
	}
	
	public static void addItem(final ItemEconomyFactory item) {
		final ItemEconomyFactory existingValue = salePossiblitiesFactory.put(item.getName(), item);
        if (existingValue != null) {
            throw new InvalidParameterException("Duplicate marketId in economy: " + item.getName());
        }
	}
	
	public ItemEconomy getItem(final String key) {
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
		ArrayList<String> out = new ArrayList<String>();
		out.ensureCapacity(tries);
		List<String> possibilities = professionSales.get(profession);
		for (int i=0; i<tries; i++) {
			String element = JJUtils.getRandomListElement(possibilities);
			if (!out.contains(element)) {
				out.add(element);
			}
		}
		out.trimToSize();
		return out;
	}
	
	private void build() {
		this.salePossibilites = new HashMap<String, ItemEconomy>();
		for (Entry<String, ItemEconomyFactory> factory : salePossiblitiesFactory.entrySet()) {
			this.salePossibilites.put(factory.getKey(), factory.getValue().build());
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		for (Entry<String, ItemEconomy> element : this.salePossibilites.entrySet()) {
			nbt.put(element.getKey(), element.getValue().toNbt());
		}
		return nbt;
	}
	
	@Override
	public boolean isDirty() {
		return true;
	}

	public void reset() {
		for (Entry<String, ItemEconomy> entry : this.salePossibilites.entrySet()) {
			entry.getValue().reset();
		}
	}
}
