package com.goldrushmc.bukkit.mines;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.goldrushmc.bukkit.defaults.DefaultListener;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class MineLis extends DefaultListener{

	public static Map<Player, Location> mineLoc = new HashMap<Player, Location>();
	public static Map<Player, Vector> mineMin = new HashMap<Player, Vector>();
	public static Map<Player, Vector> mineMax = new HashMap<Player, Vector>();
	public static WorldEditPlugin worldEditPlugin = null;
	
	public MineLis(JavaPlugin plugin) {
		super(plugin);
		// TODO Auto-generated constructor stub
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRightClick(PlayerInteractEvent e) {		
		Action eAction = e.getAction();
		Player p = e.getPlayer();
		
        
        
		if(eAction.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (p.getItemInHand().getType().equals(Material.CLAY_BALL)) {
				if (p.getItemInHand().getItemMeta().hasDisplayName()) {
					if (p.getItemInHand().getItemMeta().getDisplayName().equals("Mine Creator")) {
						mineLoc.put(p, e.getClickedBlock().getLocation());
						
						//initialise WorldEdit
						worldEditPlugin = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
				        if(worldEditPlugin == null){
				            p.sendMessage("Error: WorldEdit is null.");   
				        }
				        Selection sel = worldEditPlugin.getSelection(p);

				        //allow proceed if selection is of correct type
				        if (sel instanceof CuboidSelection) {
				            mineMin.put(p, new Vector(sel.getNativeMinimumPoint().getBlockX(),
				            		sel.getNativeMinimumPoint().getBlockY(),
				            		sel.getNativeMinimumPoint().getBlockZ()));
				            mineMax.put(p, new Vector(sel.getNativeMaximumPoint().getBlockX(),
				            		sel.getNativeMaximumPoint().getBlockY(),
				            		sel.getNativeMaximumPoint().getBlockZ()));         

				            //make sure the Entrance block is inside the WorlEdit selection
				            if(sel.contains(e.getClickedBlock().getLocation())) {
								p.sendMessage("Mine entrance donated at: " +
										mineLoc.get(p).getX() + ", " +
										mineLoc.get(p).getY() + ", " +
										mineLoc.get(p).getZ() + ".");
								p.sendMessage("Use '/mine create (name) to create mine");
							} else {
								p.sendMessage(ChatColor.RED + "Entrance is not within WorldEdit Selection"); 
							}
				            
				        }else{
				            p.sendMessage(ChatColor.DARK_RED + "Invalid Selection!");
				        }
						
					}
				}
			}
		}
	}
}
