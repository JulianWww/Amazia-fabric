package net.denanu.amazia.networking.s2c;

import java.util.ArrayList;

import net.denanu.amazia.GUI.debug.VillagePathingOverlay;
import net.denanu.amazia.pathing.node.ClientPathingNode;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class AmaziaSetupVillageReneringS2CPacker {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		try {
			VillagePathingOverlay.sem.acquire();
			VillagePathingOverlay.render = buf.readBoolean();
			if (VillagePathingOverlay.render) {
				final long key = buf.readLong();
				if (key != VillagePathingOverlay.id) {
					VillagePathingOverlay.nodes.clear();
					VillagePathingOverlay.id = key;
				}
				buf.readCollection(ArrayList<Byte>::new, buf2 -> {
					final BlockPos pos = buf.readBlockPos();
					final ArrayList<BlockPos> ajacents = buf.readCollection(ArrayList<BlockPos>::new, PacketByteBuf::readBlockPos);
					ajacents.trimToSize();
					final ClientPathingNode node = new ClientPathingNode(ajacents, pos);
					VillagePathingOverlay.nodes.add(node);
					return null;
				});
			}
			VillagePathingOverlay.sem.release();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}
}
