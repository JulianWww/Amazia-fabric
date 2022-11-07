package net.denanu.amazia.utils.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.minecraft.util.Identifier;

public class AmaziaRegistry<T> implements IAmaziaRegistry<T> {
	protected Map<Identifier, T> entries;
	
	public AmaziaRegistry() {
		this.entries = new HashMap<Identifier, T>();
	}

	@Override
	public void register(Identifier id, T val) {
		this.entries.putIfAbsent(id, val);
	}

	@Override
	public Optional<T> get(Identifier id) {
		return Optional.ofNullable(this.entries.get(id));
	}

}
