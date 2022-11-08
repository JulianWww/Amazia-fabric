package net.denanu.amazia.village.sceduling;

import java.util.List;
import java.util.Random;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.village.server.AmaziaVillagerEntity;
import net.denanu.amazia.village.Village;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public abstract class VillageSceduler {
	private Village village;
	
	public VillageSceduler(Village _village) {
		this.village = _village;
	}
	
	public Village getVillage() {
		return this.village;
	}
	
	public abstract NbtCompound writeNbt();
    public abstract void readNbt(NbtCompound nbt);
	
	protected static <E extends Object> E getRandomListElement(List<E> list) {
		return JJUtils.getRandomListElement(list);
	}
	
	public void setChanged() {
		this.village.setChanged();
	}
	
	public static void markBlockAsFound(BlockPos pos, ServerWorld world) {
		world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY()+1.3, pos.getZ() + 0.5, 7, 0.3, 0.2, 0.3, 0.6);
	}
	public static void markBlockAsLost(BlockPos pos, ServerWorld world) {
		world.spawnParticles(ParticleTypes.ANGRY_VILLAGER, pos.getX() + 0.5, pos.getY()+1.3, pos.getZ() + 0.5, 7, 0.3, 0.2, 0.3, 0.6);
	}
	
	public abstract void discover(ServerWorld world, BlockPos pos);
	public abstract void initialize();
}
