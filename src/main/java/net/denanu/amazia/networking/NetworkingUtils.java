package net.denanu.amazia.networking;

import net.denanu.amazia.Amazia;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.OffThreadException;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.util.thread.ThreadExecutor;

public class NetworkingUtils {
	public static <T extends PacketListener, E> void forceMainThread(PlayRunHandler<E> packet, T listener, ThreadExecutor<?> engine, MinecraftClient client, ClientPlayNetworkHandler handler, E data, PacketSender responseSender) throws OffThreadException {
        if (!engine.isOnThread()) {
            engine.executeSync(() -> {
                if (listener.getConnection().isOpen()) {
                    try {
                        packet.run(client, handler, data, responseSender);
                    }
                    catch (Exception exception) {
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
}
