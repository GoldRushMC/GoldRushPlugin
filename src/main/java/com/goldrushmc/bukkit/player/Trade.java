package com.goldrushmc.bukkit.player;

import net.minecraft.server.v1_5_R3.EntityPlayer;
import net.minecraft.server.v1_5_R3.ItemStack;
import net.minecraft.server.v1_5_R3.TileEntityChest;
import net.minecraft.server.v1_5_R3.BlockPistonExtension;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Trade {

    Player player1, player2;
    EntityPlayer ePlayer1, ePlayer2;
    CraftPlayer cPlayer1, cPlayer2;
    TileEntityChest chest;


    public Trade(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        openTradeWindow();
    }

    public void openTradeWindow(){
        cPlayer1 = (CraftPlayer) player1;
        ePlayer1 = cPlayer1.getHandle();
        cPlayer2 = (CraftPlayer) player2;
        ePlayer2 = cPlayer2.getHandle();

       // BlockPistonExtension bpe = new BlockPistonExtension(1);
        //ItemStack iStack = new ItemStack(bpe);
        chest = new TileEntityChest();
        //chest.setItem(5, iStack);
        //chest.setItem(14, iStack);
        //chest.setItem(23, iStack);
        //chest.setItem(32, iStack);
        
        ePlayer1.a(chest);
        ePlayer2.a(chest);
    }
}