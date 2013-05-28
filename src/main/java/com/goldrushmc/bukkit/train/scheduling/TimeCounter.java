package com.goldrushmc.bukkit.train.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.train.station.TrainStation;

/**
 * Should be set to run every tick, for each world it is in.
 * <p>
 * Controls the scheduling of trains in a more configurable way.
 * 
 * @author Diremonsoon
 *
 */
public class TimeCounter implements Runnable {

	public long currentTime = 0, publik = 0, transport = 0, hub = 0;
	private final JavaPlugin plugin;
	private Map<String, Long> times = new HashMap<String, Long>();
	private final Map<String, List<TrainStation>> stations = new HashMap<String, List<TrainStation>>();

	private final World world;

	/**
	 * Make sure that the plugin already has a config before initializing this class!!!
	 * 
	 * @param plugin
	 * @param world
	 */
	public TimeCounter(JavaPlugin plugin, World world) {
		this.world = world;
		this.plugin = plugin;
		//Initialize Lists
		stations.put("Public", new ArrayList<TrainStation>());
		stations.put("Transport", new ArrayList<TrainStation>());
		stations.put("Hub", new ArrayList<TrainStation>());
		//Load config
		FileConfiguration fc =  plugin.getConfig();
		if(fc != null) {
			times.put("Public", fc.getLong("station.times.public"));
			times.put("Transport", fc.getLong("station.times.transport"));
			times.put("Hub", fc.getLong("station.times.hub"));
		}
	}

	/**
	 * This is meant to run EVERY tick, so we don't want to do too much.
	 */
	@Override
	public void run() {
		//Get current time
		long update = this.world.getTime();
		
		if(update == 0) {
			this.publik = this.times.get("Public");
			this.transport = this.times.get("Transport");
			this.hub = this.times.get("Hub");
			this.currentTime = update;
		}

		//Every 200 ticks, do this.
		if(Math.abs(update - currentTime) == 200) {
			onUpdateTick();
			this.currentTime = update;
		}

		if(Math.abs(this.times.get("Public") - this.publik) == update) {
			if(!this.stations.get("Public").isEmpty()) {
				for(TrainStation station : this.stations.get("Public")) {
					station.pushQueue();
				}
			}
			this.publik = update;
		}
		if(Math.abs(this.times.get("Transport") - this.transport) == update) {
			if(!this.stations.get("Transport").isEmpty()) {
				Bukkit.getLogger().info("Sending Transport Trains!"); //TODO
				for(TrainStation station : this.stations.get("Transport")) {
					station.pushQueue();
				}
			}
			this.transport = update;
		}
		if(Math.abs(this.times.get("Hub") - this.hub) == update) {
			if(!this.stations.get("Hub").isEmpty()) {
			}
			hub = update;
		}
	}

	public void onUpdateTick() {
		Bukkit.getLogger().info("Running update!"); //TODO
		List<TrainStation> stations = TrainStation.getTrainStations();
		//If no stations exist, we don't run an update.
		if(stations.isEmpty()) return;

		boolean add = true;
		for(TrainStation station : stations) {
			for(List<TrainStation> stationList : this.stations.values()) {
				if(stationList.contains(station)) {
					add = false;
					continue;
				}
			}
			if(add) {
				//TODO Need to add a Hub station class, and add logic to add it to scheduling.
				switch(station.getType()) {
				case DEFAULT: break;
				case PASSENGER_TRANS: this.stations.get("Public").add(station); break;
				case STORAGE_TRANS: this.stations.get("Transport").add(station); break;
				}
			}
		}
	}
}
