package com.goldrushmc.bukkit.commands;

import com.goldrushmc.bukkit.defaults.CommandDefault;
import com.goldrushmc.bukkit.defaults.GoldRushPerms;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildModeCommand extends CommandDefault {

    public static Map<Player, BuildMode> buildMode = new HashMap<>();

    public BuildModeCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        //You need permission to do this.
        if(!p.hasPermission(GoldRushPerms.CREATE)) { deny(sender); return true;}

        //If the player doesn't have a rod, we don't want to go further!
        ItemStack rod = getRod(p);
        if(rod == null) { sender.sendMessage(ChatColor.YELLOW + "You need a GR Build Tool to use this command."); return true; }

        if(args[0].equalsIgnoreCase("Town")) {
            buildMode.put(p, BuildMode.TOWN);
            updateMeta(rod, BuildMode.TOWN);
        }
        else if(args[0].equalsIgnoreCase("Bank")) {
            buildMode.put(p, BuildMode.BANK);
            updateMeta(rod, BuildMode.BANK);
        }
        else if(args[0].equalsIgnoreCase("Station")) {
            buildMode.put(p, BuildMode.STATION);
            updateMeta(rod, BuildMode.STATION);
        }
        else if(args[0].equalsIgnoreCase("Stop") || args[0].equalsIgnoreCase("End")) {
            buildMode.put(p, BuildMode.OFF);
            updateMeta(rod, BuildMode.OFF);
        }
        return true;
    }

    private ItemStack getRod(Player p) {
        for(ItemStack is : p.getInventory()) {
            if(is.getItemMeta().getDisplayName().equals("GR Build Tool")) {
                return is;
            }
        }
        return null;
    }

    private void updateMeta(ItemStack rod, BuildMode bm) {
        ItemMeta im = rod.getItemMeta();
        List<String> lore = im.getLore();
        lore.clear();
        switch (bm) {
            case TOWN:
                lore.add("Town Mode");
                break;
            case BANK:
                lore.add("Bank Mode");
                break;
            case STATION:
                lore.add("Station Mode");
                break;
            case OFF:
                lore.add("Disabled");
                break;
        }
        im.setLore(lore);
        rod.setItemMeta(im);

    }


    public enum BuildMode {
        TOWN,
        BANK,
        STATION,
        OFF
    }
}


