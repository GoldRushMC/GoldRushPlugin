package com.goldrushmc.bukkit.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.defaults.DefaultListener;

public class TradeLis extends DefaultListener{

    public TradeLis(JavaPlugin plugin) {
        super(plugin);
    }
    
    static HashMap<Player, Trade> tradeList = new HashMap<>();
    static HashMap<Player, Player> tradeTieIn = new HashMap<>();
    static List<Trade> trades =new ArrayList<>();
    static HashMap<String, Player> nameList = new HashMap<>();

    @EventHandler
    public void  OnPlayerRightClick(PlayerInteractEntityEvent e) {
        if(e.getRightClicked() instanceof Player){
        	e.getPlayer().sendMessage("You have initiated a trade with " + ((Player) e.getRightClicked()).getPlayer().getDisplayName());
        	Player player1 = e.getPlayer();
        	Player player2 = ((Player) e.getRightClicked()).getPlayer();
        	
        	player2.sendMessage(player1.getDisplayName() + " has initiated a trade with you");
            tradeList.put(player1, new Trade(e.getPlayer(), ((Player) e.getRightClicked()).getPlayer()));
            tradeTieIn.put(player2, player1);
            nameList.put(player1.getDisplayName(), player1);
            nameList.put(player2.getDisplayName(), player2);
        }
    }
    
    @EventHandler
    public void onInvetoryItemCLicked(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if (e.getCurrentItem().getType() == Material.INK_SACK) {
            	if(e.getCurrentItem().getDurability() == 2) { //green clicked
            		if(tradeList.containsKey((Player) e.getWhoClicked())) {
            			
            			tradeList.get((Player) e.getWhoClicked()).acceptTrade((Player) e.getWhoClicked());
            			
            		} else if(tradeList.containsKey(tradeTieIn.get((Player) e.getWhoClicked()))) {
            			
            			tradeList.get(tradeTieIn.get((Player) e.getWhoClicked())).acceptTrade((Player) e.getWhoClicked());
            			
            		}
            		e.setCancelled(true);
            		
            	} else if (e.getCurrentItem().getDurability() == 1) { //red clicked
            		if(tradeList.containsKey((Player) e.getWhoClicked())) { //find if player1
            			
            			tradeList.get((Player) e.getWhoClicked()).cancelTrade();
            			
            		} else if (tradeList.containsKey(tradeTieIn.get((Player) e.getWhoClicked()))){ //find if player2
            			
            			tradeList.get(tradeTieIn.get((Player) e.getWhoClicked())).cancelTrade();
            			
            		}
            		removePlayers((Player) e.getWhoClicked());
            		e.setCancelled(true);
            	}                
            }            
        }
    }
    
    @EventHandler
    public void onInvClick(InventoryClickEvent e){
    	if(e.getInventory().getName().startsWith("Trading")){
    		String[] split = e.getInventory().getName().split(" ");
    		if(split.length == 3){
    			nameList.get(split[2]).getOpenInventory().getTopInventory().setContents(e.getInventory().getContents());
    		}
    	}
    }
    
    @EventHandler
    public void onInvMove(InventoryMoveItemEvent e){
    	if(e.getDestination().getName().startsWith("Trading")){
    		String[] split = e.getDestination().getName().split(" ");
    		if(split.length == 3){ //the 3rd item in the array is the other players name
    			nameList.get(split[2]).getOpenInventory().getTopInventory().setContents(e.getDestination().getContents());
    		}
    	} else if(e.getSource().getName().startsWith("Trading")){
    		String[] split = e.getSource().getName().split(" ");
    		if(split.length == 3){
    			nameList.get(split[2]).getOpenInventory().getTopInventory().setContents(e.getSource().getContents());
    		}
    	}
    }
    
    public int getFreeTradeSpot(Inventory inv){
    	for(int i = 8; i <= 17; i++){
    		if(inv.getItem(i) == null){
    			return i;
    		}
    		
    		if(i == 8){
    			i = 13;
    		}
    	}
    	return 0;
    }
    
    public void removePlayers(Player player){
    	if(tradeList.containsKey(player)) {

			if(tradeTieIn.containsKey(tradeList.get(player).player2)) {
				tradeTieIn.remove(tradeList.get(player).player2); //remove second player from tie in list
				nameList.remove(tradeList.get(player).player2.getDisplayName());
			}
			
			tradeList.remove(player); //remove first player from trade list
			nameList.remove(player.getDisplayName());
			
		}
		
		if(tradeTieIn.containsKey(player)) {
			
			if(tradeList.containsKey((tradeTieIn.get(player)))){
				tradeList.remove(tradeTieIn.get(player)); //remove player1 from tradeList
				nameList.remove(tradeTieIn.get(player).getDisplayName());
			}
			
			tradeTieIn.remove((player)); //remove player2 from trade tie in list
			nameList.remove(player.getDisplayName());
		}
    }
}