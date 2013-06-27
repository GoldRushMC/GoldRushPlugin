package com.goldrushmc.bukkit.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class SettingsManager {

	private SettingsManager() {
	}

	static SettingsManager instance = new SettingsManager();

	Plugin p;

	FileConfiguration config;
	File cfile;

	public static SettingsManager getInstance() {

		return instance;
	}
	
	public FileConfiguration getFileConfig() {
		return config;
	}

	public void setup(Plugin p) {

		if (!p.getDataFolder().exists()) {

			p.getDataFolder().mkdir();
		}

		cfile = new File(p.getDataFolder(), "config.yml");

		if (!cfile.exists()) {

			config = p.getConfig();
			config.options().copyDefaults(true);

			//Configure tunnel collapse defaults
			config.addDefault("collapse.depth", 38);
			config.addDefault("collapse.chance", 5);
			config.addDefault("collapse.radius", 25);
			config.addDefault("collapse.delay", 30L);
			
			//Configure default world
			config.addDefault("world", "world");
			
			//Configure default trainstation types schedule times
			config.addDefault("station.times.public", 2);
			config.addDefault("station.times.transport", 5);
			config.addDefault("station.times.hub", 8);

            //Configure default bank settings
            config.addDefault("bank.teller.diameter", 3);

			saveConfig();

		}
	}

	public void saveConfig() {
		try {

			this.config.save(cfile);

		} catch (IOException e) {

			Bukkit.getServer()
					.getLogger()
					.severe(ChatColor.RED
							+ "The config.yml file could not be saved");
		}
	}

	public void reloadConfig() {

		config = YamlConfiguration.loadConfiguration(cfile);
	}

}