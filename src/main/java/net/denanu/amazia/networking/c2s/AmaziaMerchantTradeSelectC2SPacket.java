package net.denanu.amazia.networking.c2s;

import net.denanu.amazia.GUI.TradingScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class AmaziaMerchantTradeSelectC2SPacket {
	public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
			PacketByteBuf buf, PacketSender responseSender) {
		if (player.currentScreenHandler instanceof TradingScreenHandler screen) {
			int idx = buf.readInt();
			
			screen.setRecipeIndex(idx);
			screen.switchTo(idx);
		}
	}
	
	public static PacketByteBuf toBuf(int tradeId) {
		PacketByteBuf buf = PacketByteBufs.create();
		
		buf.writeInt(tradeId);
		
		return buf;
	}
}
