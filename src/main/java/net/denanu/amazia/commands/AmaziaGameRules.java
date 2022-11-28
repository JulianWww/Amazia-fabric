package net.denanu.amazia.commands;

import net.denanu.amazia.Amazia;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Category;

public class AmaziaGameRules {
	public static final GameRules.Key<GameRules.BooleanRule> VILLAGE_ENEMIES_GLOW = GameRuleRegistry.register(Identifier.of(Amazia.MOD_ID, "village-enemies-glow").toString(), Category.MISC, GameRuleFactory.createBooleanRule(true));
	public static void setup() {
	}
}
