package net.denanu.amazia.networking.s2c;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.entities.moods.VillagerMoods;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;

public class AmaziaEntityMoodS2CPacket {
	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
			PacketByteBuf buf, PacketSender responseSender) {
		Amazia.LOGGER.info("recived mood");
	}
	
	public static PacketByteBuf toBuf(Entity entity, VillagerMoods mood) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeUuid(entity.getUuid());
		buf.writeEnumConstant(mood);
		return buf;
	}
}
