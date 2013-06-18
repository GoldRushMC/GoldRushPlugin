package com.goldrushmc.bukkit.commands;

import com.goldrushmc.bukkit.defaults.CommandDefault;
import com.goldrushmc.bukkit.defaults.GoldRushPerms;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class StationWand extends CommandDefault {

    public StationWand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!isPlayer(sender)) {
            sender.sendMessage("You cannot use this command from the console.");
            return true;
        }
        if (!sender.hasPermission(GoldRushPerms.ADD)) { deny(sender); return true; }

        if (!BuildModeCommand.buildMode.containsKey((Player) sender)) {
            BuildModeCommand.buildMode.put((Player) sender, BuildModeCommand.BuildMode.STATION);
            sender.sendMessage("Your build mode has been set to STATION by default. \n"
                    + "Do /buildmode [mode] to change it");
        }

        //Get player
        Player p = (Player) sender;

        //Get the associated lore with regards to the build mode
        List<String> lore = new ArrayList<>();
        switch (BuildModeCommand.buildMode.get(p)) {
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

        //Create the tool
        ItemStack blazeRod = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = blazeRod.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName("Building Tool");
        blazeRod.setItemMeta(meta);
        p.setItemInHand(blazeRod);

        return true;
    }
}
