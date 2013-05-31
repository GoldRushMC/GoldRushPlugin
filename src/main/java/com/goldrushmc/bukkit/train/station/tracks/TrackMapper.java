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
import com.goldrushmc.bukkit.town.Town;
import com.goldrushmc.bukkit.train.station.TrainStation;
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
	public Crawl crawler;


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

		crawler = new Crawl(this.maps);

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, crawler);
	}

	public static Map<TrackMapper, List<BlockFinder>> getTrackMaps() {
		return listOfMaps;
	}

	/**
	 * Crawls along the rails, and makes sure that it doesn't take up too much memory.
	 * <p>
	 * Would be just a method in a class, but this requires memory collection, and must be cancellable.
	 * 
	 * @author Diremonsoon
	 *
	 */
	class Crawl implements Runnable {

		List<IDirectedMap> maps;
		Map<Block, BlockFinder> boundaries;
		Map<IDirectedMap, List<BlockFinder>> mapToListedLands = new HashMap<IDirectedMap, List<BlockFinder>>();

		public Crawl(List<IDirectedMap> maps) {
			//Gives the maps with initial block (not yet mapped) to map.
			this.maps = maps;
			for(IDirectedMap map : maps) {
				mapToListedLands.put(map, new ArrayList<BlockFinder>());
			}
			//Gets the towns currently in existence and makes a map of boundaries.
			//Which will be used to check to see if the mapping needs to stop, and also for adding TrackMapper classes to the right map.
			boundaries = new HashMap<Block, BlockFinder>();
			for(Town t : Town.getTowns()) {
				for(Block b : t.getTownArea()) {
					boundaries.put(b, t);
				}
			}
			for(TrainStation s : TrainStation.getTrainStations()) {
				for(Block b : s.getTrainArea()) {
					boundaries.put(b, s);
				}
			}

		}

		@Override
		public void run() {
			for(int i = 0; i < this.maps.size(); i++) {
				IDirectedMap mapTemp = this.maps.get(i);
				for(; ;) {
					if(mapTemp.hasNext()) {
						//This means it is either leaving a town, or just entering
						if(boundaries.containsKey(mapTemp.getCurrent())) {
							//This means it is entering the boundary
							if(!boundaries.containsKey(mapTemp.getLast()) && boundaries.containsKey(mapTemp.peekNext())) {
								//Add the new BlockFinder find to the list of existing BlockFinders.
								mapToListedLands.get(mapTemp).add(boundaries.get(mapTemp.peekNext()));
							}
							//This means it is leaving the town
							else if(!boundaries.containsKey(mapTemp.peekNext()) && boundaries.containsKey(mapTemp.getLast())) {
								//Make sure that we added it to the list of BlockFinders this map hits.
								if(!mapToListedLands.get(mapTemp).contains(boundaries.get(mapTemp.getLast()))) {
									//add it to the list. This may occur if we start a crawler within a town/train station.
									mapToListedLands.get(mapTemp).add(boundaries.get(mapTemp.getLast()));
								}
							}
						}
						mapTemp.getNext();
					}
					else {
						//Try to branch out, and create a new DirectedMap to iterate with.
						BlockFace branch = canBranch(mapTemp);
						if(branch != null) {
							//If it is possible, continue.
							IDirectedMap newMap = new DirectedMap(branch, mapTemp.getCurrent());
							this.maps.add(newMap);
							this.mapToListedLands.put(newMap, new ArrayList<BlockFinder>());
							break;
						}
						//We can't branch more...
						else {
							break;
						}
					}
				}
			}
		}


			public List<IDirectedMap> getRailMaps() {
				return this.maps;
			}
		}

		/**
		 * Simply returns null if it can't.
		 * 
		 * @param map
		 * @return
		 */
		protected BlockFace canBranch(IDirectedMap map) {

			switch(map.getDirection()) {
			case NORTH: {
				if(map.getSearchTypes().contains(map.getBlockAt(BlockFace.WEST).getType())) {
					return BlockFace.WEST;
				}
				else if(map.getSearchTypes().contains(map.getBlockAt(BlockFace.EAST).getType())) {
					return BlockFace.EAST;
				}

				break;
			}
			case SOUTH: {
				if(map.getSearchTypes().contains(map.getBlockAt(BlockFace.WEST).getType())) {
					return BlockFace.WEST;
				}
				else if(map.getSearchTypes().contains(map.getBlockAt(BlockFace.EAST).getType())) {
					return BlockFace.EAST;
				}
				break;
			}
			case EAST: {
				if(map.getSearchTypes().contains(map.getBlockAt(BlockFace.SOUTH).getType())) {
					return BlockFace.SOUTH;
				}
				else if(map.getSearchTypes().contains(map.getBlockAt(BlockFace.NORTH).getType())) {
					return BlockFace.NORTH;
				}
				break;
			}
			case WEST: {
				if(map.getSearchTypes().contains(map.getBlockAt(BlockFace.SOUTH).getType())) {
					return BlockFace.SOUTH;
				}
				else if(map.getSearchTypes().contains(map.getBlockAt(BlockFace.NORTH).getType())) {
					return BlockFace.NORTH;
				}
				break;
			}
			default: break;
			}
			return null;
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
