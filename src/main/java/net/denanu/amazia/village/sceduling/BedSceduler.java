package net.denanu.amazia.village.sceduling;

import java.util.function.Predicate;

import net.denanu.amazia.components.AmaziaBlockComponents;
import net.denanu.amazia.village.Village;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class BedSceduler extends PathingNoHeightSceduler {

	public BedSceduler(final Village _village, final Predicate<BlockState> tester, final Identifier id) {
		super(_village, tester, id);
	}

	@Override
	public void find(final ServerWorld world, final BlockPos pos) {
		final BlockEntity entity = world.getBlockEntity(pos);
		AmaziaBlockComponents.addVillage(entity, this.getVillage());
	}

	@Override
	public void loose(final ServerWorld world, final BlockPos pos) {
		//final BlockEntity entity = world.getBlockEntity(pos);
		//AmaziaBlockComponents.removeVillage(entity, this.getVillage());
	}
}
