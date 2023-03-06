package net.denanu.amazia.entities.moods;

import java.util.List;

import net.denanu.amazia.networking.AmaziaNetworking;
import net.denanu.amazia.networking.s2c.AmaziaDataSetterS2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

public class ServerMoodEmiter {
	private static Box reciveBox = new Box(100,100,100,-100,-100,-100);
	public static void sendMood(final LivingEntity entity, final VillagerMoods mood) {
		final PacketByteBuf buf = AmaziaDataSetterS2C.toMoodBuf(entity, mood);
		final List<PlayerEntity> players = ((ServerWorld)entity.world).getPlayers(TargetPredicate.createNonAttackable(), entity, ServerMoodEmiter.reciveBox.offset(entity.getBlockPos()));
		for (final PlayerEntity player : players) {
			ServerPlayNetworking.send((ServerPlayerEntity)player, AmaziaNetworking.S2C.MOOD_UPDATE, buf);
		}
	}
}
