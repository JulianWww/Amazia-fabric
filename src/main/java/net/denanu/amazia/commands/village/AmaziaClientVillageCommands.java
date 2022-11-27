package net.denanu.amazia.commands.village;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.denanu.amazia.commands.village.pathing.AmaziaClientVillagePathingCommands;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class AmaziaClientVillageCommands {
	public static LiteralArgumentBuilder<ServerCommandSource> register(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess access) {
		final LiteralArgumentBuilder<ServerCommandSource> namespace = CommandManager.literal("village");

		namespace.then(AmaziaClientVillagePathingCommands.register(dispatcher, access));

		return namespace;
	}
}
