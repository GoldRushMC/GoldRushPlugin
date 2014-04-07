package com.goldrushmc.bukkit.player;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Trade {

    Player player1, player2;
    Boolean player1Accepted =  false, player2Accepted =  false;


    public Trade(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        openTradeWindow();
    }

    public void openTradeWindow(){
    	
    	Inventory inventory1 = Bukkit.createInventory(player1, 27, "Trading with "+ player2.getDisplayName());    	
    	Inventory inventory2 = Bukkit.createInventory(player2, 27, "Trading with "+ player1.getDisplayName());    	
      
        ItemStack blockItem = new ItemStack(Material.PISTON_EXTENSION);
        Short green = 2, red = 1;
        ItemStack accept = new ItemStack(Material.INK_SACK, 1, green);
        ItemStack decline = new ItemStack(Material.INK_SACK, 1, red);
        
        // Column
        inventory1.setItem(4, blockItem); inventory2.setItem(4, blockItem);
        inventory1.setItem(13, blockItem); inventory2.setItem(13, blockItem);
        inventory1.setItem(18, blockItem); inventory2.setItem(18, blockItem);
        
        // row
        inventory1.setItem(19, blockItem); inventory2.setItem(19, blockItem);
        inventory1.setItem(20, blockItem); inventory2.setItem(20, blockItem);
        inventory1.setItem(21, blockItem); inventory2.setItem(21, blockItem);
        inventory1.setItem(22, blockItem); inventory2.setItem(22, blockItem);
        inventory1.setItem(23, blockItem); inventory2.setItem(23, blockItem);
        inventory1.setItem(24, blockItem); inventory2.setItem(24, blockItem);
        
        //buttons
        inventory1.setItem(25, accept); inventory2.setItem(25, accept);
        inventory1.setItem(26, decline); inventory2.setItem(26, decline);
        
        player1.openInventory(inventory1);
        player2.openInventory(inventory2);
    }
    
    public void acceptTrade(Player player){
    	if(player == player1) {
    		player1Accepted = true;
    		player2.sendMessage(player.getDisplayName() + " has accepted the trade!");
    		player1.sendMessage("You have excepted the trade with " + player2.getDisplayName());
    	} else if(player == player2) {
    		player2Accepted = true;
    		player1.sendMessage(player.getDisplayName() + " has accepted the trade!");
    		player2.sendMessage("You have excepted the trade with " + player1.getDisplayName());
    	}
    	
    	if(player1Accepted == true && player2Accepted == true) {
    		
    		if(player2.getOpenInventory().getTopInventory().getItem(5) != null)
    		player1.getInventory().addItem(player2.getOpenInventory().getTopInventory().getItem(5));
    		if(player2.getOpenInventory().getTopInventory().getItem(6) != null)
    		player1.getInventory().addItem(player2.getOpenInventory().getTopInventory().getItem(6));
    		if(player2.getOpenInventory().getTopInventory().getItem(7) != null)
    		player1.getInventory().addItem(player2.getOpenInventory().getTopInventory().getItem(7));
    		if(player2.getOpenInventory().getTopInventory().getItem(8) != null)
    		player1.getInventory().addItem(player2.getOpenInventory().getTopInventory().getItem(8));
    		if(player2.getOpenInventory().getTopInventory().getItem(14) != null)
    		player1.getInventory().addItem(player2.getOpenInventory().getTopInventory().getItem(14));
    		if(player2.getOpenInventory().getTopInventory().getItem(15) != null)
    		player1.getInventory().addItem(player2.getOpenInventory().getTopInventory().getItem(15));
    		if(player2.getOpenInventory().getTopInventory().getItem(16) != null)
    		player1.getInventory().addItem(player2.getOpenInventory().getTopInventory().getItem(16));
    		if(player2.getOpenInventory().getTopInventory().getItem(17) != null)
    		player1.getInventory().addItem(player2.getOpenInventory().getTopInventory().getItem(17));
    		
    		if(player1.getOpenInventory().getTopInventory().getItem(0) != null)
    		player2.getInventory().addItem(player1.getOpenInventory().getTopInventory().getItem(0));
    		if(player1.getOpenInventory().getTopInventory().getItem(1) != null)
    		player2.getInventory().addItem(player1.getOpenInventory().getTopInventory().getItem(1));
    		if(player1.getOpenInventory().getTopInventory().getItem(2) != null)
    		player2.getInventory().addItem(player1.getOpenInventory().getTopInventory().getItem(2));
    		if(player1.getOpenInventory().getTopInventory().getItem(3) != null)
    		player2.getInventory().addItem(player1.getOpenInventory().getTopInventory().getItem(3));
    		if(player1.getOpenInventory().getTopInventory().getItem(9) != null)
    		player2.getInventory().addItem(player1.getOpenInventory().getTopInventory().getItem(9));
    		if(player1.getOpenInventory().getTopInventory().getItem(10) != null)
    		player2.getInventory().addItem(player1.getOpenInventory().getTopInventory().getItem(10));
    		if(player1.getOpenInventory().getTopInventory().getItem(11) != null)
    		player2.getInventory().addItem(player1.getOpenInventory().getTopInventory().getItem(11));
    		if(player1.getOpenInventory().getTopInventory().getItem(12) != null)
    		player2.getInventory().addItem(player1.getOpenInventory().getTopInventory().getItem(12));
    		
    		player1.getOpenInventory().getTopInventory().clear();
    		player2.getOpenInventory().getTopInventory().clear();
    		player1.closeInventory();
        	player2.closeInventory();
    	}
    }
    
    public void cancelTrade(){
    	player1.closeInventory();
    	player1.sendMessage("Trade canceled");
    	player2.closeInventory();
    	player2.sendMessage("Trade canceled");
    }
}