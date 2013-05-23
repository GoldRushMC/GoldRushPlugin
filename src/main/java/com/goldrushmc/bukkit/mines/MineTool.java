package com.goldrushmc.bukkit.mines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.defaults.CommandDefault;

public class MineTool  extends CommandDefault {
	
	public static Map<Player, List<Integer>> mineSize = new HashMap<Player, List<Integer>>();
	public static Map<Player, List<blockBackup>> backList = new HashMap<Player, List<blockBackup>>();

	public MineTool(JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("mine")){ // command label
			if (p instanceof Player){ // instance of player - checks if player
				if (args.length > 0){
					if (args[0].equalsIgnoreCase("create")){ 
						if (args.length == 4){
							try{
								List<Integer> list = new ArrayList<Integer>();
								list.add(Integer.valueOf(args[1])); 
								list.add(Integer.valueOf(args[2]));
								list.add(Integer.valueOf(args[3]));
								mineSize.put(p, list);
								p.sendMessage("Created mine outline of size: "+ args[1] + ", " + args[2] + ", " + args[3] + ".");
								p.sendMessage("Type /mine confirm to create the mine, /mine cancel to start again.");
								makeMarkers(p.getWorld(), p, Material.WOOL);
								return true;
							} catch(Exception ex) {
								p.sendMessage(ChatColor.RED + "Invalid Paramters!");
								return false;
							}
						} else {
							return false;
						}
					} else if (args[0].equalsIgnoreCase("tool")){ 
						ItemStack panningTool= new ItemStack(Material.CLAY_BALL);
						List<String> lore = new ArrayList<String>();
						lore.add("Right click to denote start of mine");
						ItemMeta meta = panningTool.getItemMeta();
						meta.setLore(lore);
						meta.setDisplayName("Mine Creator");
						panningTool.setItemMeta(meta);
						p.getInventory().addItem(panningTool);
						return true;
					} else if (args[0].equalsIgnoreCase("comfirm")){ 
						
						return true;
					} else if (args[0].equalsIgnoreCase("cancel")){ 
						undoMarkers(p.getWorld(), p);
						return true;
					} else {
						p.sendMessage("Not a valid command!");
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
		return false;
		}
	}
	
	private void makeMarkers(World w, Player p, Material m) {
		int maxx = MineLis.mineLoc.get(p).getBlockX() + (mineSize.get(p).get(0) / 2);
		int minx = MineLis.mineLoc.get(p).getBlockX() - (mineSize.get(p).get(0) / 2);
		int maxz = MineLis.mineLoc.get(p).getBlockZ() + (mineSize.get(p).get(1) / 2);
		int minz = MineLis.mineLoc.get(p).getBlockZ() - (mineSize.get(p).get(1) / 2);
		int maxy = MineLis.mineLoc.get(p).getBlockY() + (mineSize.get(p).get(2) / 2);
		int miny = MineLis.mineLoc.get(p).getBlockY() - (mineSize.get(p).get(2) / 2);
		
		List<blockBackup> temp = new ArrayList<blockBackup>();
		
		if(backList.containsKey(p)){
			if(backList.get(p).size() > 0) {
				undoMarkers(w, p);
			}
		}
		
		backList.remove(p);
		for(int x = minx; x <= maxx; x++) {
			for(int z = minz; z <= maxz; z++) {
				for(int y = miny; y <= maxy; y++) {
					if(x == minx && y == miny){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == maxx && y == maxy){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == maxx && y == miny){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == minx && y == maxy){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(z == minz && y == miny){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(z == maxz && y == maxy){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(z == maxz && y == miny){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(z == minz && y == maxy){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == minx && z == minz){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == maxx && z == maxz){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == maxx && z == minz){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == minx && z == maxz){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					}
				}
			}
		}
		
		backList.put(p, temp);
	}
	
	private void undoMarkers(World w, Player p) {
		for(int i = 0; i < backList.get(p).size(); i++){
			w.getBlockAt(backList.get(p).get(i).location).setType(backList.get(p).get(i).material);
		}
	}
	
	class blockBackup {
		public Location location;
		public Material material;
	
		public blockBackup(Location loc, Material mat) {
			location = loc;
			material = mat;
		}
	}
}
