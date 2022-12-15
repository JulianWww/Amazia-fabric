package net.denanu.amazia.commands.village.scedulers;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ScedulingMarkers {
	public static LiteralArgumentBuilder<ServerCommandSource> register(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess access) {
		return CommandManager.literal("sceduling");
	}

	public static void mark(final World world, final Collection<BlockPos> pos, final boolean active) {

	}
}
