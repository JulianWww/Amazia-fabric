package net.denanu.amazia.networking.s2c;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.moods.ClientMoodHandler;
import net.denanu.amazia.entities.moods.VillagerMoods;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;

public class AmaziaEntityMoodS2CPacket {
	public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
			PacketByteBuf buf, PacketSender responseSender) {
		
		Entity entity = JJUtils.getEntityByUniqueId(buf.readUuid(), client.world);
		if (entity instanceof LivingEntity living) {
			VillagerMoods mood = buf.readEnumConstant(VillagerMoods.class);
			
			ClientMoodHandler.handleMood(living, mood);
		}
	}
	
	public static PacketByteBuf toBuf(Entity entity, VillagerMoods mood) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeUuid(entity.getUuid());
		buf.writeEnumConstant(mood);
		return buf;
	}
}
