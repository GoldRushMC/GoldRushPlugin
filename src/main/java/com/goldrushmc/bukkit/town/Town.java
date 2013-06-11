package com.goldrushmc.bukkit.town;

<<<<<<< HEAD
import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.train.exceptions.MarkerNumberException;
=======
import java.util.ArrayList;
import java.util.List;

>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Will be used to find town areas and facilitate plots of land.
 *
 * @author Lucas
 */
public abstract class Town extends BlockFinder {

    protected static List<Town> towns = new ArrayList<Town>();

    public Town(World world, List<Location> coords, JavaPlugin plugin) throws MarkerNumberException {
        super(world, coords, plugin);
    }

    public static List<Town> getTowns() {
        return towns;
    }

    public List<Block> getTownBorder() {
        return null;
    }

<<<<<<< HEAD
    public List<Block> getTownArea() {
        return null;
    }
=======
	protected static List<Town> towns = new ArrayList<Town>();
	
	public Town(World world, List<Location> coords, JavaPlugin plugin)	throws MarkerNumberException {
		super(world, coords, plugin);
	}
	
	public static List<Town> getTowns() {
		return towns;
	}
	
	public List<Block> getTownBorder() {
		return null;
	}
	
	public List<Block> getTownArea() {
		return null;
	}
>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
}
