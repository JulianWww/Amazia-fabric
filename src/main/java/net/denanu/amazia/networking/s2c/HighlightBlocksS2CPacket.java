package net.denanu.amazia.networking.s2c;

import java.util.ArrayList;
import java.util.Collection;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class HighlightBlocksS2CPacket {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {

		buf.readBoolean();
		Collection<BlockPos> poses = buf.readCollection(ArrayList<BlockPos>::new, PacketByteBuf::readBlockPos);

		for (BlockPos pos : poses) {
			//BlockHighlighting.highlightBlock(client, pos);
		}

	}

	public static PacketByteBuf toBuf(final Collection<BlockPos> poses, final boolean active) {
		final PacketByteBuf buf = PacketByteBufs.create();

		buf.writeBoolean(active);
		buf.writeCollection(poses, PacketByteBuf::writeBlockPos);

		return buf;
	}
}
