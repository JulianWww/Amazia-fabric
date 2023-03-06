package net.denanu.amazia.commands.economy.item;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.amazia.economy.Economy;
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
	public static LiteralArgumentBuilder<ServerCommandSource> register(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess access, final CommandManager.RegistrationEnvironment env) {
		final LiteralArgumentBuilder<ServerCommandSource> value = CommandManager.literal("item");

		value.then(CommandManager.literal("get")
				.then(CommandManager.argument("item", ItemStackArgumentType.itemStack(access))
						.executes(AmaziaEconomyItemCommands::get)));

		value.then(CommandManager.literal("set")
				.then(CommandManager.argument("item", ItemStackArgumentType.itemStack(access))
						.then(CommandManager.argument("value", FloatArgumentType.floatArg(0, ItemEconomy.MAX_VALUE))
								.requires(AmaziaEconomyItemCommands::setRequirements)
								.executes(AmaziaEconomyItemCommands::set))));

		return value;
	}

	public static int get(final CommandContext<ServerCommandSource> context) {
		final Item itm = ItemStackArgumentType.getItemStackArgument(context, "item").getItem();

		final BaseItemEconomy economy = Economy.getItem(itm.getTranslationKey());
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

	public static int set(final CommandContext<ServerCommandSource> context) {
		final Item itm = ItemStackArgumentType.getItemStackArgument(context, "item").getItem();

		final BaseItemEconomy economy = Economy.getItem(itm.getTranslationKey());
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

	public static boolean setRequirements(final ServerCommandSource source) {
		return source.hasPermissionLevel(2);
	}
}
