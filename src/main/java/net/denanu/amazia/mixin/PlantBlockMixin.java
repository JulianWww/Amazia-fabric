package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.denanu.amazia.block.AmaziaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Mixin(PlantBlock.class)
public class PlantBlockMixin {
	@Inject(method = "canPlantOnTop", at = @At("RETURN"), cancellable = true)
	private void injectCanPlantOnTop(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> out) {
		out.setReturnValue(out.getReturnValue() || floor.isOf(AmaziaBlocks.TREE_FARM_MARKER));
	}
}
