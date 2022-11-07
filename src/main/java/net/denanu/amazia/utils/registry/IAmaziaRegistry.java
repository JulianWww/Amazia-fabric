package net.denanu.amazia.utils.registry;

import java.util.Optional;

import net.minecraft.util.Identifier;

public interface IAmaziaRegistry <T> {
	public void register(Identifier id, T val);
	public Optional<T> get(Identifier id);
}
