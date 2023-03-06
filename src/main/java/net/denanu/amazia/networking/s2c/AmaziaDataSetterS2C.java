package net.denanu.amazia.networking.s2c;

import net.denanu.amazia.compat.malilib.NamingLanguageOptions;
import net.denanu.amazia.economy.AmaziaTradeOfferList;
import net.denanu.amazia.entities.moods.VillagerMoods;
import net.denanu.amazia.pathing.node.BasePathingNode;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class AmaziaDataSetterS2C {
	public static PacketByteBuf toMoodBuf(final Entity entity, final VillagerMoods mood) {
		final PacketByteBuf buf = PacketByteBufs.create();
		buf.writeUuid(entity.getUuid());
		buf.writeEnumConstant(mood);
		return buf;
	}

	public static PacketByteBuf toGetNameSystemBuffer() {
		final PacketByteBuf buf = PacketByteBufs.create();
		buf.writeEnumConstant(NamingLanguageOptions.NAMINGLANGUAGE);
		return buf;
	}

	public static PacketByteBuf toPathingOverlayBuf(final BasePathingNode node) {
		final PacketByteBuf buf = PacketByteBufs.create();

		buf.writeBlockPos(node.getBlockPos());
		buf.writeCollection(node.ajacentNodes, (buf2, pos) -> {
			buf2.writeBlockPos(pos.getBlockPos());
		});

		return buf;
	}

	public static PacketByteBuf toSetTradeOfferBuf(final int syncId, final AmaziaTradeOfferList offers) {
		final PacketByteBuf buf = PacketByteBufs.create();

		buf.writeInt(syncId);
		return offers.toPacket(buf);
	}

	public static PacketByteBuf toSetupBasePathingRendereingBuf(final BlockPos pos, final boolean val) {
		final PacketByteBuf buf = PacketByteBufs.create();

		buf.writeBoolean(val);
		if (val) {
			buf.writeBlockPos(pos);
		}

		return buf;
	}
}
