package com.goldrushmc.bukkit.player;

import com.goldrushmc.bukkit.defaults.DefaultListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TradeLis extends DefaultListener{

    public TradeLis(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void  OnPlayerRightClick(PlayerInteractEntityEvent e) {
        if(e.getRightClicked() instanceof Player){
        	e.getPlayer().sendMessage("You have initiated a trade with " + ((Player) e.getRightClicked()).getPlayer().getDisplayName());
        	((Player) e.getRightClicked()).getPlayer().sendMessage(e.getPlayer().getDisplayName() + " has initiated a trade with you");
            Trade trade = new Trade(e.getPlayer(), ((Player) e.getRightClicked()).getPlayer());
        }
    }
}