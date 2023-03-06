package net.denanu.amazia.GUI;

import net.denanu.amazia.Amazia;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AmaziaScreens {
	public static final Identifier MERCHANT = new Identifier(Amazia.MOD_ID, "merchant_screen");
	public static final Identifier VILLAGER = new Identifier(Amazia.MOD_ID, "villager_screen");

	public static final ScreenHandlerType<AmaziaVillagerUIScreenHandler> VILLAGER_SCREEN_HANDLER = AmaziaScreens.registerAdvanced(AmaziaScreens.VILLAGER, AmaziaVillagerUIScreenHandler::new);
	public static final ScreenHandlerType<TradingScreenHandler> TRADING_SCREEN_HANDLER = AmaziaScreens.register(AmaziaScreens.MERCHANT, TradingScreenHandler::new);

	private static <T extends ScreenHandler> ScreenHandlerType<T> register(final Identifier id, final ScreenHandlerType.Factory<T> factory) {
		return Registry.register(Registry.SCREEN_HANDLER, id, new ScreenHandlerType<>(factory));
	}

	private static <T extends ScreenHandler> ScreenHandlerType<T> registerAdvanced(final Identifier id, final ExtendedScreenHandlerType.ExtendedFactory<T> factory) {
		return Registry.register(Registry.SCREEN_HANDLER, id, new ExtendedScreenHandlerType<>(factory));
	}

	public static void setup() {}
}
