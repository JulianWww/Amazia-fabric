package net.denanu.amazia.networking;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.networking.c2s.AmaziaMerchantTradeSelectC2SPacket;
import net.denanu.amazia.networking.c2s.AmaziaPathingOverlayRequestUpdateC2SPacket;
import net.denanu.amazia.networking.s2c.AmaziaEntityMoodS2CPacket;
import net.denanu.amazia.networking.s2c.AmaziaPathingOverlayUpdateS2CPacket;
import net.denanu.amazia.networking.s2c.AmaziaSetTradeOffersS2CPacket;
import net.denanu.amazia.networking.s2c.AmaziaSetupVillageReneringS2CPacker;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class AmaziaNetworking {
	public static final Identifier SET_TRADE_OFFERS = new Identifier(Amazia.MOD_ID, "set_trade_offers");
	public static final Identifier MERCHANT_TRADE_SELECT = new Identifier(Amazia.MOD_ID, "trade_select");
	public static final Identifier MOOD_UPDATE = Identifier.of(Amazia.MOD_ID, "mood");
	public static final Identifier PATHING_OVERLAY_UPDATE_S2C = Identifier.of(Amazia.MOD_ID, "pathing_sc");
	public static final Identifier PATHING_OVERLAY_UPDATE_C2S = Identifier.of(Amazia.MOD_ID, "pathing_cs");
	public static final Identifier SETUP_PATHINGOVERLAY = Identifier.of(Amazia.MOD_ID, "setup_pathing");


	public static void registerC2SPackets() {
		ServerPlayNetworking.registerGlobalReceiver(AmaziaNetworking.MERCHANT_TRADE_SELECT, AmaziaMerchantTradeSelectC2SPacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(AmaziaNetworking.PATHING_OVERLAY_UPDATE_C2S, AmaziaPathingOverlayRequestUpdateC2SPacket::receive);
	}

	public static void registerS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(AmaziaNetworking.SET_TRADE_OFFERS, AmaziaSetTradeOffersS2CPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(AmaziaNetworking.MOOD_UPDATE, AmaziaEntityMoodS2CPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(AmaziaNetworking.MERCHANT_TRADE_SELECT, AmaziaPathingOverlayUpdateS2CPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(AmaziaNetworking.PATHING_OVERLAY_UPDATE_S2C, AmaziaPathingOverlayUpdateS2CPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(AmaziaNetworking.SETUP_PATHINGOVERLAY, AmaziaSetupVillageReneringS2CPacker::receive);
	}
}
