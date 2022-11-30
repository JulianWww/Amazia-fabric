package net.denanu.amazia.village;

import java.util.HashSet;

import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class VillageManager {
	private final HashSet<Village> villages = new HashSet<Village>();

	public void addVillage(final Village v) {
		this.villages.add(v);
	}
	public void removeVillage(final Village v) {
		this.villages.remove(v);
	}

	public void onPathingBlockUpdate(final BlockPos pos) {
		for (final Village v: this.villages) {
			v.onPathingBlockUpdate(pos);
		}
	}
	public void onVillageBlockUpdate(final BlockPos pos) {
		for (final Village v: this.villages) {
			v.onVillageBlockUpdate(pos);
		}
	}

	public Village getVillage(final BlockPos pos) {
		for (final Village village : this.villages) {
			if (village.isInVillage(pos)) {
				return village;
			}
		}
		return null;
	}

	public HashSet<Village> get() {
		return this.villages;
	}

	public static Village getVillage(final BlockPos pos, final ServerWorld world) {
		final BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof final VillageCoreBlockEntity core) {
			return core.getVillage();
		}
		throw new RuntimeException("invalid villag location " + pos);
	}
}
