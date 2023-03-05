package net.denanu.amazia.networking;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.networking.c2s.AmaziaMerchantTradeSelectC2SPacket;
import net.denanu.amazia.networking.c2s.AmaziaPathingOverlayRequestUpdateC2SPacket;
import net.denanu.amazia.networking.c2s.AmaziaRequestNamingSystemC2SPacket;
import net.denanu.amazia.networking.c2s.UpdateNamingSystemC2SPacket;
import net.denanu.amazia.networking.s2c.AmaziaCustomChatMessageS2CPacket;
import net.denanu.amazia.networking.s2c.AmaziaEntityMoodS2CPacket;
import net.denanu.amazia.networking.s2c.AmaziaGetNameingSystemS2CPacket;
import net.denanu.amazia.networking.s2c.AmaziaPathingOverlayUpdateS2CPacket;
import net.denanu.amazia.networking.s2c.AmaziaSetTradeOffersS2CPacket;
import net.denanu.amazia.networking.s2c.AmaziaSetupVillageReneringS2CPacker;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class AmaziaNetworking {
	public static class C2S {
		public static final Identifier MERCHANT_TRADE_SELECT = new Identifier(Amazia.MOD_ID, "trade_select");
		public static final Identifier PATHING_OVERLAY_UPDATE = Identifier.of(Amazia.MOD_ID, "pathing_cs");
		public static final Identifier CHANGE_NAME_GENERATOR = Identifier.of(Amazia.MOD_ID, "change_name");
		public static final Identifier GET_NAME_GENERATOR = Identifier.of(Amazia.MOD_ID, "get_name");
	}

	public static class S2C {
		public static final Identifier SET_TRADE_OFFERS = new Identifier(Amazia.MOD_ID, "set_trade_offers");
		public static final Identifier MOOD_UPDATE = Identifier.of(Amazia.MOD_ID, "mood");
		public static final Identifier PATHING_OVERLAY_UPDATE = Identifier.of(Amazia.MOD_ID, "pathing_sc");
		public static final Identifier SETUP_PATHINGOVERLAY = Identifier.of(Amazia.MOD_ID, "setup_pathing");
		public static final Identifier GET_NAME_GENERATOR = Identifier.of(Amazia.MOD_ID, "get_name");
		public static final Identifier SEND_CUSTOM_CHAT = Identifier.of(Amazia.MOD_ID, "custom_chat");
	}


	public static void registerC2SPackets() {
		ServerPlayNetworking.registerGlobalReceiver(AmaziaNetworking.C2S.MERCHANT_TRADE_SELECT, AmaziaMerchantTradeSelectC2SPacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(AmaziaNetworking.C2S.PATHING_OVERLAY_UPDATE, AmaziaPathingOverlayRequestUpdateC2SPacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(AmaziaNetworking.C2S.CHANGE_NAME_GENERATOR, UpdateNamingSystemC2SPacket::receive);
		ServerPlayNetworking.registerGlobalReceiver(AmaziaNetworking.C2S.GET_NAME_GENERATOR, AmaziaRequestNamingSystemC2SPacket::receive);
	}

	public static void registerS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(AmaziaNetworking.S2C.SET_TRADE_OFFERS, AmaziaSetTradeOffersS2CPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(AmaziaNetworking.S2C.MOOD_UPDATE, AmaziaEntityMoodS2CPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(AmaziaNetworking.S2C.PATHING_OVERLAY_UPDATE, 	AmaziaPathingOverlayUpdateS2CPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(AmaziaNetworking.S2C.SETUP_PATHINGOVERLAY, AmaziaSetupVillageReneringS2CPacker::receive);
		ClientPlayNetworking.registerGlobalReceiver(AmaziaNetworking.S2C.GET_NAME_GENERATOR, AmaziaGetNameingSystemS2CPacket::receive);
		ClientPlayNetworking.registerGlobalReceiver(AmaziaNetworking.S2C.SEND_CUSTOM_CHAT, AmaziaCustomChatMessageS2CPacket::receive);
	}
}
