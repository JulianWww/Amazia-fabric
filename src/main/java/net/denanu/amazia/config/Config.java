package net.denanu.clientblockhighlighting.config;

import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigColor;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import net.denanu.clientblockhighlighting.Mod;

public class Config implements IConfigHandler {
	private static final String CONFIG_FILE_NAME = Mod.MOD_ID + ".json";

	public static class Generic {
		public static final ConfigBoolean SHOULD_RENDER = new ConfigBoolean("render", true, "Render Highlights", "Render Highlights");

		public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
				Generic.SHOULD_RENDER
				);
	}

	public static class Colors {
		public static final ConfigColor OUTLINE_COLOR 	= new ConfigColor("areaSelectionBoxSideColor",          "0xFFFFFFFF", "Color of the highlight outline");
		public static final ConfigColor FILL_COLOR 		= new ConfigColor("areaSelectionBoxFillColor",          "0x30FFFFFF", "Color of the highlight Surface");

		public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
				Colors.OUTLINE_COLOR,
				Colors.FILL_COLOR
				);
	}

	 public static void loadFromFile()
	    {
	        File configFile = new File(FileUtils.getConfigDirectory(), Config.CONFIG_FILE_NAME);

	        if (configFile.exists() && configFile.isFile() && configFile.canRead())
	        {
	            JsonElement element = JsonUtils.parseJsonFile(configFile);

	            if (element != null && element.isJsonObject())
	            {
	                JsonObject root = element.getAsJsonObject();

	                ConfigUtils.readConfigBase(root, "Generic", 	Generic.OPTIONS);
	                ConfigUtils.readConfigBase(root, "Color", 		Colors.OPTIONS);
	            }
	        }
	    }
	    public static void saveToFile()
	    {
	        File dir = FileUtils.getConfigDirectory();

	        if (dir.exists() && dir.isDirectory() || dir.mkdirs())
	        {
	            JsonObject root = new JsonObject();

	            ConfigUtils.writeConfigBase(root, "Generic", 	Generic.OPTIONS);
	            ConfigUtils.writeConfigBase(root, "Color", 		Colors.OPTIONS);

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