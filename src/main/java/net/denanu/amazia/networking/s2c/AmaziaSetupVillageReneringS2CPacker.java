package net.denanu.amazia.networking.s2c;

import net.denanu.amazia.GUI.debug.VillagePathingOverlay;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class AmaziaSetupVillageReneringS2CPacker {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {
		VillagePathingOverlay.render = buf.readBoolean();
		if (VillagePathingOverlay.render) {
			VillagePathingOverlay.nodes.clear();
			VillagePathingOverlay.villageLoc = buf.readBlockPos();
			VillagePathingOverlay.queue.add(VillagePathingOverlay.villageLoc.up());
		}
	}
}
