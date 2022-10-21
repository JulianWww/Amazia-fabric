package net.denanu.amazia.mixin;

import java.util.function.BiConsumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.block.AmaziaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.trunk.TrunkPlacer;

@Mixin(TrunkPlacer.class)
public class TrunkPlacerMixin {
	@Inject(method="setToDirt", at = @At("HEAD"), cancellable = true)
	private static void setToDirtMixin(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, BlockPos pos, TreeFeatureConfig config, CallbackInfo info) {
		if (world.testBlockState(pos, state -> state.isOf(AmaziaBlocks.TREE_FARM_MARKER))) {
			info.cancel();
		}
	}
}
