package net.denanu.amazia.commands.testSuit;

import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.amazia.village.AmaziaData;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.Vec3d;

public class AmaziaTestCommands {
	public static LiteralArgumentBuilder<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment env) {
		LiteralArgumentBuilder<ServerCommandSource> value = literal("tests");
		
		value.then(literal("enchantmentItems").executes(AmaziaTestCommands::spawnEnchantmentItems));
		
		return value;
	}
	
	private static int spawnEnchantmentItems(CommandContext<ServerCommandSource> context) {
		Vec3d pos = context.getSource().getPosition();
		for (Item item : AmaziaData.ENCHANTABLES) {
			ItemStack stack = new ItemStack(item, 1);
			ItemEntity itemEntity = new ItemEntity(context.getSource().getWorld(), pos.x, pos.y, pos.z, stack);
			itemEntity.setPickupDelay(100);
			itemEntity.setVelocity(Vec3d.ZERO);
			context.getSource().getWorld().spawnEntity(itemEntity);
		}
		return 1;
	}
}
