package net.denanu.amazia.commands.economy.modifier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.amazia.commands.args.sugestions.EconomyModifierArgumentSugestions;
import net.denanu.amazia.commands.args.types.EconomyModifierArgumentType;
import net.denanu.amazia.economy.Economy;
import net.denanu.amazia.economy.itemEconomy.ItemEconomy;
import net.denanu.amazia.economy.offerModifiers.ModifierEconomy;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class AmaziaPriceModifierCommands {
	public static LiteralArgumentBuilder<ServerCommandSource> register(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess access, final CommandManager.RegistrationEnvironment env) {
		final LiteralArgumentBuilder<ServerCommandSource> value = CommandManager.literal("modifier");

		value.then(CommandManager.literal("get")
				.then(CommandManager.argument("mod", EconomyModifierArgumentType.modifier()).suggests(new EconomyModifierArgumentSugestions<ServerCommandSource>())
						.executes(AmaziaPriceModifierCommands::get)));

		value.then(CommandManager.literal("set")
				.then(CommandManager.argument("mod", EconomyModifierArgumentType.modifier()).suggests(new EconomyModifierArgumentSugestions<ServerCommandSource>())
						.then(CommandManager.argument("value", FloatArgumentType.floatArg(-ItemEconomy.MAX_VALUE, ItemEconomy.MAX_VALUE))
								.executes(AmaziaPriceModifierCommands::set))));

		return value;
	}

	public static int get(final CommandContext<ServerCommandSource> context) {
		final String key = EconomyModifierArgumentType.getEconomyModifierArgument(context, "mod");
		final ModifierEconomy economy = Economy.getModifierEconomy(key);

		if (economy == null) {
			context.getSource().sendError(
					Text.translatable("messages.amazia.erros.non_existant_economy_modifier")
					.append(Text.translatable(key)));
			return 1;
		}

		context.getSource().sendMessage(
				Text.translatable("messages.amazia.commands.modifier_value")
				.append(Float.toString(economy.getCurrentPrice())));
		return 1;
	}

	public static int set(final CommandContext<ServerCommandSource> context) {
		final String key = EconomyModifierArgumentType.getEconomyModifierArgument(context, "mod");
		final ModifierEconomy economy = Economy.getModifierEconomy(key);

		if (economy == null) {
			context.getSource().sendError(
					Text.translatable("messages.amazia.erros.non_existant_economy_modifier")
					.append(Text.translatable(key)));
			return 1;
		}

		economy.setCurrentPrice(
				FloatArgumentType.getFloat(context, "value")
				);

		context.getSource().sendMessage(
				Text.translatable("messages.amazia.commands.success").formatted(Formatting.GREEN)
				);

		return 1;
	}
}
