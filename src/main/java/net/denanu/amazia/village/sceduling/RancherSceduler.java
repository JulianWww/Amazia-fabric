package net.denanu.amazia.village.sceduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.block.custom.api.AmaziaTrough;
import net.denanu.amazia.components.AmaziaEntityComponents;
import net.denanu.amazia.highlighting.BlockHighlightingAmaziaConfig;
import net.denanu.amazia.utils.nbt.NbtUtils;
import net.denanu.amazia.village.Village;
import net.denanu.blockhighlighting.Highlighter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class RancherSceduler extends VillageSceduler {
	private Map<Identifier, List<BlockPos>> troughLocations;

	public RancherSceduler(final Village village) {
		super(village);
		this.troughLocations = new HashMap<>();
	}

	@Override
	public NbtCompound writeNbt() {
		return NbtUtils.toNbt(this.troughLocations);
	}

	@Override
	public void readNbt(final NbtCompound nbt) {
		this.troughLocations = NbtUtils.fromNbt(nbt, this.troughLocations);
	}

	@Override
	public void initialize() {
	}

	@Nullable
	public BlockPos getEntityTargetPos(final AnimalEntity animal) {
		final List<BlockPos> registry = this.troughLocations.get(EntityType.getId(animal.getType()));
		if (registry != null) {
			return JJUtils.getRandomListElement(registry);
		}
		return null;
	}

	private boolean hasPen(final AnimalEntity animal) {
		return this.troughLocations.containsKey(EntityType.getId(animal.getType()));
	}

	public AnimalEntity getUnboundAnimal() {
		final AnimalEntity animal =  JJUtils.getRandomListElement(
				this.getVillage().getWorld().getEntitiesByClass(AnimalEntity.class, this.getVillage().getBox(), this::canReciveAttention)
				);
		if (animal == null || animal.isLeashed()) {
			return null;
		}
		return animal;
	}

	private boolean canReciveAttention(final AnimalEntity animal) {
		return this.hasPen(animal) && (
				!AmaziaEntityComponents.getIsPartOfVillage(animal)
				|| !animal.isInLove() &&
				animal.getBreedingAge() == 0
				);
	}

	@Override
	public void discover(final ServerWorld world, final BlockPos pos) {
		this.setChanged();
		if (world.getBlockState(pos).getBlock() instanceof final AmaziaTrough block) {
			final Identifier id = block.getEntityType();
			if (!this.troughLocations.containsKey(id)) {
				this.troughLocations.put(id, new ArrayList<BlockPos>());
			}
			this.troughLocations.get(id).add(pos);

			this.setChanged();
			Highlighter.highlight(world, BlockHighlightingAmaziaConfig.RANCHING_PENS, pos);
		}
		else {
			for (final Entry<Identifier, List<BlockPos>> element : this.troughLocations.entrySet()) {
				if (element.getValue().remove(pos)) {
					this.troughLocations.remove(element.getKey());

					this.setChanged();
					Highlighter.unhighlight(world, BlockHighlightingAmaziaConfig.RANCHING_PENS, pos);
				}
			}
		}
	}
}
