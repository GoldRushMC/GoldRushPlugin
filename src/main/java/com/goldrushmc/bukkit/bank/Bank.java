package com.goldrushmc.bukkit.bank;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.train.exceptions.MarkerNumberException;

/**
 * Will be used to facilitate an economy. 
 * 
 * @author Lucas
 *
 */
public abstract class Bank extends BlockFinder {

	public Bank(World world, List<Location> coords, JavaPlugin plugin)	throws MarkerNumberException {
		super(world, coords, plugin);
	}

}
