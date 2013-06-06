package com.goldrushmc.bukkit.weapons;

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

public class GunTool extends CommandDefault {

	public GunTool(JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("gun")){ // command label
			if (p instanceof Player){ // instance of player - checks if player
				if (args[0].equalsIgnoreCase("revolver")){ 
					ItemStack gunTool= new ItemStack(Material.GOLD_AXE);
					List<String> lore = new ArrayList<String>();
					lore.add("5 Rounds Per Cylinder");
					lore.add("4 Damage");					
					ItemMeta meta = gunTool.getItemMeta();
					meta.setLore(lore);
					meta.setDisplayName("Colt 1851");
					gunTool.setItemMeta(meta);
					gunTool.setDurability((short) 1);
					p.getInventory().addItem(gunTool);
					return true;
				} else if (args[0].equalsIgnoreCase("shotgun")){ 
					ItemStack gunTool= new ItemStack(Material.GOLD_HOE);
					List<String> lore = new ArrayList<String>();
					lore.add("2 Rounds per clip");
					lore.add("Fires two shells");
					lore.add("6 x 1 Damage");
					ItemMeta meta = gunTool.getItemMeta();
					meta.setLore(lore);
					meta.setDisplayName("Coach Gun");
					gunTool.setItemMeta(meta);
					gunTool.setDurability((short) 1);
					p.getInventory().addItem(gunTool);
					return true;
				} else if (args[0].equalsIgnoreCase("rifle")){
					ItemStack gunTool= new ItemStack(Material.GOLD_SPADE);
					List<String> lore = new ArrayList<String>();
					lore.add("1 Round per clip");
					lore.add("8 Damage");
					ItemMeta meta = gunTool.getItemMeta();
					meta.setLore(lore);
					meta.setDisplayName("Sharps Rifle");
					gunTool.setItemMeta(meta);
					gunTool.setDurability((short) 1);
					p.getInventory().addItem(gunTool);
					return true;
				} else {
					return false;
				}
			} else
			return false;
		} else
			return false;		
	}
}
