package com.goldrushmc.bukkit.panning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.goldrushmc.bukkit.defaults.DefaultListener;

public class PanningLis extends DefaultListener {

	public PanningLis(JavaPlugin plugin) {
		super(plugin);
	}

	int panDuration = 100;
	public static HashMap<Player, Boolean> panningHash = new HashMap<Player, Boolean>();
	public static HashMap<Player, Location> panningMoveHash = new HashMap<Player, Location>();
	public static HashMap<Player, Location> panningLocHash = new HashMap<Player, Location>();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRightClick(PlayerInteractEvent e) {
		Action eAction = e.getAction();
		Player p = e.getPlayer();
		if(!panningHash.containsKey(p)){
			panningHash.put(p, false);
		}
		boolean panning = panningHash.get(p).booleanValue();
		if(eAction.equals(Action.RIGHT_CLICK_BLOCK)) {
			if(!panning){
				if(e.getItem() != null) {
					if(e.getItem().getType().equals(Material.BOWL)){
						if(e.getItem().getItemMeta().hasDisplayName()) {
							if(e.getItem().getItemMeta().getDisplayName().equals("Panning Tool")) {
								if(e.getClickedBlock().getBiome().equals(Biome.RIVER)) {
//									boolean water = false;
									for(Block b : p.getLineOfSight(null, 4)) {
										if(b.getType().equals(Material.WATER) || b.getType().equals(Material.STATIONARY_WATER)) {
											if(panningLocHash.containsKey(p)){
												if(!e.getClickedBlock().getLocation().equals(panningLocHash.get(p))){	
													ItemMeta meta = e.getItem().getItemMeta();
													if(!meta.getLore().get(0).equals("Broken")) {
														panSound(p, panDuration);
														doPan(p, e);
													} else {
														p.sendMessage(ChatColor.DARK_RED + "You can't pan with a broken tool!");
													}
												} else {
													p.sendMessage(ChatColor.RED + "You won't find anything in that spot again!");
												}
											} else {
												doPan(p, e);
												panningLocHash.put(p, e.getClickedBlock().getLocation());
											}
										}
									}
								} else {
									p.sendMessage(ChatColor.RED + "You can only pan in Rivers!");
								}					
							}
						}
					}
				}
			}
		}
	}

	public void doPan(Player p, PlayerInteractEvent e) {
		panningMoveHash.put(p, p.getLocation());
		panningLocHash.put(p, e.getClickedBlock().getLocation());

		//uses logic
		List<String> lore = new ArrayList<String>();									
		ItemMeta meta = e.getItem().getItemMeta();
		int usesLeft = Integer.valueOf(meta.getLore().get(0).substring(0,meta.getLore().get(0).indexOf(' ')));
		lore.add(usesLeft - 1 + " uses left");
		meta.setLore(lore);
		e.getItem().setItemMeta(meta);

		if(usesLeft - 1 == 0){
			lore.clear();
			lore.add("Broken");
			meta.setLore(lore);
			e.getItem().setItemMeta(meta);
			p.sendMessage(ChatColor.RED + "Your tool will break after this pan!");
		}

		panningHash.put(p, true);						
		p.sendMessage(ChatColor.GOLD + "You begin to pan..");
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, panDuration, 5));
		Pan pan = new Pan(p);
		Bukkit.getServer().getScheduler().runTaskLater(plugin, pan, panDuration);	
	}

	public void panSound(Player p, int panDuration) {
		SoundPlay sp = new SoundPlay(p);
		for(int i = 0; i < panDuration / 20; i++){
			Bukkit.getServer().getScheduler().runTaskLater(plugin, sp, i * 20);
		}
	}

	class SoundPlay implements Runnable{
		Player p;
		SoundPlay(Player player){
			p = player;
		}

		@Override
		public void run() {
			p.playSound(p.getLocation(), Sound.STEP_GRAVEL, 10, 1);	
			p.playSound(p.getLocation(), Sound.SPLASH2, 10, 1);	
		}

	}

	class Pan implements Runnable{
		Player p;
		Pan(Player player) {
			p = player;
		}

		@Override
		public void run() {
			if(p.getItemInHand().getItemMeta().hasDisplayName()) {
				if(p.getItemInHand().getItemMeta().getDisplayName().equals("Panning Tool")) {
					if(p.getLocation().getBlock().equals(panningMoveHash.get(p).getBlock())) {
						Random randomGenerator = new Random();
						int randomInt = randomGenerator.nextInt(10);
						if(randomInt == 1) {
							p.getPlayer().getInventory().addItem(new ItemStack(Material.GOLD_NUGGET));
							p.getPlayer().sendMessage(ChatColor.GREEN+ "You found a gold nugget!");
						} else {
							p.getPlayer().sendMessage(ChatColor.DARK_RED+ "You sift through the river bed but find nothing..");
						}
						panningHash.put(p, false);						
						if(p.getFoodLevel() > 0)
							p.setFoodLevel(p.getFoodLevel() - 1);	
					} else {
						p.getPlayer().sendMessage(ChatColor.DARK_RED+ "You must stand still while panning!");
						panningHash.put(p, false);
					}
				} else {
					p.getPlayer().sendMessage(ChatColor.DARK_RED+ "You must continue to hold the panning tool while panning!");
					panningHash.put(p, false);
				}
			} else {
				p.getPlayer().sendMessage(ChatColor.DARK_RED+ "You must continue to hold the panning tool while panning!");
				panningHash.put(p, false);
			}		
		}
	}
}
