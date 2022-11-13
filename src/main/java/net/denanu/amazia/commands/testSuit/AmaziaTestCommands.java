package net.denanu.amazia.commands.testSuit;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.denanu.amazia.village.AmaziaData;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.Vec3d;

public class AmaziaTestCommands {
	public static LiteralArgumentBuilder<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment env) {
		LiteralArgumentBuilder<ServerCommandSource> value = CommandManager.literal("tests");

		value.then(CommandManager.literal("enchantmentItems").executes(AmaziaTestCommands::spawnEnchantmentItems));
		value.then(CommandManager.literal("blastingItems").executes(AmaziaTestCommands::spawnBlastingItems));

		return value;
	}

	private static int spawnEnchantmentItems(CommandContext<ServerCommandSource> context) {
		Vec3d pos = context.getSource().getPosition();
		for (Item item : AmaziaData.ENCHANTABLES) {
			AmaziaTestCommands.spawn(context, item, pos);
		}
		for (int i=0;i<9;i++) {
			AmaziaTestCommands.spawn(context, Items.LAPIS_LAZULI, pos);
		}
		return 1;
	}

	private static int spawnBlastingItems(CommandContext<ServerCommandSource> context) {
		Vec3d pos = context.getSource().getPosition();
		for (Item item : AmaziaData.BLASTABLES) {
			AmaziaTestCommands.spawn(context, item, pos);
		}
		AmaziaTestCommands.spawn(context, Items.COAL_BLOCK, pos);
		return 1;
	}

	private static void spawn(CommandContext<ServerCommandSource> context, Item item, Vec3d pos) {
		ItemStack stack = new ItemStack(item, item.getMaxCount());
		ItemEntity itemEntity = new ItemEntity(context.getSource().getWorld(), pos.x, pos.y, pos.z, stack);
		itemEntity.setPickupDelay(10);
		itemEntity.setVelocity(Vec3d.ZERO);
		context.getSource().getWorld().spawnEntity(itemEntity);
	}
}
