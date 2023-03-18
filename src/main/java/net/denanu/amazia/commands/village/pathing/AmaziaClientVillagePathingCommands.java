package net.denanu.amazia.commands.village.pathing;

import java.util.Collection;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.networking.AmaziaNetworking;
import net.denanu.amazia.networking.s2c.AmaziaDataSetterS2C;
import net.denanu.amazia.pathing.interfaces.PathingEventListener;
import net.denanu.amazia.pathing.node.BasePathingNode;
import net.denanu.amazia.village.Village;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
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
		namespace.then(CommandManager.literal("getConnection")
				.then(CommandManager.argument("villagePos", BlockPosArgumentType.blockPos())
						.then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
								.executes(AmaziaClientVillagePathingCommands::getConnections)
								)
						)
				);
		namespace.then(CommandManager.literal("showAccessPoints")
				.then(CommandManager.argument("villagePos", BlockPosArgumentType.blockPos())
						.executes(AmaziaClientVillagePathingCommands::showAccesPoints)
						)
				);

		return namespace;
	}

	private static int getConnections(final CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		final BlockPos village = BlockPosArgumentType.getBlockPos(context, "villagePos");
		final BlockPos pos     = BlockPosArgumentType.getBlockPos(context, "pos");

		if (context.getSource().getWorld().getBlockEntity(village) instanceof final VillageCoreBlockEntity core) {
			final BasePathingNode node = core.getVillage().getPathingGraph().getNode(pos.up());
			if (node != null) {
				final StringBuilder builder = new StringBuilder();

				for (final BasePathingNode aj : node.ajacentNodes) {
					builder.append(aj.toString()).append(" ");
				}

				context.getSource().sendFeedback(Text.literal(builder.toString()), false);
				return 1;
			}
		}
		return 0;
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
			final long id = context.getSource().getWorld().getTime();
			Amazia.LOGGER.info(Long.toString(id));
			final List<PacketByteBuf> packets = AmaziaDataSetterS2C.toSetupBasePathingRendereingBuf(context, pos, val, id);
			for (final PacketByteBuf packet : packets) {
				ServerPlayNetworking.send(context.getSource().getPlayer(), AmaziaNetworking.S2C.SETUP_PATHINGOVERLAY, packet);
			}
		}
		catch (final Exception e){
			Amazia.LOGGER.error(e.getMessage());
			return -1;
		}
		return 1;
	}

	private static int showAccesPoints(final CommandContext<ServerCommandSource> context) {
		try {
			final BlockPos pos = BlockPosArgumentType.getBlockPos(context, "villagePos");

			if (context.getSource().getWorld().getBlockEntity(pos) instanceof final VillageCoreBlockEntity core) {
				final Village village = core.getVillage();
				final ServerWorld world = context.getSource().getWorld();
				AmaziaClientVillagePathingCommands.renderAccessPoints(village.getBlasting().getAccessPoints(), world);
				AmaziaClientVillagePathingCommands.renderAccessPoints(village.getSmelting().getAccessPoints(), world);
				AmaziaClientVillagePathingCommands.renderAccessPoints(village.getSmoking().getAccessPoints(), world);
				AmaziaClientVillagePathingCommands.renderAccessPoints(village.getLumber().getAccessPoints(), world);
				AmaziaClientVillagePathingCommands.renderAccessPoints(village.getEnchanting().getAccessPoints(), world);
				AmaziaClientVillagePathingCommands.renderAccessPoints(village.getDesk().getAccessPoints(), world);
				AmaziaClientVillagePathingCommands.renderAccessPoints(village.getChairs().getAccessPoints(), world);
				AmaziaClientVillagePathingCommands.renderAccessPoints(village.getStorage().getAccessPoints(), world);
				AmaziaClientVillagePathingCommands.renderAccessPoints(village.getBeds().getAccessPoints(), world);
			}
		}
		catch (final Exception e){
			Amazia.LOGGER.error(e.getMessage());
			return -1;
		}
		return 1;
	}

	private static <T extends PathingEventListener> void renderAccessPoints(final Collection<T> listeners, final ServerWorld world) {
		for (final PathingEventListener listener : listeners) {
			for (final BlockPos pos : listener.getPathingOptions()) {
				world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 16, 0, 0, 0, 0);
			}
		}
	}
}
