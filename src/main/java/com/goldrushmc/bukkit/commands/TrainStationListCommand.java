package com.goldrushmc.bukkit.commands;

import com.goldrushmc.bukkit.defaults.CommandDefault;
import com.goldrushmc.bukkit.defaults.GoldRushPerms;
import com.goldrushmc.bukkit.train.station.TrainStation;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class TrainStationListCommand extends CommandDefault {

    public TrainStationListCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission(GoldRushPerms.LIST)) {
            deny(sender);
            return true;
        }

        if (TrainStation.getTrainStations().isEmpty()) {
            sender.sendMessage("There are no train stations currently in existence.");
            return true;
        }

        sender.sendMessage("List of Train Stations: ");
        int i = 1;
        for (TrainStation station : TrainStation.getTrainStations()) {
            sender.sendMessage(i + ". " + ChatColor.YELLOW + station.getStationName());
        }

        return true;
    }

}
