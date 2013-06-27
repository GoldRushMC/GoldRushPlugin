package com.goldrushmc.bukkit.town.shop;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.trainstation.exceptions.MarkerNumberException;

public abstract class AbstractShop extends BlockFinder implements IShop<ItemStack> {
	
	protected String name;

	public AbstractShop(World world, List<Location> coords, JavaPlugin plugin) throws MarkerNumberException {
		super(world, coords, plugin);
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return name;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

	@Override
	public void add() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Block> findNonAirBlocks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@EventHandler
	public void onPlayerDamageAttempt(BlockDamageEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	@EventHandler
	public void onPlayerBreakAttempt(BlockBreakEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	@EventHandler
	public void onPlayerPlaceAttempt(BlockPlaceEvent event) {
		// TODO Auto-generated method stub

	}

}
