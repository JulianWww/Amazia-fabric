package net.denanu.amazia.commands.args;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.commands.args.serializers.EconomyModiferArgumentSerializer;
import net.denanu.amazia.commands.args.types.EconomyModifierArgumentType;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.util.Identifier;

public class AmaziaArgumentTypes {
	public static void setup() {
		ArgumentTypeRegistry.registerArgumentType(Identifier.of(Amazia.MOD_ID, "economy:modifier"), EconomyModifierArgumentType.class, new EconomyModiferArgumentSerializer());
	}
}
