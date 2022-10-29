package net.denanu.amazia.economy;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Economy {
	private Map<String, ItemEconomy> salePossiblities;
	private Map<Professions, ArrayList<String>> professionSales;
	
	public Economy() {
		this.salePossiblities = new HashMap<String, ItemEconomy>();
		this.professionSales  = new HashMap<Professions, ArrayList<String>>();
	}
	
	public void addProfessionItem(final Professions profession, final String item) {
		if (!this.professionSales.containsKey(profession)) {
			this.professionSales.put(profession, new ArrayList<String>());
		}
		this.professionSales.get(profession).add(item);
	}
	
	public void trimToSize() {
		for (Entry<Professions, ArrayList<String>> entry : this.professionSales.entrySet()) {
			entry.getValue().trimToSize();
		}
	}
	
	public void addItem(final ItemEconomy item) {
		final ItemEconomy existingValue = this.salePossiblities.put(item.getName(), item);
        if (existingValue != null) {
            throw new InvalidParameterException("Duplicate marketId in economy: " + item.getName());
        }
	}
}
