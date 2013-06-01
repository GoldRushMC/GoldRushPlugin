package com.goldrushmc.bukkit.train.station.tracks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.train.util.TrainTools;

/**
 * Will be in charge of mapping ALL of the rails for any place.
 * <p>
 * Train stations will just check to see if they have rails within these mappings, and if they do,
 * the station can know where they go and what direction they go in.
 * 
 * @author Diremonsoon
 *
 */
public class TrackMapper {

	public static Map<TrackMapper, List<BlockFinder>> listOfMaps = new HashMap<TrackMapper, List<BlockFinder>>();
	//Most west rail is south, most north rail is west.
	//Most east rail is north, most south rail is east.
	public List<IDirectedMap> maps = new ArrayList<IDirectedMap>();
	public Crawler crawler;


	public TrackMapper(List<Block> toCrawl, JavaPlugin plugin) {
		for(Block b : toCrawl) {
			IDirectedMap dm = new DirectedMap(getBearing(b), b);

			List<Material> types = new ArrayList<Material>();
			types.add(Material.RAILS);
			types.add(Material.ACTIVATOR_RAIL);
			types.add(Material.DETECTOR_RAIL);
			types.add(Material.POWERED_RAIL);
			dm.setSearchTypes(types);

			maps.add(dm);
		}

		crawler = new Crawler(this.maps);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, crawler);
	}

	public static Map<TrackMapper, List<BlockFinder>> getTrackMaps() {
		return listOfMaps;
	}



	/**
	 * Applicable to rails only. Returns null if not a rail.
	 * 
	 * @param b
	 * @return
	 */
	public BlockFace getBearing(Block b) {
		if(!TrainTools.isRail(b)) return null;

		switch(b.getData()) {
		case 0: return BlockFace.NORTH;
		case 1: return BlockFace.WEST;
		default: return BlockFace.SELF;
		}
	}
}
