package net.denanu.amazia.village;

import java.util.HashSet;

import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class VillageManager {
	private HashSet<Village> villages = new HashSet<Village>();

	public void addVillage(Village v) {
		this.villages.add(v);
	}
	public void removeVillage(Village v) {
		this.villages.remove(v);
	}

	public void onPathingBlockUpdate(BlockPos pos) {
		for (Village v: this.villages) {
			v.onPathingBlockUpdate(pos);
		}
	}
	public void onVillageBlockUpdate(BlockPos pos) {
		for (Village v: this.villages) {
			v.onVillageBlockUpdate(pos);
		}
	}

	public static Village getVillage(BlockPos pos, ServerWorld world) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof VillageCoreBlockEntity core) {
			return core.getVillage();
		}
		throw new RuntimeException("invalid villag location " + pos);
	}
}
