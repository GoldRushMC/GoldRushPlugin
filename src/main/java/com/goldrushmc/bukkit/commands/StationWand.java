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

import com.goldrushmc.bukkit.defaults.CommandDefault;
import com.goldrushmc.bukkit.defaults.GoldRushPerms;

public class StationWand extends CommandDefault {

	public StationWand(JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!sender.hasPermission(GoldRushPerms.ADD)) { deny(sender); return true; }
		
		Player p = (Player) sender;
		ItemStack blazeRod = new ItemStack(Material.BLAZE_ROD);
		List<String> lore = new ArrayList<String>();
		lore.add("Creatable");
		ItemMeta meta = blazeRod.getItemMeta();
		meta.setLore(lore);
		meta.setDisplayName("Station Tool");
		blazeRod.setItemMeta(meta);
		p.setItemInHand(blazeRod);
		return true;
	}

}
