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
	float getProfitMargin();

	Collection<String> getTradePossibilities();

	AmaziaTradeOfferList getOffers();

	void setOffersFromServer(AmaziaTradeOfferList offers);

	void setCustomer(PlayerEntity player);

	default void sendOffers(final PlayerEntity player2, final AmaziaTradeOfferList trades, final Text name, final LivingEntity merchant) {
		trades.finalize(merchant);
		final OptionalInt optionalInt = player2.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> new TradingScreenHandler(syncId, playerInventory, this), name));
		if (optionalInt.isPresent() && !trades.isEmpty()) {
			final PacketByteBuf buf = PacketByteBufs.create();
			buf.writeInt(optionalInt.getAsInt());
			ServerPlayNetworking.send((ServerPlayerEntity) player2, AmaziaNetworking.S2C.SET_TRADE_OFFERS, trades.toPacket(buf));
		}
	}

	boolean isClient();

	void onSellingItem(ItemStack stack);

	PlayerEntity getCustomer();

	void trade(AmaziaTradeOffer tradeOffer);
}
