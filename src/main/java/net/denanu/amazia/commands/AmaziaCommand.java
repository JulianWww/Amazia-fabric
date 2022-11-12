package net.denanu.amazia.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.amazia.commands.data.AmaziaDataCommand;
import net.denanu.amazia.commands.economy.AmaziaEconomyCommand;
import net.denanu.amazia.commands.testSuit.AmaziaTestCommands;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class AmaziaCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment env) {
		LiteralArgumentBuilder<ServerCommandSource> namespace = literal("amazia");
		
		namespace.then(AmaziaEconomyCommand.register(dispatcher, access, env));
		namespace.then(AmaziaDataCommand.register(dispatcher, access, env));
		namespace.then(AmaziaTestCommands.register(dispatcher, access, env));
		
		dispatcher.register(namespace);
	}
}
