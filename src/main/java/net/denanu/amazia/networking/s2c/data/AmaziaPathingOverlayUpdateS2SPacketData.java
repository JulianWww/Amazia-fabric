package net.denanu.amazia.networking.s2c.data;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class AmaziaPathingOverlayUpdateS2SPacketData {
	public final BlockPos villagePos;
	public final BlockPos nodePos;

	public AmaziaPathingOverlayUpdateS2SPacketData(final PacketByteBuf buf) {
		this.villagePos = buf.readBlockPos();
		this.nodePos = buf.readBlockPos();
	}
}
