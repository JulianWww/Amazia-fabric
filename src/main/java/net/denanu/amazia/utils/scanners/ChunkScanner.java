package net.denanu.amazia.utils.scanners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.denanu.amazia.utils.CuboidChunkSampler;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.Structure;

public class ChunkScanner {
	private static CuboidChunkSampler sampler = new CuboidChunkSampler(0,0,20,20);

	public static final Set<Identifier> toLookFor = new HashSet<>();

	private static int i = 0;
	public static Map<Identifier, ChunkPos> structureLocations = new HashMap<>();
	public static Set<Identifier> dirtys = new HashSet<>();

	public static void init() {
		ChunkScanner.dirtys = ChunkScanner.toLookFor;
	}

	public static void fromNbt(final NbtCompound nbt) {
		final NbtCompound structures = (NbtCompound)nbt.get("structures");
		for (final String key : structures.getKeys()) {
			ChunkScanner.structureLocations.put(
					new Identifier(key),
					NbtUtils.toChunkPos((NbtList)structures.get(key))
					);
		}
		final NbtList dirtiesNbt = (NbtList)nbt.get("dirties");
		for (final NbtElement dirty : dirtiesNbt) {
			ChunkScanner.dirtys.add(new Identifier(dirty.asString()));
		}
	}

	public static void tick(final ServerWorld world) {
		if (world.getPlayers().size() > 0 && ChunkScanner.dirtys.size() > 0) {
			final ChunkPos pos = ChunkScanner.getPos(world);
			final Chunk chunk = world.getChunkManager().getWorldChunk(pos.x, pos.z);
			if (chunk!= null) {
				final Map<Structure, StructureStart> structures = chunk.getStructureStarts();
				for (final Structure entry : structures.keySet()) {
					final Identifier key = world.getRegistryManager().get(Registry.STRUCTURE_KEY).getId(entry);

					ChunkScanner.structureLocations.put(key, pos);
					ChunkScanner.dirtys.remove(key);
				}
			}
		}
	}

	public static ChunkPos getPos(final Identifier structure) {
		return ChunkScanner.structureLocations.get(structure);
	}

	public static void markDirty(final Identifier structure) {
		ChunkScanner.dirtys.add(structure);
	}

	private static ChunkPos getPos(final ServerWorld world) {
		ChunkScanner.i++;
		ChunkScanner.i = ChunkScanner.i % world.getPlayers().size();
		ChunkScanner.sampler.setPos(world.getPlayers().get(ChunkScanner.i));

		return ChunkScanner.sampler.getPos();
	}

	public static NbtCompound writeNbt() {
		return ChunkScanner.writeNbt(new NbtCompound());
	}

	public static NbtCompound writeNbt(final NbtCompound nbt) {
		nbt.put("structures", ChunkScanner.getStructuresNbt());
		nbt.put("dirties",    ChunkScanner.getDirtyNbt());
		return nbt;
	}

	private static NbtCompound getStructuresNbt() {
		final NbtCompound nbt = new NbtCompound();
		for (final Entry<Identifier, ChunkPos> struct : ChunkScanner.structureLocations.entrySet()) {
			nbt.put(struct.getKey().toString(),
					NbtUtils.toNbt(struct.getValue())
					);
		}
		return nbt;
	}

	private static NbtElement getDirtyNbt() {
		final NbtList nbt = new NbtList();
		for (final Identifier item : ChunkScanner.dirtys) {
			nbt.add(NbtString.of(item.toString()));
		}
		return nbt;
	}
}
