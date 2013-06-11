package com.goldrushmc.bukkit.train.station.tracks;

<<<<<<< HEAD
import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.town.Town;
import com.goldrushmc.bukkit.train.station.TrainStation;
import org.bukkit.block.Block;

=======
>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

<<<<<<< HEAD
/**
 * Crawls along the rails, and makes sure that it doesn't take up too much memory.
 * <p/>
 * Would be just a method in a class, but this requires memory collection, and must be cancellable.
 *
 * @author Diremonsoon
 */
class Crawler implements Runnable {

    List<IDirectedMap> maps;
    Map<Block, BlockFinder> boundaries;
    Map<IDirectedMap, List<BlockFinder>> objectMap = new HashMap<IDirectedMap, List<BlockFinder>>();

    public Crawler(List<IDirectedMap> maps) {
        //Gives the maps with initial block (not yet mapped) to map.
        this.maps = maps;
        for (IDirectedMap map : maps) {
            objectMap.put(map, new ArrayList<BlockFinder>());
        }
        //Gets the towns currently in existence and makes a map of boundaries.
        //Which will be used to check to see if the mapping needs to stop, and also for adding TrackMapper classes to the right map.
        boundaries = new HashMap<Block, BlockFinder>();
        for (Town t : Town.getTowns()) {
            for (Block b : t.getTownArea()) {
                boundaries.put(b, t);
            }
        }
        for (TrainStation s : TrainStation.getTrainStations()) {
            for (Block b : s.getTrainArea()) {
                boundaries.put(b, s);
            }
        }

    }

    @Override
    public void run() {
        for (IDirectedMap map : this.maps) {
            //Find ALL boundaries involved with this rail system.
            while (!map.isDone()) {
                Block next = map.getNext();
                if (map.isDone()) break;
                //Entering a border of something.
                if (boundaries.containsKey(next) && !boundaries.containsKey(map.getLast())) {
                    objectMap.get(map).add(boundaries.get(next));
                }
                //Leaving the border of something. We only care if the mapper hasn't added the item to the map yet.
                else if (!boundaries.containsKey(next) && boundaries.containsKey(map.getLast())) {
                    if (!objectMap.get(map).contains(boundaries.get(map.getLast()))) {
                        objectMap.get(map).add(boundaries.get(map.getLast()));
                    }
                }
            }
            //Reverse the direction of the crawl, starting at the original block.
            map.goOtherWay();
            while (!map.isDone()) {
                Block next = map.getNext();
                if (map.isDone()) break;
                //Entering a border of something.
                if (boundaries.containsKey(next) && !boundaries.containsKey(map.getLast())) {
                    objectMap.get(map).add(boundaries.get(next));
                }
                //Leaving the border of something. We only care if the mapper hasn't added the item to the map yet.
                else if (!boundaries.containsKey(next) && boundaries.containsKey(map.getLast())) {
                    if (!objectMap.get(map).contains(boundaries.get(map.getLast()))) {
                        objectMap.get(map).add(boundaries.get(map.getLast()));
                    }
                }
            }
        }
    }

    public List<IDirectedMap> getRailMaps() {
        return this.maps;
    }

    public List<BlockFinder> getRailObjects(IDirectedMap map) {
        return objectMap.get(map);
    }
=======
import org.bukkit.block.Block;

import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.town.Town;
import com.goldrushmc.bukkit.train.station.TrainStation;

/**
 * Crawls along the rails, and makes sure that it doesn't take up too much memory.
 * <p>
 * Would be just a method in a class, but this requires memory collection, and must be cancellable.
 * 
 * @author Diremonsoon
 *
 */
class Crawler implements Runnable {

	List<IDirectedMap> maps;
	Map<Block, BlockFinder> boundaries;
	Map<IDirectedMap, List<BlockFinder>> objectMap = new HashMap<IDirectedMap, List<BlockFinder>>();

	public Crawler(List<IDirectedMap> maps) {
		//Gives the maps with initial block (not yet mapped) to map.
		this.maps = maps;
		for(IDirectedMap map : maps) {
			objectMap.put(map, new ArrayList<BlockFinder>());
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
		for(IDirectedMap map : this.maps) {
			//Find ALL boundaries involved with this rail system.
			while(!map.isDone()) {
				Block next = map.getNext();
				if(map.isDone()) break;
				//Entering a border of something.
				if(boundaries.containsKey(next) && !boundaries.containsKey(map.getLast())) {
					objectMap.get(map).add(boundaries.get(next));
				}
				//Leaving the border of something. We only care if the mapper hasn't added the item to the map yet.
				else if(!boundaries.containsKey(next) && boundaries.containsKey(map.getLast())) {
					if(!objectMap.get(map).contains(boundaries.get(map.getLast()))) {
						objectMap.get(map).add(boundaries.get(map.getLast()));
					}
				}
			}
			//Reverse the direction of the crawl, starting at the original block.
			map.goOtherWay();
			while(!map.isDone()) {
				Block next = map.getNext();
				if(map.isDone()) break;
				//Entering a border of something.
				if(boundaries.containsKey(next) && !boundaries.containsKey(map.getLast())) {
					objectMap.get(map).add(boundaries.get(next));
				}
				//Leaving the border of something. We only care if the mapper hasn't added the item to the map yet.
				else if(!boundaries.containsKey(next) && boundaries.containsKey(map.getLast())) {
					if(!objectMap.get(map).contains(boundaries.get(map.getLast()))) {
						objectMap.get(map).add(boundaries.get(map.getLast()));
					}
				}
			}
		}
	}
	public List<IDirectedMap> getRailMaps() {
		return this.maps;
	}

	public List<BlockFinder> getRailObjects(IDirectedMap map) {
		return objectMap.get(map);
	}
>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
}
