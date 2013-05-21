package com.goldrushmc.bukkit.chunkpersist;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.defaults.DefaultListener;
import com.goldrushmc.bukkit.train.station.TrainStation;

/**
 * This will prevent our train stations, banks, and towns from unloading.
 * 
 * @author Diremonsoon
 *
 */
public class ChunkLoaderLis extends DefaultListener {

	private static List<Chunk> loadedChunks = new ArrayList<Chunk>();
	
	public ChunkLoaderLis(JavaPlugin plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		List<TrainStation> loaded = TrainStation.getTrainStations();
		for(TrainStation station : loaded) {
			if(station.getChunks().contains(event.getChunk())) {
				event.setCancelled(true);
			}
		}
	}
	
	public void populate() {
		if(!TrainStation.getTrainStations().isEmpty()) {
			for(TrainStation station : TrainStation.getTrainStations()) {
				loadedChunks.addAll(station.getChunks());
			}
		}
	}
	public static void addStation(TrainStation station) {
		loadedChunks.addAll(station.getChunks());
	}
	public static void removeStation(TrainStation station) {
		loadedChunks.removeAll(station.getChunks());
	}
	
}
