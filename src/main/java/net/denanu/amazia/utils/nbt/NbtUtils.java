package net.denanu.amazia.utils.nbt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class NbtUtils {
	public static NbtList toNbt(final ChunkPos pos) {
		final NbtList out = new NbtList();
		out.add(NbtInt.of(pos.x));
		out.add(NbtInt.of(pos.z));
		return out;
	}

	public static ChunkPos toChunkPos(final NbtList list) {
		return new ChunkPos(list.getInt(0), list.getInt(1));
	}

	public static NbtElement toNbt(final BlockPos pos) {
		final NbtList out = new NbtList();
		if (pos!= null) {
			out.add(NbtInt.of(pos.getX()));
			out.add(NbtInt.of(pos.getY()));
			out.add(NbtInt.of(pos.getZ()));
		}
		return out;
	}

	public static BlockPos toBlockPos(final NbtList list) {
		if (list.size() == 0) {
			return null;
		}
		return new BlockPos(list.getInt(0), list.getInt(1),  list.getInt(2));
	}

	public static <E extends Iterable<BlockPos>> NbtList toNbt(final E list) {
		final NbtList nbt = new NbtList();
		for (final BlockPos pos: list) {
			nbt.add(NbtUtils.toNbt(pos));
		}
		return nbt;
	}
	public static List<BlockPos> toBlockPosList(final NbtList tag) {
		final List<BlockPos> list = new ArrayList<>();
		for (int idx=0; idx < tag.size(); idx++) {
			final BlockPos pos = NbtUtils.toBlockPos(tag.getList(idx));
			if (pos != null) {
				list.add(pos);
			}
		}
		return list;
	}

	public static HashSet<BlockPos> toBlockPosSet(final NbtList tag) {
		final HashSet<BlockPos> list = new HashSet<BlockPos>();
		for (int idx=0; idx < tag.size(); idx++) {
			final BlockPos pos = NbtUtils.toBlockPos(tag.getList(idx));
			if (pos != null) {
				list.add(pos);
			}
		}
		return list;
	}

	public static <E extends Map<Identifier, List<BlockPos>>> NbtCompound toNbt(final E data){
		final NbtCompound nbt = new NbtCompound();
		for (final Entry<Identifier, List<BlockPos>> entry : data.entrySet()) {
			nbt.put(entry.getKey().toString(), NbtUtils.toNbt(entry.getValue()));
		}
		return nbt;
	}

	public static <E extends Set<BlockPos>>NbtList toNbt(final E data){
		final NbtList nbt = new NbtList();
		for (final BlockPos entry : data) {
			nbt.add(NbtUtils.toNbt(entry));
		}
		return nbt;
	}

	public static <E extends Map<Identifier, List<BlockPos>>> E fromNbt(final NbtCompound nbt, final E out){
		for (final String key : nbt.getKeys()) {
			out.put(new Identifier(key), NbtUtils.toBlockPosList(nbt.getList(key, NbtElement.LIST_TYPE)));
		}
		return out;
	}
}
