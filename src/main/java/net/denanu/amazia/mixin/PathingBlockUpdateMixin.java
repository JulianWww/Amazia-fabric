package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.pathing.PathingShouldUpdateChecker;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(ServerWorld.class)
public class PathingBlockUpdateMixin {
	@Inject(at = @At("TAIL"), method = "updateListeners")
	private void syncWorldEvent(BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo info) {
		if (PathingShouldUpdateChecker.shouldUpdate(pos, oldState, newState, flags))
			Amazia.getVillageManager().onPathingBlockUpdate(pos);
	}
}
