package com.goldrushmc.bukkit.train;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Rails;

@SuppressWarnings("null")
public class SmallBlockMap implements Discoverable {

	/*
	 * The main block, which sits in the center.
	 */
	private Block block;
	
	/*
	 * The blocks in each of the four cardinal directions of the main block.
	 */
	private Block east;
	private Block west;
	private Block north;
	private Block south;

	public SmallBlockMap(Block block) {
		this.block = block;

		east = calcEastBlock();
		west = calcWestBlock();
		north = calcNorthBlock();
		south = calcSouthBlock();

	}

	public Block getBlockAt(BlockFace direction) {
		switch(direction) {
		case NORTH: return this.getNorth();
		case SOUTH: return this.getSouth();
		case EAST: return this.getEast();
		case WEST: return this.getWest();
		default: return null;
		}
	}
	
	@Override
	public Block getMainBlock() {return block;}
	
	@Override
	public Block getEast() {return east;}
	
	@Override
	public Block getWest() {return west;}
	
	@Override
	public Block getNorth() {return north;}
	
	@Override
	public Block getSouth() {return south;}
	
	@Override
	public Block calcEastBlock() {
		Location loc = block.getLocation();
		loc.setX(loc.getX() + 1);
		Block b = checkUpDown(loc);
		while(b == null) {
			b = checkUpDown(b.getLocation());
		}
		return b;

	}

	@Override
	public Block calcWestBlock() {
		Location loc = block.getLocation();
		loc.setX(loc.getX() - 1);
		Block b = checkUpDown(loc);
		while(b == null) {
			b = checkUpDown(b.getLocation());
		}
		return b;
	}

	@Override
	public Block calcNorthBlock() {
		Location loc = block.getLocation();
		loc.setZ(loc.getZ() - 1);
		Block b = checkUpDown(loc);
		while(b == null) {
			b = checkUpDown(b.getLocation());
		}
		return b;
	}

	@Override
	public Block calcSouthBlock() {
		Location loc = block.getLocation();
		loc.setZ(loc.getZ() + 1);
		Block b = checkUpDown(loc);
		while(b == null) {
			b = checkUpDown(b.getLocation());
		}
		return b;
	}

	@Override
	public Block checkUpDown(Location loc) {

		if(loc.getBlock().getType().equals(Material.AIR)) {
			loc.setY(loc.getY() + 1F);
			if(block.getWorld().getBlockAt(loc).getType().equals(Material.AIR)) {
				loc.setY(loc.getY() - 2F);
				if(!block.getWorld().getBlockAt(loc).getType().equals(Material.AIR)) {
					return block.getWorld().getBlockAt(loc);			
				}
			}
		}
		return block.getWorld().getBlockAt(loc);
	}
	
	public Material getBlockType(Location loc) {
		Block b = block.getWorld().getBlockAt(loc);
		return b.getType();
	}

	@Override
	public boolean hasRailConnection() {
		return potentialConnections() >= 1;
	}
	
	public boolean nextIsRail(BlockFace direction) {
		if(this.isRail(this.getBlockAt(direction))) return true;
		else return false;
	}

	@Override
	public int potentialConnections() {
		return connectedRails().size();
	}

	@Override
	public List<Block> connectedRails() {
		List<Block> rails = new ArrayList<Block>();

		if(isRail(east)) rails.add(east);
		if(isRail(west)) rails.add(west);
		if(isRail(north)) rails.add(north);
		if(isRail(south)) rails.add(south);
		
		return rails;
	}

	@Override
	public boolean isEnd() {
		int count = 0;
		if((!isRail(east)) || (!isRail(west)) || (!isRail(south)) || (!isRail(north))) count++;
		
		if(count > 2) return true;
		return false;	
	}

	public boolean isRail(Block block) {
		return (block.getType().equals(Material.ACTIVATOR_RAIL) || block.getType().equals(Material.RAILS) || 
				block.getType().equals(Material.DETECTOR_RAIL) || block.getType().equals(Material.POWERED_RAIL));
	}


	@Override
	public BlockFace getBearing() {
		if(isRail(block)) {
			return ((Rails)block).getDirection();
		}
		return null;
	}
}
