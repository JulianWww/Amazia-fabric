package net.denanu.amazia.commands.testSuit;

import java.util.ArrayList;
import java.util.HashMap;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.denanu.amazia.Amazia;
import net.denanu.amazia.block.entity.VillageCoreBlockEntity;
import net.denanu.amazia.compat.malilib.NamingLanguageOptions;
import net.denanu.amazia.mechanics.hunger.CraftingHungerManager;
import net.denanu.amazia.village.AmaziaData;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AmaziaTestCommands {
	public static LiteralArgumentBuilder<ServerCommandSource> register(final CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess access, final CommandManager.RegistrationEnvironment env) {
		final LiteralArgumentBuilder<ServerCommandSource> value = CommandManager.literal("tests").requires(source -> source.hasPermissionLevel(4));

		value.then(CommandManager.literal("enchantmentItems").		executes(AmaziaTestCommands::spawnEnchantmentItems));
		value.then(CommandManager.literal("blastingItems").			executes(AmaziaTestCommands::spawnBlastingItems));
		value.then(CommandManager.literal("blacksmithCraftables").	executes(AmaziaTestCommands::spawnBlacksmithCraftableItems));
		value.then(CommandManager.literal("guardUsable").			executes(AmaziaTestCommands::spawnGuardUsableItems));
		value.then(CommandManager.literal("smokableItems").			executes(AmaziaTestCommands::spawnSmeltingItems));
		value.then(CommandManager.literal("getMissingCraftables").	executes(AmaziaTestCommands::spawnCraftableItems));
		value.then(CommandManager.literal("testNameGen").			executes(AmaziaTestCommands::testNameGen));
		value.then(CommandManager.literal("testChatMessat").		executes(AmaziaTestCommands::chatMessage));
		value.then(CommandManager.literal("spawnLocation")
				.then(
						CommandManager.argument("pos", BlockPosArgumentType.blockPos())
						.executes(AmaziaTestCommands::testSpawnLocations)
						)
				);

		return value;
	}

	private static int spawnEnchantmentItems(final CommandContext<ServerCommandSource> context) {
		final Vec3d pos = context.getSource().getPosition();
		for (final Item item : AmaziaData.ENCHANTABLES) {
			AmaziaTestCommands.spawn(context, item, pos);
		}
		for (int i=0;i<9;i++) {
			AmaziaTestCommands.spawn(context, Items.LAPIS_LAZULI, pos);
		}
		return 1;
	}

	private static int spawnBlastingItems(final CommandContext<ServerCommandSource> context) {
		final Vec3d pos = context.getSource().getPosition();
		for (final Item item : AmaziaData.BLASTABLES) {
			AmaziaTestCommands.spawn(context, item, pos);
		}
		AmaziaTestCommands.spawn(context, Items.COAL, pos);
		return 1;
	}
	private static int spawnSmeltingItems(final CommandContext<ServerCommandSource> context) {
		final Vec3d pos = context.getSource().getPosition();
		for (final Item item : AmaziaData.SMOKABLES) {
			AmaziaTestCommands.spawn(context, item, pos);
		}
		AmaziaTestCommands.spawn(context, Items.COAL, pos);
		return 1;
	}

	private static int spawnBlacksmithCraftableItems(final CommandContext<ServerCommandSource> context) {
		final Vec3d pos = context.getSource().getPosition();
		for (final Item item : AmaziaData.buildBlacksmithCraftables()) {
			AmaziaTestCommands.spawn(context, item, pos);
		}
		return 1;
	}

	private static int chatMessage(final CommandContext<ServerCommandSource> context) {
		final Style style = Style.EMPTY.withFormatting(Formatting.DARK_PURPLE);
		context.getSource().getPlayer().sendMessage(
				Text.translatable("No path found %0", "hello").setStyle(style)
				);
		return 1;
	}

	private static int spawnGuardUsableItems(final CommandContext<ServerCommandSource> context) {
		final Vec3d pos = context.getSource().getPosition();
		for (final Item item : AmaziaData.MELEE_WEAPONS) {
			AmaziaTestCommands.spawn(context, item, pos);
		}
		return 1;
	}

	private static int spawnCraftableItems(final CommandContext<ServerCommandSource> context) {
		final Vec3d pos = context.getSource().getPosition();
		AmaziaTestCommands.spawnItemsFromCraftingMap(context, pos, Amazia.FARMER_CRAFTS);
		AmaziaTestCommands.spawnItemsFromCraftingMap(context, pos, Amazia.MINER_CRAFTS);
		AmaziaTestCommands.spawnItemsFromCraftingMap(context, pos, Amazia.LUMBERJACK_CRAFTS);
		AmaziaTestCommands.spawnItemsFromCraftingMap(context, pos, Amazia.BLACKSMITH_CRAFTABLES);
		AmaziaTestCommands.spawnItemsFromCraftingMap(context, pos, Amazia.CHEF_CRAFTABLES);
		AmaziaTestCommands.spawnItemsFromCraftingMap(context, pos, Amazia.GUARD_CRAFTABLES);

		return 1;
	}

	private static int testNameGen(final CommandContext<ServerCommandSource> context) {
		for (int i = 0; i<16; i++) {
			context.getSource().sendMessage(Text.literal(NamingLanguageOptions.generateName(null)));
		}
		return 1;
	}

	private static void spawnItemsFromCraftingMap(final CommandContext<ServerCommandSource> context, final Vec3d pos, final HashMap<Item,ArrayList<CraftingRecipe>> map) {
		for (final Item itm : map.keySet()) {
			if (!CraftingHungerManager.ITEM_HUNGER_MAP.containsKey(itm)) {
				AmaziaTestCommands.spawn(context, itm, pos);
			}
		}
	}

	private static void spawn(final CommandContext<ServerCommandSource> context, final Item item, final Vec3d pos) {
		final ItemStack stack = new ItemStack(item, 1);
		final ItemEntity itemEntity = new ItemEntity(context.getSource().getWorld(), pos.x, pos.y, pos.z, stack);
		itemEntity.setPickupDelay(10);
		itemEntity.setVelocity(Vec3d.ZERO);
		context.getSource().getWorld().spawnEntity(itemEntity);
	}

	private static int testSpawnLocations(final CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		final BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
		if (context.getSource().getWorld().getBlockEntity(pos) instanceof final VillageCoreBlockEntity core) {
			for (int i = 0; i < 1; i++) {
				final BlockPos spawnPos = core.getVillage().getPathingGraph().getRandomVillageEnterNode();

				//final var stand = new ArmorStandEntity(context.getSource().getWorld(), spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
				//context.getSource().getWorld().spawnEntity(stand);
				context.getSource().getWorld().setBlockState(spawnPos, Blocks.RED_CARPET.getDefaultState());

				context.getSource().getPlayer().teleport(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
			}
		}
		context.getSource().sendFeedback(Text.literal("done"), false);
		return 1;
	}
}
