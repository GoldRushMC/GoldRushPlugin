package com.goldrushmc.bukkit.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.commands.BuildModeCommand.BuildMode;
import com.goldrushmc.bukkit.defaults.CommandDefault;
import com.goldrushmc.bukkit.defaults.GoldRushPerms;

public class StationWand extends CommandDefault {

	public StationWand(JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(isPlayer(sender)) { sender.sendMessage("You cannot use this command from the console."); return true; }
		if(!sender.hasPermission(GoldRushPerms.ADD)) { deny(sender); return true; }
		
		if(!BuildModeCommand.buildMode.containsKey((Player) sender)) {
			BuildModeCommand.buildMode.put((Player) sender, BuildMode.STATION);
			sender.sendMessage("Your build mode has been set to station by default. \n"
					+ "Do /buildmode [mode] to change it");
		}
		
		//Get player
		Player p = (Player) sender;
		
		//Create the tool
		ItemStack blazeRod = new ItemStack(Material.BLAZE_ROD);
		List<String> lore = new ArrayList<String>();
		lore.add("Creatable");
		ItemMeta meta = blazeRod.getItemMeta();
		meta.setLore(lore); 
		meta.setDisplayName("Building Tool");
		blazeRod.setItemMeta(meta);
		p.setItemInHand(blazeRod);
		return true;
	}
}
