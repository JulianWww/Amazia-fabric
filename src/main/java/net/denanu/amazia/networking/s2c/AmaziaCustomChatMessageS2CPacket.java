package net.denanu.amazia.networking.s2c;

import java.util.function.Predicate;

import fi.dy.masa.malilib.config.options.ConfigColor;
import net.denanu.amazia.config.Config;
import net.denanu.amazia.networking.NetworkingUtils;
import net.denanu.amazia.networking.s2c.data.CustomChatMessageData;
import net.denanu.amazia.networking.s2c.data.CustomChatMessageData.Data;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class AmaziaCustomChatMessageS2CPacket {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		NetworkingUtils.forceMainThread(AmaziaCustomChatMessageS2CPacket::run, client.getNetworkHandler(), client, client, handler, new CustomChatMessageData.Data(
				buf.readString(),
				buf.readText(),
				buf.readEnumConstant(MessageType.class)
				), responseSender);
	}

	public static void run(final MinecraftClient client, final ClientPlayNetworkHandler handler, final CustomChatMessageData.Data<MessageType> data, final PacketSender responseSender) {
		client.getMessageHandler().onGameMessage(
				Text.translatable(data.key, data.type.test(data) ? data.name : "").setStyle(Style.EMPTY.withColor(data.type.getColor())
						), false);
	}

	public enum MessageType {
		WARNING(Config.Generic.PROBLEM_COLOR, data -> Config.Generic.SHOW_NAMES_IN_CHAT.getBooleanValue());

		private final ConfigColor color;
		Predicate<Data<MessageType>> pred;

		MessageType(final ConfigColor color, final Predicate<Data<MessageType>> pred) {
			this.color = color;
			this.pred = pred;
		}

		public int getColor() {
			return this.color.getIntegerValue();
		}

		public boolean test(final Data<MessageType> data) {
			return this.pred.test(data);
		}
	}
}
