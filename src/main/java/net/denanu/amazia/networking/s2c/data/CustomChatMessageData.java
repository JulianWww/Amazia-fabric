package net.denanu.amazia.networking.s2c.data;

import net.denanu.amazia.networking.AmaziaNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CustomChatMessageData {
	private static PacketByteBuf warningMessage(final String key, final Text entityName) {
		final PacketByteBuf buf = PacketByteBufs.create();

		buf.writeString(key);
		buf.writeText(entityName);
		buf.writeEnumConstant(MessageType.WARNING);

		return buf;
	}

	private static void send(final ServerPlayerEntity player, final PacketByteBuf buf) {
		ServerPlayNetworking.send(player, AmaziaNetworking.S2C.SEND_CUSTOM_CHAT, buf);
	}

	public static void sendWarning(final ServerPlayerEntity player, final String key, final Text entityName) {
		CustomChatMessageData.send(player, CustomChatMessageData.warningMessage(key, entityName));
	}

	public enum MessageType {
		WARNING;
	}

	public static class Data<T> {
		public final String key;
		public final Text name;
		public final T type;

		public Data(final String key, final Text name, final T type) {
			this.key = key;
			this.name = name;
			this.type = type;
		}
	}
}
