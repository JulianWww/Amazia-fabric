package net.denanu.amazia.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.amazia.commands.economy.AmaziaEconomyCommand;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class AmaziaCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment env) {
		LiteralArgumentBuilder<ServerCommandSource> current = literal("amazia").executes(AmaziaCommand::run);
		
		current.then(AmaziaEconomyCommand.register(dispatcher, access, env));
		
		dispatcher.register(current);
	}
	
	public static int run(CommandContext<ServerCommandSource> context) {
		context.getSource().sendMessage(Text.translatable("message.amazia.command.hello"));
		return 1;
	}

	public static LiteralArgumentBuilder<ServerCommandSource> getLiteral(LiteralArgumentBuilder<ServerCommandSource> parent, String string) {
		LiteralArgumentBuilder<ServerCommandSource> current = literal(string);
		parent.then(current);
		return null;
	}
}
