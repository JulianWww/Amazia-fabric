package net.denanu.amazia.networking.c2s;

import net.denanu.amazia.networking.AmaziaNetworking;
import net.denanu.amazia.networking.s2c.AmaziaDataSetterS2C;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class AmaziaRequestNamingSystemC2SPacket {
	// Serveronly
	public static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		ServerPlayNetworking.send(player, AmaziaNetworking.S2C.GET_NAME_GENERATOR, AmaziaDataSetterS2C.toGetNameSystemBuffer());
	}

	public static PacketByteBuf toBuf() {
		return PacketByteBufs.create();
	}

	public static void send() {
		ClientPlayNetworking.send(AmaziaNetworking.C2S.GET_NAME_GENERATOR, AmaziaRequestNamingSystemC2SPacket.toBuf());
	}
}
