package net.denanu.amazia.utils.scanners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.JJUtils;
import net.denanu.amazia.utils.CuboidChunkSampler;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.PersistentState;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.Structure;

public class ChunkScanner extends PersistentState {
	private static CuboidChunkSampler sampler = new CuboidChunkSampler(0,0,20,20);
	
	public static final Set<Identifier> toLookFor = new HashSet<Identifier>();
	
	private int i;
	public Map<Identifier, ChunkPos> structureLocations;
	public Set<Identifier> dirtys;
	
	public ChunkScanner() {
		this.i = 0;
		this.structureLocations = new HashMap<Identifier, ChunkPos>();
		this.dirtys = new HashSet<Identifier>();
	}
	public static ChunkScanner init() {
		ChunkScanner out = new ChunkScanner();
		out.dirtys = toLookFor;
		return out;
	}
	
	public static ChunkScanner fromNbt(NbtCompound nbt) {
		ChunkScanner out = new ChunkScanner();
		
		NbtCompound structures = (NbtCompound)nbt.get("structures");
		for (String key : structures.getKeys()) {
			out.structureLocations.put(
					new Identifier(key),
					NbtUtils.toChunkPos(((NbtList)structures.get(key)))
				);
		}
		NbtList dirtiesNbt = (NbtList)nbt.get("dirties");
		for (NbtElement dirty : dirtiesNbt) {
			out.dirtys.add(new Identifier(dirty.asString()));
		}
		return out;
	}
	
	public void tick(ServerWorld world) {
		if (world.getPlayers().size() > 0 && this.dirtys.size() > 0) {
			ChunkPos pos = this.getPos(world);
			Chunk chunk = world.getChunkManager().getWorldChunk(pos.x, pos.z);
			if (chunk!= null) {
				Map<Structure, StructureStart> structures = chunk.getStructureStarts();
				for (Structure entry : structures.keySet()) {		
					Identifier key = world.getRegistryManager().get(Registry.STRUCTURE_KEY).getId(entry);
					
					structureLocations.put(key, pos);
					this.dirtys.remove(key);
					
					Amazia.LOGGER.info("Found Structure: " + key.toString());
				}
			}
		}
	}
	
	public ChunkPos getPos(Identifier structure) {
		return this.structureLocations.get(structure);
	}
	
	public void markDirty(Identifier structure) {
		this.dirtys.add(structure);
	}
	
	private ChunkPos getPos(ServerWorld world) {
		i++;
		i = i % world.getPlayers().size();
		sampler.setPos(world.getPlayers().get(i));

		return sampler.getPos();
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.put("structures", this.getStructuresNbt());
		nbt.put("dirties",    this.getDirtyNbt());
		return nbt;
	}
	
	@Override
	public boolean isDirty() {
		return true;
	}
	
	private NbtCompound getStructuresNbt() {
		NbtCompound nbt = new NbtCompound();
		for (Entry<Identifier, ChunkPos> struct : this.structureLocations.entrySet()) {
			nbt.put(struct.getKey().toString(), 
					NbtUtils.toNbt(struct.getValue())
				);
		}
		return nbt;
	}
	
	private NbtElement getDirtyNbt() {
		NbtList nbt = new NbtList();
		for (Identifier item : this.dirtys) {
			nbt.add(NbtString.of(item.toString()));
		}
		return nbt;
	}
}
