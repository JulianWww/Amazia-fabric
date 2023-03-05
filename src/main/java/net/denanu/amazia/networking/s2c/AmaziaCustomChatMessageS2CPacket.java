package net.denanu.amazia.networking.s2c;

import java.util.function.Predicate;

import fi.dy.masa.malilib.config.options.ConfigColor;
import net.denanu.amazia.config.Config;
import net.denanu.amazia.networking.AmaziaNetworking;
import net.denanu.amazia.networking.NetworkingUtils;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class AmaziaCustomChatMessageS2CPacket {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		NetworkingUtils.forceMainThread(AmaziaCustomChatMessageS2CPacket::run, client.getNetworkHandler(), client, client, handler, new Data(buf), responseSender);
	}

	public static void run(final MinecraftClient client, final ClientPlayNetworkHandler handler, final Data data, final PacketSender responseSender) {
		client.getMessageHandler().onGameMessage(
				Text.translatable(data.key, data.type.test(data) ? data.name : "").setStyle(Style.EMPTY.withColor(data.type.getColor())
						), false);
	}

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
		AmaziaCustomChatMessageS2CPacket.send(player, AmaziaCustomChatMessageS2CPacket.warningMessage(key, entityName));
	}

	public enum MessageType {
		WARNING(Config.Generic.PROBLEM_COLOR, data -> Config.Generic.SHOW_NAMES_IN_CHAT.getBooleanValue());

		private final ConfigColor color;
		Predicate<Data> pred;

		MessageType(final ConfigColor color, final Predicate<Data> pred) {
			this.color = color;
			this.pred = pred;
		}

		public int getColor() {
			return this.color.getIntegerValue();
		}

		public boolean test(final Data data) {
			return this.pred.test(data);
		}
	}

	private static class Data {
		private final String key;
		private final Text name;
		private final MessageType type;

		private Data(final String key, final Text name, final MessageType type) {
			this.key = key;
			this.name = name;
			this.type = type;
		}

		private Data(final PacketByteBuf buf) {
			this(
					buf.readString(),
					buf.readText(),
					buf.readEnumConstant(MessageType.class));
		}
	}
}
