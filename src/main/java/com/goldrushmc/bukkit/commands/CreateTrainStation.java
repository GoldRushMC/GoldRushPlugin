package com.goldrushmc.bukkit.commands;

import com.goldrushmc.bukkit.defaults.CommandDefault;
import com.goldrushmc.bukkit.defaults.GoldRushPerms;
import com.goldrushmc.bukkit.train.listeners.WandLis;
import com.goldrushmc.bukkit.train.station.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;


/**
 * For the command "CreateStation"
 *
 * @author Diremonsoon
 */
public class CreateTrainStation extends CommandDefault {

    public CreateTrainStation(JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Needs the type of station followed by the name of the station
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission(GoldRushPerms.ADD)) {
            deny(sender);
            return false;
        }
        if (!(args.length == 2) && !(args.length == 3)) return false;

        Player p = (Player) sender;

        if (!WandLis.wandLoc.containsKey(p)) {
            sender.sendMessage("Create some markers first!");
            return true;
        }

        int size = WandLis.wandLoc.get(p).size();

        if (!(size == 2)) {
            sender.sendMessage("You need " + (2 - size) + " more markers");
            return true;
        }

        List<Location> locs = WandLis.wandLoc.get(p);

        StationType type = StationType.findType(args[0]);
        if (type == null) {
            p.sendMessage("Please use a legitimate station type");
            return true;
        }

        boolean trainSpawn = false;
        if (args.length == 3 && args[2].equals("train")) trainSpawn = true;

        int initial = TrainStation.getTrainStations().size();

        try {
            switch (type) {
                case DEFAULT:
                    new TrainStationTransport(plugin, args[1], locs, p.getWorld(), trainSpawn, true);
                    break;
                case PASSENGER_TRANS:
                    new PublicTrainStation(plugin, args[1], locs, p.getWorld(), trainSpawn, true);
                    break;
                case STORAGE_TRANS:
                    new TrainStationTransport(plugin, args[1], locs, p.getWorld(), trainSpawn, true);
                    break;
                case HYBRID_TRANS:
                    new HybridTrainStation(plugin, args[1], locs, p.getWorld(), trainSpawn, true);
                    break;
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning(e.getMessage());
            e.printStackTrace();
            if (TrainStation.getTrainStations().size() > initial) {
                TrainStation.getTrainStations().remove(initial);
            }
            return false;
        }

        WandLis.wandLoc.remove(p);
        p.sendMessage("Markers reset!");

        return true;

    }
}