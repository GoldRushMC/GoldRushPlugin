package com.goldrushmc.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.chunkpersist.ChunkLoaderLis;
import com.goldrushmc.bukkit.defaults.CommandDefault;
import com.goldrushmc.bukkit.defaults.GoldRushPerms;
import com.goldrushmc.bukkit.train.listeners.TrainStationLis;
import com.goldrushmc.bukkit.train.station.TrainStation;

public class RemoveTrainStation extends CommandDefault {

	public RemoveTrainStation(JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!sender.hasPermission(GoldRushPerms.REMOVE)) { deny(sender); return true;}
		
		int starting = TrainStation.getTrainStations().size();
		
		for(TrainStation station : TrainStation.getTrainStations()) {
			if(station.getStationName().equalsIgnoreCase(args[0])) {
				TrainStationLis.removeStation(station);
				ChunkLoaderLis.removeStation(station);
				TrainStation.getTrainStations().remove(station);
				break;
			}
		}
		
		if(TrainStation.getTrainStations().size() < starting) { sender.sendMessage("Train station removed successfully!"); return true; }
		
		return false;
	}

}
