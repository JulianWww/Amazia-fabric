package net.denanu.amazia.commands.economy;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.amazia.commands.economy.item.AmaziaEconomyItemCommands;
import net.denanu.amazia.commands.economy.modifier.AmaziaPriceModifierCommands;
import net.denanu.amazia.economy.Economy;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class AmaziaEconomyCommand {

	public static LiteralArgumentBuilder<ServerCommandSource> register(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess access, final CommandManager.RegistrationEnvironment env) {
		final LiteralArgumentBuilder<ServerCommandSource> value = CommandManager.literal("economy");

		value.then(AmaziaEconomyItemCommands.register(dispatcher, access, env));
		value.then(AmaziaPriceModifierCommands.register(dispatcher, access, env));

		value.then(CommandManager.literal("reset").executes(AmaziaEconomyCommand::reset));

		return value;
	}

	private static int reset(final CommandContext<ServerCommandSource> context) {
		Economy.reset();
		return 1;
	}
}
