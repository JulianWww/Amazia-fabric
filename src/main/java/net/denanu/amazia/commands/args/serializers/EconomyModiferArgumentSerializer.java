/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.denanu.amazia.commands.args.serializers;

import com.google.gson.JsonObject;

import net.denanu.amazia.commands.args.types.EconomyModifierArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;

public class EconomyModiferArgumentSerializer
implements ArgumentSerializer<EconomyModifierArgumentType, net.denanu.amazia.commands.args.serializers.EconomyModiferArgumentSerializer.Properties> {
	@Override
	public void writePacket(final Properties properties, final PacketByteBuf packetByteBuf) {
		packetByteBuf.writeString(properties.data == null ? "" : properties.data);
	}

	@Override
	public Properties fromPacket(final PacketByteBuf buf) {
		return new Properties(buf.readString());
	}

	@Override
	public void writeJson(final Properties props, final JsonObject json) {
		json.addProperty("value", props.data);
	}

	@Override
	public Properties getArgumentTypeProperties(final EconomyModifierArgumentType mod) {
		return new Properties(mod.data);
	}

	public final class Properties
	implements ArgumentSerializer.ArgumentTypeProperties<EconomyModifierArgumentType> {
		String data;

		public Properties(final String data) {
			this.data = data;
		}

		//@Override
		@Override
		public EconomyModifierArgumentType createType(final CommandRegistryAccess commandRegistryAccess) {
			return EconomyModifierArgumentType.modifier();
		}

		@Override
		public ArgumentSerializer<EconomyModifierArgumentType, ?> getSerializer() {
			return EconomyModiferArgumentSerializer.this;
		}
	}
}

