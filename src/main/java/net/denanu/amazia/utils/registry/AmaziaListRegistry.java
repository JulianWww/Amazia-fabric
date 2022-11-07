package net.denanu.amazia.utils.registry;

import java.util.ArrayList;
import java.util.Optional;

import net.denanu.amazia.JJUtils;
import net.minecraft.util.Identifier;

public class AmaziaListRegistry<T> extends AmaziaRegistry<ArrayList<T>> {
	
	public AmaziaListRegistry<T> add(Identifier id, T val) {
		if (!this.entries.containsKey(id)) {
			this.register(id, new ArrayList<T>());
		}
		this.entries.get(id).add(val);
		return this;
	}
	
	public Optional<T> getRandom(Identifier id) {
		return Optional.ofNullable(JJUtils.getNullableRandomListElement(this.entries.get(id)));
	}
}
