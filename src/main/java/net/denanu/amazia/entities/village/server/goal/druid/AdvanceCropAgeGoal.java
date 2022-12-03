package net.denanu.amazia.entities.village.server.goal.druid;

import org.apache.commons.lang3.mutable.MutableInt;

import net.denanu.amazia.entities.moods.VillagerMoods;
import net.denanu.amazia.entities.village.server.DruidEntity;
import net.denanu.amazia.entities.village.server.goal.TimedVillageGoal;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class AdvanceCropAgeGoal extends TimedVillageGoal<DruidEntity> {

	public AdvanceCropAgeGoal(final DruidEntity e, final int priority) {
		super(e, priority);
	}

	@Override
	protected int getRequiredTime() {
		return this.entity.getPlantAdvanceAgeTime();
	}

	public static boolean useOnFertilizable(final World world, final BlockPos pos) {
		final BlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() instanceof final Fertilizable fertilizable && fertilizable.isFertilizable(world, pos, blockState, world.isClient)) {
			if (world instanceof final ServerWorld sworld) {
				if (fertilizable.canGrow(world, world.random, pos, blockState)) {
					fertilizable.grow(sworld, world.random, pos, blockState);
				}
			}
			return true;
		}
		return false;
	}

	public static boolean grow(final ServerWorld world, final BlockPos pos) {
		final boolean success = AdvanceCropAgeGoal.useOnFertilizable(world, pos);
		if (success) {
			world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, pos, 0);
		}
		return success;
	}


	@Override
	protected void takeAction() {
		final BlockPos pos = this.entity.getToRegrow();
		final ServerWorld world = (ServerWorld) this.entity.world;
		final MutableInt count = new MutableInt();
		/*BlockPosStream.circle(pos, this.entity.getGrowRadius()).forEach(pos2 -> {
			if (AdvanceCropAgeGoal.grow(world, pos2)) {
				count.add(1);
			}
		});*/
		if (AdvanceCropAgeGoal.grow(world, pos)) {
			count.add(1);
		}
		if (count.getValue() > 0) {
			this.entity.emmitMood(VillagerMoods.HAPPY);
		}
		else {
			this.entity.emmitMood(VillagerMoods.ANGRY);
		}
	}

}
