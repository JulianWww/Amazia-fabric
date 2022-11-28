package net.denanu.amazia.networking;

import net.denanu.amazia.Amazia;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.OffThreadException;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.thread.ThreadExecutor;

public class NetworkingUtils {
	public static <T extends PacketListener, E> void forceMainThread(final PlayRunHandler<E> packet, final T listener, final ThreadExecutor<?> engine, final MinecraftClient client, final ClientPlayNetworkHandler handler, final E data, final PacketSender responseSender) throws OffThreadException {
		if (!engine.isOnThread()) {
			engine.executeSync(() -> {
				if (listener.getConnection().isOpen()) {
					try {
						packet.run(client, handler, data, responseSender);
					}
					catch (final Exception exception) {
						if (listener.shouldCrashOnException()) {
							throw exception;
						}
						Amazia.LOGGER.error("Failed to handle packet, suppressing error");
					}
				} else {
					Amazia.LOGGER.debug("Ignoring packet due to disconnection");
				}
			});
			//throw OffThreadException.INSTANCE;
		}
	}

	public static <T extends PacketListener, E> void ServerforceMainThread (
			final ServerPlayRunHandler<E> packet,
			final T listener,
			final ThreadExecutor<?> engine,
			final MinecraftServer client,
			final ServerPlayNetworkHandler handler,
			final ServerPlayerEntity player,
			final E data,
			final PacketSender responseSender) throws OffThreadException {

		if (!engine.isOnThread()) {
			engine.executeSync(() -> {
				if (listener.getConnection().isOpen()) {
					try {
						packet.run(client, handler, player, data, responseSender);
					}
					catch (final Exception exception) {
						if (listener.shouldCrashOnException()) {
							throw exception;
						}
						Amazia.LOGGER.error("Failed to handle packet, suppressing error");
					}
				} else {
					Amazia.LOGGER.debug("Ignoring packet due to disconnection");
				}
			});
			//throw OffThreadException.INSTANCE;
		}
	}

	@Environment(EnvType.CLIENT)
	@FunctionalInterface
	public interface PlayRunHandler <E> {
		void run(MinecraftClient client, ClientPlayNetworkHandler handler, E buf, PacketSender responseSender);
	}

	@FunctionalInterface
	public interface ServerPlayRunHandler <E> {
		void run(MinecraftServer server, ServerPlayNetworkHandler handler, ServerPlayerEntity player, E buf, PacketSender responseSender);
	}
}
