package net.denanu.amazia.networking.s2c;

import net.denanu.amazia.GUI.TradingScreenHandler;
import net.denanu.amazia.networking.NetworkingUtils;
import net.denanu.amazia.networking.s2c.data.AmaziaSetTradeOffersS2CPacketData;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;

public class AmaziaSetTradeOffersS2CPacket {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {

		final AmaziaSetTradeOffersS2CPacketData data = new AmaziaSetTradeOffersS2CPacketData(buf);
		NetworkingUtils.forceMainThread(AmaziaSetTradeOffersS2CPacket::run, client.getNetworkHandler(), client, client, handler, data, responseSender);
	}

	public static void run(final MinecraftClient client, final ClientPlayNetworkHandler handler, final AmaziaSetTradeOffersS2CPacketData data, final PacketSender responseSender) {
		final ScreenHandler screenHandler = client.player.currentScreenHandler;
		if (data.sync == screenHandler.syncId && screenHandler instanceof final TradingScreenHandler scree) {

			scree.setOffers(data.offers);
		}
	}
}
