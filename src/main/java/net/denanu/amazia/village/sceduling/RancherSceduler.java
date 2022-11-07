package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.block.custom.api.AmaziaTrough;
import net.denanu.amazia.components.AmaziaComponents;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;

public class RancherSceduler extends VillageSceduler {
	private ImmutableList<TypeFilter<Entity, ? extends AnimalEntity>> animals = ImmutableList.of(EntityType.COW, EntityType.SHEEP, EntityType.CHICKEN, EntityType.PIG);
	
	private Map<Identifier, List<BlockPos>> troughLocations;

	public RancherSceduler(Village village) {
		super(village);
		this.troughLocations = new HashMap<Identifier, List<BlockPos>>();
	}

	@Override
	public NbtCompound writeNbt() {
		return NbtUtils.toNbt(this.troughLocations);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.troughLocations = NbtUtils.fromNbt(nbt, this.troughLocations);
	}
	
	@Override
	public void initialize() {
	}
	
	@Nullable
	public BlockPos getEntityTargetPos(AnimalEntity animal) {
		List<BlockPos> registry = this.troughLocations.get(EntityType.getId(animal.getType()));
		if (registry != null) {
			return JJUtils.getRandomListElement(registry);
		}
		return null;
	}
	
	private boolean hasPen(AnimalEntity animal) {
		return this.troughLocations.containsKey(EntityType.getId(animal.getType()));
	}
	
	public AnimalEntity getUnboundAnimal() {
		AnimalEntity animal =  JJUtils.getRandomListElement(
				this.getVillage().getWorld().getEntitiesByClass(AnimalEntity.class, this.getVillage().getBox(), a -> this.canReciveAttention(a))
			);
		if (animal == null || animal.isLeashed()) return null;
		return animal;
	}
	
	private boolean canReciveAttention(AnimalEntity animal) {
		return this.hasPen(animal) && (
				!AmaziaComponents.getIsPartOfVillage(animal)
				|| (
						!animal.isInLove() &&
						animal.getBreedingAge() == 0
					)
			);
	}

	@Override
	public void discover(ServerWorld world, BlockPos pos) {
		this.setChanged();
		if (world.getBlockState(pos).getBlock() instanceof AmaziaTrough block) {
			Identifier id = block.getEntityType();
			if (!this.troughLocations.containsKey(id)) {
				this.troughLocations.put(id, new ArrayList<BlockPos>());
			}
			this.troughLocations.get(id).add(pos);
		}
		else {
			for (Entry<Identifier, List<BlockPos>> element : this.troughLocations.entrySet()) {
				if (element.getValue().remove(pos)) {
					this.troughLocations.remove(element.getKey());
					return;
				};
			}
		}
	}
}
