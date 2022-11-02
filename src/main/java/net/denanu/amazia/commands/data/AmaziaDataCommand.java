package net.denanu.amazia.commands.data;

import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.denanu.amazia.commands.data.scanner.AmaziaDataScannerCommand;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class AmaziaDataCommand {
	public static LiteralArgumentBuilder<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment env) {		
		LiteralArgumentBuilder<ServerCommandSource> value = literal("scanner");
		
		value.then(AmaziaDataScannerCommand.register(dispatcher, access, env));
		
		return value;
	}
}
