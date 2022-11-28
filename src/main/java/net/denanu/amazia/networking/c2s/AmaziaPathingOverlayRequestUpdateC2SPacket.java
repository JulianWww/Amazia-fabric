package net.denanu.amazia.networking.c2s;

import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.networking.AmaziaNetworking;
import net.denanu.amazia.networking.NetworkingUtils;
import net.denanu.amazia.networking.s2c.AmaziaPathingOverlayUpdateS2CPacket;
import net.denanu.amazia.networking.s2c.data.AmaziaPathingOverlayUpdateS2SPacketData;
import net.denanu.amazia.pathing.node.BasePathingNode;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class AmaziaPathingOverlayRequestUpdateC2SPacket {
	public static void receive(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {

		final AmaziaPathingOverlayUpdateS2SPacketData data = new AmaziaPathingOverlayUpdateS2SPacketData(buf);
		NetworkingUtils.ServerforceMainThread(AmaziaPathingOverlayRequestUpdateC2SPacket::run, player.networkHandler, server, server, handler, player, data, responseSender);
	}

	public static void run(final MinecraftServer server, final ServerPlayNetworkHandler handler, final ServerPlayerEntity player, final AmaziaPathingOverlayUpdateS2SPacketData data, final PacketSender responseSender) {
		if (player.getWorld().isChunkLoaded(data.villagePos)) {
			final BlockEntity entity = player.getWorld().getBlockEntity(data.villagePos);
			if (entity instanceof final VillageCoreBlockEntity core) {
				final BasePathingNode node = core.getVillage().getPathingGraph().getNode(data.nodePos);
				if (node != null) {
					ServerPlayNetworking.send(player, AmaziaNetworking.PATHING_OVERLAY_UPDATE_S2C, AmaziaPathingOverlayUpdateS2CPacket.toBuf(node));
				}
			}
		}
	}

	public static PacketByteBuf toBuf(final BlockPos node, final BlockPos village) {
		final PacketByteBuf buf = PacketByteBufs.create();

		buf.writeBlockPos(village);
		buf.writeBlockPos(node);

		return buf;
	}
}
