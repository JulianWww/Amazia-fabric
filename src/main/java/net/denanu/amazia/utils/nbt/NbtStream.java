package net.denanu.amazia.utils.nbt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.NbtDataSource;

public class NbtStream implements NbtDataSource {
	private final Collection<NbtCompound> nbt;
	
	public NbtStream(Collection<NbtCompound> nbt) {
		this.nbt = nbt;
	}

	@Override
	public Stream<NbtCompound> get(ServerCommandSource var1) throws CommandSyntaxException {
		return nbt.stream();
	}

	public static NbtStream of(NbtCompound nbt) {
		return new NbtStream(ImmutableSet.of(nbt));
	}

}
