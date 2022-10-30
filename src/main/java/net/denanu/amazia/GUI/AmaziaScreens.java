package net.denanu.amazia.GUI;

import net.denanu.amazia.Amazia;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

@SuppressWarnings("deprecation")
public class AmaziaScreens {
    public static final Identifier MERCHANT = new Identifier(Amazia.MOD_ID, "merchant_screen");
	
	public static final ScreenHandlerType<TradingScreenHandler> TRADING_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(MERCHANT, TradingScreenHandler::new);
}
