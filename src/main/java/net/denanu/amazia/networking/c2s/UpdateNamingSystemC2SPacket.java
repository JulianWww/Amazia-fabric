package net.denanu.amazia.networking.c2s;

import net.denanu.amazia.compat.malilib.NamingLanguageOptions;
import net.denanu.amazia.networking.AmaziaNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class UpdateNamingSystemC2SPacket {
	// Serveronly
	public static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		NamingLanguageOptions.update(buf.readEnumConstant(NamingLanguageOptions.class));
	}

	public static PacketByteBuf toBuf(final NamingLanguageOptions nameSystem) {
		final PacketByteBuf buf = PacketByteBufs.create();

		buf.writeEnumConstant(nameSystem);

		return buf;
	}

	public static void send(final NamingLanguageOptions nameSystem) {
		ClientPlayNetworking.send(AmaziaNetworking.C2S.CHANGE_NAME_GENERATOR, UpdateNamingSystemC2SPacket.toBuf(nameSystem));
	}
}
