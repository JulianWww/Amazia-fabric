package net.denanu.amazia.networking.s2c;

import net.denanu.amazia.JJUtils;
import net.denanu.amazia.entities.moods.ClientMoodHandler;
import net.denanu.amazia.entities.moods.VillagerMoods;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;


public class AmaziaEntityMoodS2CPacket {
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {

		final Entity entity = JJUtils.getEntityByUniqueId(buf.readUuid(), client.world);
		if (entity instanceof final LivingEntity living) {
			final VillagerMoods mood = buf.readEnumConstant(VillagerMoods.class);

			ClientMoodHandler.handleMood(living, mood);
		}
	}
}
