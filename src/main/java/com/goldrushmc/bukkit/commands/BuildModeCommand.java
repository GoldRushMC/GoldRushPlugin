package com.goldrushmc.bukkit.commands;

import com.goldrushmc.bukkit.defaults.CommandDefault;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class BuildModeCommand extends CommandDefault {

    public static Map<Player, BuildMode> buildMode = new HashMap<>();

    public BuildModeCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args[0].equalsIgnoreCase("Town")) {
            buildMode.put((Player) sender, BuildMode.TOWN);
        }
        else if(args[0].equalsIgnoreCase("Bank")) {
            buildMode.put((Player) sender, BuildMode.BANK);
        }
        else if(args[0].equalsIgnoreCase("Station")) {
            buildMode.put((Player) sender, BuildMode.STATION);
        }
        else if(args[0].equalsIgnoreCase("Stop") || args[0].equalsIgnoreCase("End")) {
            buildMode.put((Player) sender, BuildMode.OFF);
        }
        return false;
    }


    public enum BuildMode {
        TOWN,
        BANK,
        STATION,
        OFF;
    }
}


