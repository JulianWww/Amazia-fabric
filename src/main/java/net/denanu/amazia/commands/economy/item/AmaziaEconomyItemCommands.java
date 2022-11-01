package net.denanu.amazia.commands.economy.item;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.economy.itemEconomy.BaseItemEconomy;
import net.denanu.amazia.economy.itemEconomy.ItemEconomy;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.item.Item;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class AmaziaEconomyItemCommands {
	public static LiteralArgumentBuilder<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment env) {
		LiteralArgumentBuilder<ServerCommandSource> value = literal("item");
		
		value.then(literal("get")
			.then((ArgumentBuilder<ServerCommandSource, ?>)(argument("item", ItemStackArgumentType.itemStack(access)))
			.executes(AmaziaEconomyItemCommands::get)));
		
		value.then(literal("set")
			.then((ArgumentBuilder<ServerCommandSource, ?>)(argument("item", ItemStackArgumentType.itemStack(access)))
			.then((ArgumentBuilder<ServerCommandSource, ?>)(argument("value", FloatArgumentType.floatArg(0, ItemEconomy.MAX_VALUE)))
			.requires(AmaziaEconomyItemCommands::setRequirements)
			.executes(AmaziaEconomyItemCommands::set))));
		
		return value;
	}
	
	public static int get(CommandContext<ServerCommandSource> context) {
		Item itm = ItemStackArgumentType.getItemStackArgument(context, "item").getItem();
		
		BaseItemEconomy economy = Amazia.economy.getItem(itm.getTranslationKey());
		if (economy == null) {
			context.getSource().sendError(
					Text.translatable("messages.amazia.erros.non_existant_economy_item")
					.append(Text.translatable(itm.getTranslationKey()))); 
			return 1;
		}
		
		context.getSource().sendMessage(
				Text.translatable("messages.amazia.commands.item_value")
				.append(Float.toString(economy.getCurrentPrice())));
		
		return 1;
	}
	
	public static int set(CommandContext<ServerCommandSource> context) {
		Item itm = ItemStackArgumentType.getItemStackArgument(context, "item").getItem();
		
		BaseItemEconomy economy = Amazia.economy.getItem(itm.getTranslationKey());
		if (economy == null) {
			context.getSource().sendError(
					Text.translatable("messages.amazia.erros.non_existant_economy_item")
					.append(Text.translatable(itm.getTranslationKey()))); 
			return 1;
		}
		economy.setCurrentValue(
				FloatArgumentType.getFloat(context, "value")
			);
		
		context.getSource().sendMessage(
				Text.translatable("messages.amazia.commands.success").formatted(Formatting.GREEN)
			);
		
		return 1;
	}
	
	public static boolean setRequirements(ServerCommandSource source) {
		return source.hasPermissionLevel(2);
	}
}
