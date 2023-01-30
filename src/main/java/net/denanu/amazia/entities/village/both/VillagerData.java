package net.denanu.amazia.entities.village.both;

import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class VillagerData {
	private Identifier type;

	public VillagerData(final Identifier type) {
		this.setType(type);
	}

	public VillagerData(final NbtCompound nbt) {
		this.setType(new Identifier(nbt.getString("type")));
	}

	public Identifier getType() {
		return this.type;
	}

	public void setType(final Identifier type) {
		this.type = type;
	}

	public static final TrackedDataHandler<VillagerData> VILLAGER_DATA = new TrackedDataHandler.ImmutableHandler<>(){

		@Override
		public void write(final PacketByteBuf packetByteBuf, final VillagerData villagerData) {
			packetByteBuf.writeIdentifier(villagerData.getType());
		}

		@Override
		public VillagerData read(final PacketByteBuf buf) {
			return new VillagerData(buf.readIdentifier());
		}
	};

	public NbtElement toNbt() {
		final NbtCompound nbt = new NbtCompound();
		nbt.putString("type", this.getType().toString());
		return nbt;
	}
}
