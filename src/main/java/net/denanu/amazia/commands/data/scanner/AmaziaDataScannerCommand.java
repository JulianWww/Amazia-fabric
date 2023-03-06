package net.denanu.amazia.commands.data.scanner;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.amazia.utils.scanners.ChunkScanner;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class AmaziaDataScannerCommand {
	public static LiteralArgumentBuilder<ServerCommandSource> register(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess access, final CommandManager.RegistrationEnvironment env) {
		final LiteralArgumentBuilder<ServerCommandSource> value = CommandManager.literal("data");


		value.then(CommandManager.literal("get")
				.executes(AmaziaDataScannerCommand::get));


		return value;
	}

	public static int get(final CommandContext<ServerCommandSource> context) {
		final NbtCompound nbt = ChunkScanner.writeNbt(new NbtCompound());

		context.getSource().sendFeedback(
				Text.translatable("commands.data.entity.query", "Amazia Chunk Scanner", NbtHelper.toPrettyPrintedText(nbt)),
				false
				);

		return 1;
	}
}
