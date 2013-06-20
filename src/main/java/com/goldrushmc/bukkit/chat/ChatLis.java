package com.goldrushmc.bukkit.chat;

import com.goldrushmc.bukkit.defaults.DefaultListener;
import com.goldrushmc.bukkit.guns.GunTool;
import com.goldrushmc.bukkit.guns.GunTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lex
 * Date: 19/06/13
 * Time: 12:50
 * To change this template use File | Settings | File Templates.
 */
public class ChatLis  extends DefaultListener {

    public ChatLis(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void OnChatMessage(AsyncPlayerChatEvent e){
        String message = e.getMessage();
        if(!(message.substring(1).equals("/"))) {
            e.setCancelled(true);
            GunTools gt = new GunTools();
            List<Player> playerList = gt.getPlayersWithin(e.getPlayer(), 40);
            for(Player player : playerList){
                player.sendMessage("<" + e.getPlayer().getName() + "> " + message);
            }
            e.getPlayer().sendMessage("<" + e.getPlayer().getName() + "> " + message);
        }
    }
}
