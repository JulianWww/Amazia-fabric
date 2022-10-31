package net.denanu.amazia.commands.args.types;

import java.util.ArrayList;
import java.util.Collection;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.denanu.amazia.economy.Economy;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.text.Text;

public class EconomyModifierArgumentType implements ArgumentType<String> {
	public String data;
	
    public static EconomyModifierArgumentType modifier() {
        return new EconomyModifierArgumentType();
    }
    
    public static <S> String getEconomyModifierArgument(CommandContext<S> context, String name) {
        return context.getArgument(name, String.class);
    }

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		if (!reader.canRead()) {
			reader.skip();
		}
		int begining = reader.getCursor();
		
		while (reader.canRead() && reader.peek() != ' ') {
			reader.skip();
		}
		try {
			return reader.getString().substring(begining, reader.getCursor());
		}
		catch (Exception ex) {
			throw new SimpleCommandExceptionType(Text.literal(ex.getMessage())).createWithContext(reader);
		}
	}
	
    @Override
    public Collection<String> getExamples() {
    	return new ArrayList<String>();
    }
}
