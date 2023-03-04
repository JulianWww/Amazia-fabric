package net.denanu.amazia.networking.s2c;

import net.denanu.amazia.compat.malilib.NamingLanguageOptions;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class AmaziaGetNameingSystemS2CPacket {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		NamingLanguageOptions.NAMINGLANGUAGE = buf.readEnumConstant(NamingLanguageOptions.class);
	}

	public static PacketByteBuf toBuf() {
		final PacketByteBuf buf = PacketByteBufs.create();
		buf.writeEnumConstant(NamingLanguageOptions.NAMINGLANGUAGE);
		return buf;
	}
}
