package net.denanu.amazia.config;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigColor;
import fi.dy.masa.malilib.config.options.ConfigOptionList;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import net.denanu.amazia.Amazia;
import net.denanu.amazia.compat.malilib.NameFakerOption;
import net.denanu.amazia.compat.malilib.NamingLanguageOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Config implements IConfigHandler {
	private static final String CONFIG_FILE_NAME = Amazia.MOD_ID + ".json";

	public static class Generic {
		public static final ArrayList<IConfigBase> OPTIONS = new ArrayList<>();

		public static ConfigColor PROBLEM_COLOR = Generic.register(new ConfigColor("problem_color", "#00820000", "Color used in chat for Problems"));
		public static ConfigBoolean SHOW_NAMES_IN_CHAT = Generic.register(new ConfigBoolean("show_names", true, "If chat messages should contain entity names", "Chat Names"));

		protected static <T extends IConfigBase> T register(final T config) {
			Generic.OPTIONS.add(config);
			return config;
		}
	}

	public static class Admin {
		public static final ArrayList<IConfigBase> OPTIONS = new ArrayList<>();

		public static final ConfigOptionList NAMELANGUAGE = Admin.register(new ConfigOptionList("Name generator Language", new NameFakerOption(NamingLanguageOptions.ENGLISH), "The language the name generator will use to generate names for the villagers"));

		protected static <T extends IConfigBase> T register(final T config) {
			Admin.OPTIONS.add(config);
			return config;
		}
	}

	public static void loadFromFile()
	{
		final File configFile = new File(FileUtils.getConfigDirectory(), Config.CONFIG_FILE_NAME);

		if (configFile.exists() && configFile.isFile() && configFile.canRead())
		{
			final JsonElement element = JsonUtils.parseJsonFile(configFile);

			if (element != null && element.isJsonObject())
			{
				final JsonObject root = element.getAsJsonObject();

				ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
			}
		}
	}

	public static void saveToFile()
	{
		final File dir = FileUtils.getConfigDirectory();

		if (dir.exists() && dir.isDirectory() || dir.mkdirs())
		{
			final JsonObject root = new JsonObject();

			ConfigUtils.writeConfigBase(root, "Generic", Generic.OPTIONS);

			JsonUtils.writeJsonToFile(root, new File(dir, Config.CONFIG_FILE_NAME));
		}
	}

	@Override
	public void load()
	{
		Config.loadFromFile();
	}

	@Override
	public void save()
	{
		Config.saveToFile();
	}
}
