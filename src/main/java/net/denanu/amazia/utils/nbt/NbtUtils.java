package net.denanu.amazia.utils.nbt;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
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
		out.add(NbtInt.of(pos.getX()));
		out.add(NbtInt.of(pos.getY()));
		out.add(NbtInt.of(pos.getZ()));
		return out;
	}
	
	public static BlockPos toBlockPos(NbtList list) {
		return new BlockPos(list.getInt(0), list.getInt(1),  list.getInt(2));
	}
}
