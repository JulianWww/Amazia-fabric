package net.denanu.amazia.economy;

import java.util.Collection;
import java.util.OptionalInt;

import net.denanu.amazia.GUI.TradingScreenHandler;
import net.denanu.amazia.networking.AmaziaNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public interface IAmaziaMerchant {
	public float getProfitMargin();
	
	public Collection<String> getTradePossibilities();
	
	public AmaziaTradeOfferList getOffers();
	
	public void setOffersFromServer(AmaziaTradeOfferList offers);
	
	public void setCustomer(PlayerEntity player);
	
	default public void sendOffers(PlayerEntity player2, AmaziaTradeOfferList trades, Text name, LivingEntity merchant) {
		trades.finalize(merchant);
		OptionalInt optionalInt = player2.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> new TradingScreenHandler(syncId, playerInventory, this), name));
		if (optionalInt.isPresent() && !trades.isEmpty()) {
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeInt(optionalInt.getAsInt());
			ServerPlayNetworking.send((ServerPlayerEntity) (player2), AmaziaNetworking.SET_TRADE_OFFERS, trades.toPacket(buf));
        }
    }

	public boolean isClient();

	public void onSellingItem(ItemStack stack);

	public PlayerEntity getCustomer();

	public void trade(AmaziaTradeOffer tradeOffer);
}
