package com.goldrushmc.bukkit.town;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.town.shop.IShop;
import com.goldrushmc.bukkit.trainstation.exceptions.MarkerNumberException;

/**
 * Will be used to find town areas and facilitate plots of land.
 *
 * @author Diremonsoon
 *
 */
public abstract class Town extends BlockFinder{

    protected static List<Town> towns = new ArrayList<>();

    public List<IShop<?>> shops;
    public List<String> citizens;
    public Map<String, Player> citizenMapping;
    
    public Town(World world, List<Location> coords, JavaPlugin plugin)	throws MarkerNumberException {
        super(world, coords, plugin);
    }
    
    public List<IShop<?>> getShops() {
    	return shops;
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

    public static Town getTownAtBlock(Block block) {
        for(Town t : towns) {
            if(t.getTownArea().contains(block)) return t;
        }
        return null;
    }
}
