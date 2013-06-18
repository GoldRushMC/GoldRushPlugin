package com.goldrushmc.bukkit.bank;

import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.trainstation.exceptions.MarkerNumberException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Will be used to facilitate an economy.
 *
 * @author Lucas
 */
public abstract class Bank extends BlockFinder {

    private Map<Player, Account> accountHolders = new HashMap<>();

    public Bank(World world, List<Location> coords, JavaPlugin plugin) throws MarkerNumberException {
        super(world, coords, plugin);
    }

}
