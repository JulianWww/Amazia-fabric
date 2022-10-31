package net.denanu.amazia.commands.args.sugestions;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.denanu.amazia.economy.Economy;

public class EconomyModifierArgumentSugestions<S> implements SuggestionProvider<S>{

	@Override
	public CompletableFuture<Suggestions> getSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
			throws CommandSyntaxException {
		for (String str : Economy.getModifierList()) {
			builder.suggest(str);
		}
		return builder.buildFuture();
	}

}
