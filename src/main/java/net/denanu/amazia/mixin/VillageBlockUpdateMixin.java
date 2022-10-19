package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.Amazia;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(ServerWorld.class)
public class VillageBlockUpdateMixin {
	@Inject(at = @At("HEAD"), method = "updateListeners")
	private void syncWorldEvent(BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo info) {
		Amazia.getVillageManager().onVillageBlockUpdate(pos);
	}
}