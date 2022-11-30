package net.denanu.amazia.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.networking.AmaziaNetworking;
import net.denanu.amazia.networking.s2c.VillageExistanceUpdateS2CPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	@Inject(method = "<init>", at = @At("RETURN"))
	public void acceptPlayerMixin(final MinecraftServer server, final ClientConnection connection, final ServerPlayerEntity player, final CallbackInfo info) {
		if (!Amazia.getVillageManager().get().isEmpty()) {
			ServerPlayNetworking.send(
					player,
					AmaziaNetworking.VILLAGE_BOUNDNG_BOX_UPDATE,
					VillageExistanceUpdateS2CPacket.toAddBuf(
							Amazia.getVillageManager().get()
							)
					);
		}
	}
}
