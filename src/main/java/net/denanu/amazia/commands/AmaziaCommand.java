package net.denanu.amazia.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.denanu.amazia.commands.data.AmaziaDataCommand;
import net.denanu.amazia.commands.economy.AmaziaEconomyCommand;
import net.denanu.amazia.commands.testSuit.AmaziaTestCommands;
import net.denanu.amazia.commands.village.AmaziaClientVillageCommands;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class AmaziaCommand {
	public static void register(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess access, final CommandManager.RegistrationEnvironment env) {
		final LiteralArgumentBuilder<ServerCommandSource> namespace = CommandManager.literal("amazia");

		namespace.then(AmaziaEconomyCommand.register(dispatcher, access, env));
		namespace.then(AmaziaDataCommand.register(dispatcher, access, env));
		namespace.then(AmaziaTestCommands.register(dispatcher, access, env));
		namespace.then(AmaziaClientVillageCommands.register(dispatcher, access));

		dispatcher.register(namespace);
	}
}
