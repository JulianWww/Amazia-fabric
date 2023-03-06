package net.denanu.amazia.economy;

import java.util.ArrayList;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;

public class AmaziaTradeOfferList extends ArrayList<AmaziaTradeOffer> {
	@java.io.Serial
	private static final long serialVersionUID = 24374378672934709L;

	public AmaziaTradeOfferList() {}

	public AmaziaTradeOfferList(final int size) {
		super(size);
	}

	public AmaziaTradeOfferList(final NbtCompound nbt) {
		final NbtList nbtList = nbt.getList("Recipes", NbtElement.COMPOUND_TYPE);
		for (int i = 0; i < nbtList.size(); ++i) {
			this.add(new AmaziaTradeOffer(nbtList.getCompound(i)));
		}
	}

	public NbtCompound toNbt() {
		final NbtCompound nbtCompound = new NbtCompound();
		final NbtList nbtList = new NbtList();
		for (final AmaziaTradeOffer element : this) {
			nbtList.add(element.toNbt());
		}
		nbtCompound.put("Recipes", nbtList);
		return nbtCompound;
	}

	public PacketByteBuf toPacket(final PacketByteBuf buf) {
		buf.writeCollection(this, (buf2, offer) -> {
			buf2.writeItemStack(offer.item);
			buf2.writeFloat(offer.modifiedValue);
			buf2.writeBoolean(offer.buy);
			buf2.writeBoolean(offer.valid);
		});
		return buf;
	}

	public static AmaziaTradeOfferList fromPacketBuf(final PacketByteBuf buf) {
		return buf.readCollection(AmaziaTradeOfferList::new, buf2 -> {
			final ItemStack stack = buf2.readItemStack();
			final float modifiedValue = buf2.readFloat();
			final boolean buy = buf2.readBoolean();
			final boolean valid = buf2.readBoolean();
			return new AmaziaTradeOffer(stack, modifiedValue, buy, valid);
		});
	}

	public static AmaziaTradeOfferList fromPacket(final PacketByteBuf buf) {
		return AmaziaTradeOfferList.fromPacketBuf(buf).setup();
	}

	private AmaziaTradeOfferList setup() {
		int idx = 0;
		for (final AmaziaTradeOffer element : this) {
			element.offerId = idx;
			idx++;
		}
		return this;
	}

	@Nullable
	public AmaziaTradeOffer getValidOffer(final ItemStack firstBuyItem, final ItemStack secondBuyItem, final int index) {
		if (index > 0 && index < this.size()) {
			final AmaziaTradeOffer tradeOffer = this.get(index);
			if (tradeOffer.matchesBuyItems(firstBuyItem, secondBuyItem)) {
				return tradeOffer;
			}
			return null;
		}
		for (final AmaziaTradeOffer tradeOffer2 : this) {
			if (!tradeOffer2.matchesBuyItems(firstBuyItem, secondBuyItem)) {
				continue;
			}
			return tradeOffer2;
		}
		return null;
	}

	public void update(final LivingEntity merchant) {
		for (final AmaziaTradeOffer offer : this) {
			offer.setPrice(Economy.getItem(offer.getKey()).getCurrentPrice());
		}
	}

	public void finalize(final LivingEntity merchant) {
		for (final AmaziaTradeOffer offer : this) {
			offer.finalze(merchant);
		}
	}

}
