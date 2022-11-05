package net.denanu.amazia.village.sceduling;

import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.village.Village;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class RancherSceduler extends VillageSceduler {

	public RancherSceduler(Village village) {
		super(village);
	}

	@Override
	public NbtCompound writeNbt() {
		return new NbtCompound();
	}

	@Override
	public void readNbt(NbtCompound nbt) {
	}

	@Override
	public void discover(ServerWorld world, BlockPos pos) {
	}

	@Override
	public BlockPos getRandomPos(ServerWorld world, AmaziaVillagerEntity entity) {
		return null;
	}

	@Override
	public void initialize() {
	}
}
