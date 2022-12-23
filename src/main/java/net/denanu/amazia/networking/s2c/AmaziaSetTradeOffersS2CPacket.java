package net.denanu.amazia.networking.s2c;

import net.denanu.amazia.GUI.TradingScreenHandler;
import net.denanu.amazia.economy.AmaziaTradeOfferList;
import net.denanu.amazia.networking.NetworkingUtils;
import net.denanu.amazia.networking.s2c.data.AmaziaSetTradeOffersS2CPacketData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;

public class AmaziaSetTradeOffersS2CPacket {
	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
			PacketByteBuf buf, PacketSender responseSender) {
		
		AmaziaSetTradeOffersS2CPacketData data = new AmaziaSetTradeOffersS2CPacketData(buf);
		NetworkingUtils.forceMainThread(AmaziaSetTradeOffersS2CPacket::run, client.getNetworkHandler(), client, client, handler, data, responseSender);
	}
	
	public static void run(MinecraftClient client, ClientPlayNetworkHandler handler, AmaziaSetTradeOffersS2CPacketData data, PacketSender responseSender) {
        ScreenHandler screenHandler = client.player.currentScreenHandler;
        if (data.sync == screenHandler.syncId && screenHandler instanceof TradingScreenHandler scree) {
            
        	scree.setOffers(data.offers);
        }
	}

	public static PacketByteBuf toBuf(int syncId, AmaziaTradeOfferList offers) {
		PacketByteBuf buf = PacketByteBufs.create();
		
		buf.writeInt(syncId);
		return offers.toPacket(buf);
	}
}
