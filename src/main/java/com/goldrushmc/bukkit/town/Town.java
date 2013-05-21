package com.goldrushmc.bukkit.town;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.train.exceptions.MarkerNumberException;

/**
 * Will be used to find town areas and facilitate plots of land.
 * 
 * @author Lucas
 *
 */
public abstract class Town extends BlockFinder{

	public Town(World world, List<Location> coords, JavaPlugin plugin)	throws MarkerNumberException {
		super(world, coords, plugin);
	}
}
