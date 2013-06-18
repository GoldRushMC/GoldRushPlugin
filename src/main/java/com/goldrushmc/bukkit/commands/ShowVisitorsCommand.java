package com.goldrushmc.bukkit.commands;

import com.goldrushmc.bukkit.defaults.CommandDefault;
import com.goldrushmc.bukkit.defaults.GoldRushPerms;
import com.goldrushmc.bukkit.trainstation.TrainStation;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ShowVisitorsCommand extends CommandDefault {

    public ShowVisitorsCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission(GoldRushPerms.LIST)) {
            deny(sender);
            return true;
        }

        List<TrainStation> stations = TrainStation.getTrainStations();

        if (stations.isEmpty()) {
            sender.sendMessage("There are no stations available at this time.");
            return true;
        }

        for (TrainStation station : stations) {
            sender.sendMessage(ChatColor.GREEN + station.getStationName() + ChatColor.RESET + " has " + "("
                    + ChatColor.GREEN + station.getVisitors().size() + ChatColor.RESET + ") current visitors");
        }

        return true;
    }

}
