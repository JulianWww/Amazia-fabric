package net.denanu.amazia.mixin;

import java.net.Proxy;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.datafixers.DataFixer;

import net.denanu.amazia.Amazia;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.ServerTask;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.text.Text;
import net.minecraft.util.ApiServices;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.minecraft.world.level.storage.LevelStorage;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin
{
	@Inject(method = "<init>()V", at = @At("TAIL"))
	public void MinecraftServer(Thread serverThread, LevelStorage.Session session, ResourcePackManager dataPackManager, SaveLoader saveLoader, Proxy proxy, DataFixer dataFixer, ApiServices apiServices, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo info) {
		return;
	}
}
