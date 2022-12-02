package net.denanu.amazia.networking.s2c;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Semaphore;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.GUI.renderers.VillageBorderRenderer;
import net.denanu.amazia.village.Village;
import net.denanu.amazia.village.VillageBoundingBox;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

public class VillageExistanceUpdateS2CPacket {
	private static Semaphore sem = new Semaphore(1);
	public static void receive(final MinecraftClient client, final ClientPlayNetworkHandler handler,
			final PacketByteBuf buf, final PacketSender responseSender) {

		final boolean add = buf.readBoolean();
		final ArrayList<VillageBoundingBox> list = buf.readCollection(ArrayList<VillageBoundingBox>::new, VillageBoundingBox::new);
		try {
			VillageExistanceUpdateS2CPacket.sem.acquire();
		} catch (final InterruptedException e) {
			Amazia.LOGGER.error(e.toString());
		}
		if (add) {
			VillageBorderRenderer.villageBoxes.addAll(list);
		}
		else {
			VillageBorderRenderer.villageBoxes.removeAll(list);
		}

		VillageExistanceUpdateS2CPacket.sem.release();
	}

	public static PacketByteBuf toAddBuf(final Collection<Village> villages) {
		final PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(true);
		buf.writeCollection(villages, (buf2, v) -> {
			VillageBoundingBox.toBuf(buf2, v);
		});
		return buf;
	}

	public static PacketByteBuf toRemoveBuf(final Collection<Village> villages) {
		final PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(false);
		buf.writeCollection(villages, (buf2, v) -> {
			VillageBoundingBox.toBuf(buf2, v);
		});
		return buf;
	}
}
