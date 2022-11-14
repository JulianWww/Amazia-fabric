package net.denanu.amazia.mixin;

import java.util.List;
import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.Amazia;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.Spawner;

@Mixin(ServerWorld.class)
public class MinecraftServerWorldMixin
{
	@Inject(at = @At("RETURN"), method = "<init>")
	public void MinecraftServer(
			final MinecraftServer server,
			final Executor workerExecutor,
			final LevelStorage.Session session,
			final ServerWorldProperties properties,
			final RegistryKey<World> worldKey,
			final DimensionOptions dimensionOptions,
			final WorldGenerationProgressListener worldGenerationProgressListener,
			final boolean debugWorld,
			final long seed,
			final List<Spawner> spawners,
			final boolean shouldTickTime,
			final CallbackInfo info) {
		if (worldKey == World.OVERWORLD) {
			Amazia.registerCrafters(server);
		}
	}
}
