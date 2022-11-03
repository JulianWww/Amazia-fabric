package net.denanu.amazia.commands.data.scanner;

import static net.minecraft.server.command.CommandManager.literal;

import java.util.Optional;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.utils.nbt.NbtStream;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.NbtDataSource;
import net.minecraft.text.Text;

public class AmaziaDataScannerCommand {
	public static LiteralArgumentBuilder<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment env) {		
		LiteralArgumentBuilder<ServerCommandSource> value = literal("data");
		
		
		value.then(literal("get")
				.executes(AmaziaDataScannerCommand::get));
		
		
		return value;
	}
	
	public static int get(CommandContext<ServerCommandSource> context) {
		NbtCompound nbt = Amazia.chunkScanner.writeNbt(new NbtCompound());
		
		context.getSource().sendFeedback(
				Text.translatable("commands.data.entity.query", "Amazia Chunk Scanner", NbtHelper.toPrettyPrintedText(nbt)),
				false
			);
		
		return 1;
	}
}
