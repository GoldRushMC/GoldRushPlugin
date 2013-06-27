package com.goldrushmc.bukkit.player;

import net.minecraft.server.v1_5_R3.TileEntityChest;
import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftInventory;
import org.bukkit.entity.Player;

public class Trade {

    Player player1, player2;



    public Trade(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        openTradeWindow();
    }

    public void openTradeWindow(){
        
        TileEntityChest chest = new TileEntityChest ();
        CraftInventory inventory = new CraftInventory(chest);
        player1.openInventory(player2.getInventory());
        player2.openInventory(player1.getInventory());
        //woah
    }
}