package net.denanu.amazia.networking.s2c;

import java.util.ArrayList;

import net.denanu.amazia.GUI.debug.VillagePathingOverlay;
import net.denanu.amazia.pathing.node.ClientPathingNode;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class AmaziaPathingOverlayUpdateS2CPacket {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		final BlockPos pos = buf.readBlockPos();
		final ArrayList<BlockPos> ajacents = buf.readCollection(ArrayList<BlockPos>::new, PacketByteBuf::readBlockPos);

		final ClientPathingNode node =new ClientPathingNode(ajacents, pos);

		VillagePathingOverlay.addNode(node);
	}
}
