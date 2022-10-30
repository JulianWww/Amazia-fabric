package net.denanu.amazia.commands.economy;

import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.commands.economy.value.AmaziaEconomyValueCommands;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class AmaziaEconomyCommand {

	public static LiteralArgumentBuilder<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment env) {		
		LiteralArgumentBuilder<ServerCommandSource> value = literal("economy");
		
		value.then(AmaziaEconomyValueCommands.register(dispatcher, access, env));
		value.then(literal("reset").executes(AmaziaEconomyCommand::reset));
		
		return value;
	}
	
	private static int reset(CommandContext<ServerCommandSource> context) {
		Amazia.economy.reset();
		return 1;
	}
}
