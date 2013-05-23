package com.goldrushmc.bukkit.mines;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.defaults.DefaultListener;

public class MineLis extends DefaultListener{

	public static Map<Player, Location> mineLoc = new HashMap<Player, Location>();
	
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
						p.sendMessage("Mine start donated at: " +
							mineLoc.get(p).getX() + ", " +
							mineLoc.get(p).getZ() + ", " +
							mineLoc.get(p).getY() + ".");
						p.sendMessage("Use '/mine create {Size - X} {Size - Z} {Size - Y}' to create a mine");
					}
				}
			}
		}
	}
}
