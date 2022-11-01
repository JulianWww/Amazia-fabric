package net.denanu.amazia.mixin;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.Amazia;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.Spawner;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin extends ReentrantThreadExecutor<ServerTask> implements CommandOutput, AutoCloseable
{
	public MinecraftServerMixin(String string) {
		super(string);
	}

	@Inject(at = @At("HEAD"))
	public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
		Amazia.economy.update(((MinecraftServerAccessor)(Object)this).getTick());
	}

	@Override
	public void sendMessage(Text var1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldReceiveFeedback() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldTrackOutput() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldBroadcastConsoleToOps() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected ServerTask createTask(Runnable var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean canExecute(ServerTask var1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Thread getThread() {
		// TODO Auto-generated method stub
		return null;
	}
}
