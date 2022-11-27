package net.denanu.amazia.commands.village.pathing;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.amazia.networking.AmaziaNetworking;
import net.denanu.amazia.networking.s2c.AmaziaSetupVillageReneringS2CPacker;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class AmaziaClientVillagePathingCommands {
	public static LiteralArgumentBuilder<ServerCommandSource> register(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess access) {
		final LiteralArgumentBuilder<ServerCommandSource> namespace = CommandManager.literal("pathing");

		namespace.then(CommandManager.literal("showBase")
				.then(CommandManager.argument("villagePos", BlockPosArgumentType.blockPos())
						.then(CommandManager.argument("value", BoolArgumentType.bool())
								.executes(AmaziaClientVillagePathingCommands::renderBasePathing)
								)
						)
				);

		return namespace;
	}

	private static int renderBasePathing(final CommandContext<ServerCommandSource> context) {
		try {
			final boolean val = BoolArgumentType.getBool(context, "value");
			BlockPos pos = null;
			if (val) {
				pos = BlockPosArgumentType.getBlockPos(context, "villagePos");
				context.getSource().sendFeedback(Text.translatable("messages.amazia.commands.enabled_pathing_render"), false);
			}
			else {
				context.getSource().sendFeedback(Text.translatable("messages.amazia.commands.disabled_pathing_render"), false);
			}
			ServerPlayNetworking.send(context.getSource().getPlayer(), AmaziaNetworking.SETUP_PATHINGOVERLAY, AmaziaSetupVillageReneringS2CPacker.toBuf(pos, val));
		}
		catch (final Exception e){
			return -1;
		}
		return 1;
	}
}
