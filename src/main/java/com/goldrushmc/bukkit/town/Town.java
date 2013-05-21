package com.goldrushmc.bukkit.town;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.train.exceptions.MarkerNumberException;

/**
 * Will be used to find town areas and facilitate plots of land.
 * 
 * @author Lucas
 *
 */
public class Town extends BlockFinder{

	public Town(World world, List<Location> coords)	throws MarkerNumberException {
		super(world, coords);
	}

}
