package com.goldrushmc.bukkit.town;

import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.trainstation.exceptions.MarkerNumberException;
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
 *
 */
public abstract class Town extends BlockFinder{

    protected static List<Town> towns = new ArrayList<>();

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
}
