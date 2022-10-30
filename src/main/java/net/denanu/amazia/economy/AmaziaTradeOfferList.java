package net.denanu.amazia.economy;

import java.util.ArrayList;

import org.jetbrains.annotations.Nullable;

import net.denanu.amazia.Amazia;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.village.TradeOffer;

public class AmaziaTradeOfferList extends ArrayList<AmaziaTradeOffer> {
	@java.io.Serial
    private static final long serialVersionUID = 24374378672934709L;
	
	public AmaziaTradeOfferList() {}
	
	public AmaziaTradeOfferList(int size) {
        super(size);
    }
	
	public AmaziaTradeOfferList(NbtCompound nbt) {
        NbtList nbtList = nbt.getList("Recipes", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < nbtList.size(); ++i) {
            this.add(new AmaziaTradeOffer(nbtList.getCompound(i)));
        }
    }
	
	public NbtCompound toNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        NbtList nbtList = new NbtList();
        for (int i = 0; i < this.size(); ++i) {
            nbtList.add(this.get(i).toNbt());
        }
        nbtCompound.put("Recipes", nbtList);
        return nbtCompound;
    }
	
	public PacketByteBuf toPacket(PacketByteBuf buf) {
        buf.writeCollection(this, (buf2, offer) -> {
            buf2.writeItemStack(offer.item);
            buf2.writeFloat(offer.value);
            buf2.writeBoolean(offer.buy);
        });
        return buf;
    }

    public static AmaziaTradeOfferList fromPacketBuf(PacketByteBuf buf) {
        return buf.readCollection(AmaziaTradeOfferList::new, buf2 -> {
        	ItemStack stack = buf2.readItemStack();
        	float value = buf2.readFloat();
        	boolean buy = buf2.readBoolean();
            return new AmaziaTradeOffer(stack, value, buy);
        });
    }
    
    public static AmaziaTradeOfferList fromPacket(PacketByteBuf buf) {
    	return fromPacketBuf(buf).setup();
    }

	private AmaziaTradeOfferList setup() {
		int idx = 0;
		for (AmaziaTradeOffer element : this) {
			element.offerId = idx++;
		}
		return this;
	}

	@Nullable
    public AmaziaTradeOffer getValidOffer(ItemStack firstBuyItem, ItemStack secondBuyItem, int index) {
        if (index > 0 && index < this.size()) {
        	AmaziaTradeOffer tradeOffer = (AmaziaTradeOffer)this.get(index);
            if (tradeOffer.matchesBuyItems(firstBuyItem, secondBuyItem)) {
                return tradeOffer;
            }
            return null;
        }
        for (int i = 0; i < this.size(); ++i) {
        	AmaziaTradeOffer tradeOffer2 = (AmaziaTradeOffer)this.get(i);
            if (!tradeOffer2.matchesBuyItems(firstBuyItem, secondBuyItem)) continue;
            return tradeOffer2;
        }
        return null;
    }

	public void update() {
		for (AmaziaTradeOffer offer : this) {
			offer.setPrice(Amazia.economy.getItem(offer.getKey()).getCurrentPrice());
		}
	}

}
