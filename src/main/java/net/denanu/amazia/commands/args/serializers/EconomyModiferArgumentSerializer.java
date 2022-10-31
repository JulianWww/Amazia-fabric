/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */
package net.denanu.amazia.commands.args.serializers;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.denanu.amazia.commands.args.types.EconomyModifierArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;

public class EconomyModiferArgumentSerializer
implements ArgumentSerializer<EconomyModifierArgumentType, net.denanu.amazia.commands.args.serializers.EconomyModiferArgumentSerializer.Properties> {
    @Override
    public void writePacket(Properties properties, PacketByteBuf packetByteBuf) {
        packetByteBuf.writeString(properties.data);
    }

	@Override
	public Properties fromPacket(PacketByteBuf buf) {
		return new Properties(buf.readString());
	}

	@Override
	public void writeJson(Properties props, JsonObject json) {
		json.addProperty("value", props.data);
	}

	@Override
	public Properties getArgumentTypeProperties(EconomyModifierArgumentType mod) {
		return new Properties(mod.data);
	}
	
	public final class Properties
    implements ArgumentSerializer.ArgumentTypeProperties<EconomyModifierArgumentType> {
    	String data;

        public Properties(String data) {
        	this.data = data;
        }

        //@Override
        public EconomyModifierArgumentType createType(CommandRegistryAccess commandRegistryAccess) {
            return EconomyModifierArgumentType.modifier();
        }

		@Override
		public ArgumentSerializer<EconomyModifierArgumentType, ?> getSerializer() {
			return EconomyModiferArgumentSerializer.this;
		}
    }
}

