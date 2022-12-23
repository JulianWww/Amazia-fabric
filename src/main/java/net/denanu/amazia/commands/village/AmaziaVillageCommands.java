package net.denanu.amazia.commands.village;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.commands.village.pathing.AmaziaClientVillagePathingCommands;
import net.denanu.amazia.village.events.IVillageEventListener;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;

public class AmaziaVillageCommands {
	public static LiteralArgumentBuilder<ServerCommandSource> register(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess access) {
		final LiteralArgumentBuilder<ServerCommandSource> namespace = CommandManager.literal("village");

		namespace.then(AmaziaClientVillagePathingCommands.register(dispatcher, access));
		namespace.then(CommandManager.literal("highlightEntities")
				.then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
						.then(CommandManager.argument("active", BoolArgumentType.bool())
								.executes(AmaziaVillageCommands::highlightEntities)
								)
						)
				);

		return namespace;
	}

	private static int highlightEntities(final CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		final boolean active = BoolArgumentType.getBool(context, "active");
		final BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
		if (context.getSource().getWorld().getBlockEntity(pos) instanceof final VillageCoreBlockEntity core) {
			for (final IVillageEventListener entity : core.getVillage().getListeners()) {
				entity.setGlowing(active);
			}
		}

		return 1;
	}
}
