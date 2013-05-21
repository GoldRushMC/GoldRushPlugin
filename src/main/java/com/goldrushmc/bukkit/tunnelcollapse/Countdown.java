package com.goldrushmc.bukkit.tunnelcollapse;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {

	public Player player = null;
	public List<Player> ps = new ArrayList<Player>();
	
	public Plugin plugin;
	
	Countdown(Plugin instance) {
		
		this.plugin = instance;
		
	}

	public void setPlayer(Player p) {
		
		this.player = p;
		
	}

	public void setList(List<Player> list) {
		
		this.ps = list;
		
	}
	
	public List<Player> getList() {
		
		return this.ps;
	}

	public void run() {
		
		this.ps.remove(this.player);
	}


}
