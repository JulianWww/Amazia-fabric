package net.denanu.amazia.utils.nbt;

import java.util.ArrayList;
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
	public static NbtList toNbt(ChunkPos pos) {
		NbtList out = new NbtList();
		out.add(NbtInt.of(pos.x));
		out.add(NbtInt.of(pos.z));
		return out;
	}
	
	public static ChunkPos toChunkPos(NbtList list) {
		return new ChunkPos(list.getInt(0), list.getInt(1));
	}

	public static NbtElement toNbt(BlockPos pos) {
		NbtList out = new NbtList();
		if (pos!= null) {
			out.add(NbtInt.of(pos.getX()));
			out.add(NbtInt.of(pos.getY()));
			out.add(NbtInt.of(pos.getZ()));
		}
		return out;
	}
	
	public static BlockPos toBlockPos(NbtList list) {
		if (list.size() == 0) {
			return null;
		}
		return new BlockPos(list.getInt(0), list.getInt(1),  list.getInt(2));
	}
	
	public static <E extends Iterable<BlockPos>> NbtList toNbt(E list) {
		NbtList nbt = new NbtList();
		for (BlockPos pos: list) {
			nbt.add(toNbt(pos));
		}
		return nbt;
	}
	public static List<BlockPos> toBlockPosList(NbtList tag) {
		List<BlockPos> list = new ArrayList<BlockPos>();
		for (int idx=0; idx < tag.size(); idx++) {
			BlockPos pos = toBlockPos(tag.getList(idx));
			if (pos != null) {
				list.add(pos);
			}
		}
		return list;
	}
	
	public static <E extends Map<Identifier, List<BlockPos>>> NbtCompound toNbt(E data){
		NbtCompound nbt = new NbtCompound();
		for (Entry<Identifier, List<BlockPos>> entry : data.entrySet()) {
			nbt.put(entry.getKey().toString(), toNbt(entry.getValue()));
		}
		return nbt;
	}
	
	public static <E extends Set<BlockPos>>NbtList toNbt(E data){
		NbtList nbt = new NbtList();
		for (BlockPos entry : data) {
			nbt.add(NbtUtils.toNbt(entry));
		}
		return nbt;
	}
	
	public static <E extends Map<Identifier, List<BlockPos>>> E fromNbt(NbtCompound nbt, E out){
		for (String key : nbt.getKeys()) {
			out.put(new Identifier(key), toBlockPosList(nbt.getList(key, NbtList.LIST_TYPE)));
		}
		return out;
	}
}
