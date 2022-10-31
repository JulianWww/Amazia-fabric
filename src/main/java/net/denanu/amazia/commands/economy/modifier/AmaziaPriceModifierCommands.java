package net.denanu.amazia.commands.economy.modifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.amazia.commands.args.sugestions.EconomyModifierArgumentSugestions;
import net.denanu.amazia.commands.args.types.EconomyModifierArgumentType;
import net.denanu.amazia.commands.economy.item.AmaziaEconomyItemCommands;
import net.denanu.amazia.economy.Economy;
import net.denanu.amazia.economy.offerModifiers.ModifierEconomy;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class AmaziaPriceModifierCommands {
	public static LiteralArgumentBuilder<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment env) {
		LiteralArgumentBuilder<ServerCommandSource> value = literal("modifier");
		
		value.then(literal("get")
			.then((ArgumentBuilder<ServerCommandSource, ?>)(argument("mod", EconomyModifierArgumentType.modifier()).suggests(new EconomyModifierArgumentSugestions<ServerCommandSource>()))
			.executes(AmaziaPriceModifierCommands::get)));
		
		return value;
	}
	
	public static int get(CommandContext<ServerCommandSource> context) {
		String key = EconomyModifierArgumentType.getEconomyModifierArgument(context, "mod");
		ModifierEconomy economy = Economy.getModifierEconomy(key);
		
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
}
