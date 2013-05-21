package com.goldrushmc.bukkit.tunnelcollapse;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

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

	public void setup(Plugin p) {

		if (!p.getDataFolder().exists()) {

			p.getDataFolder().mkdir();
		}

		cfile = new File(p.getDataFolder(), "config.yml");

		if (!cfile.exists()) {

			config = p.getConfig();
			config.options().copyDefaults(true);
			
			config.addDefault("depth", 38);
			config.addDefault("chance", 5);
			config.addDefault("radius", 25);
			config.addDefault("delay", 30L);
			
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