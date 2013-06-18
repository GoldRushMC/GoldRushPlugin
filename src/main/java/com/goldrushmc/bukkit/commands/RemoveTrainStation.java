package com.goldrushmc.bukkit.commands;

import com.goldrushmc.bukkit.defaults.CommandDefault;
import com.goldrushmc.bukkit.defaults.GoldRushPerms;
import com.goldrushmc.bukkit.trainstation.TrainStation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoveTrainStation extends CommandDefault {

    public RemoveTrainStation(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission(GoldRushPerms.REMOVE)) {
            deny(sender);
            return true;
        }

        int starting = TrainStation.getTrainStations().size();

        for (TrainStation station : TrainStation.getTrainStations()) {
            if (station.getStationName().equalsIgnoreCase(args[0])) {
//				TrainStationLis.removeStation(types);
//				ChunkLoaderLis.removeStation(types);
//				TrainStation.getTrainStations().remove(types);
//				break;
                station.remove();
                break;
            }
        }

        if (TrainStation.getTrainStations().size() < starting) {
            sender.sendMessage("Train types removed successfully!");
            return true;
        }

        return false;
    }

}
