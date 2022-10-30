package net.denanu.amazia.networking.s2c.data;

import net.denanu.amazia.economy.AmaziaTradeOfferList;
import net.minecraft.network.PacketByteBuf;

public class AmaziaSetTradeOffersS2CPacketData {
	public final int sync;
	public final AmaziaTradeOfferList offers;
	
	public AmaziaSetTradeOffersS2CPacketData(PacketByteBuf buf) {
		this.sync = buf.readInt();
		this.offers = AmaziaTradeOfferList.fromPacket(buf);
	}
}
