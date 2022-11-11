package net.denanu.amazia.networking;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.networking.c2s.AmaziaMerchantTradeSelectC2SPacket;
import net.denanu.amazia.networking.s2c.AmaziaEntityMoodS2CPacket;
import net.denanu.amazia.networking.s2c.AmaziaSetTradeOffersS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class AmaziaNetworking {
	public static final Identifier SET_TRADE_OFFERS = new Identifier(Amazia.MOD_ID, "set_trade_offers");
	public static final Identifier MERCHANT_TRADE_SELECT = new Identifier(Amazia.MOD_ID, "trade_select");
	public static final Identifier MOOD_UPDATE = Identifier.of(Amazia.MOD_ID, "mood");
	
	public static void registerC2SPackets() {
		ServerPlayNetworking.registerGlobalReceiver(MERCHANT_TRADE_SELECT, AmaziaMerchantTradeSelectC2SPacket::receive);
	}
	
	public static void registerS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(SET_TRADE_OFFERS, AmaziaSetTradeOffersS2CPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(MOOD_UPDATE, AmaziaEntityMoodS2CPacket::receive);
	}
}
