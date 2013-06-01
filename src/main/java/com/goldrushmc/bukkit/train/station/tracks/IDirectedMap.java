package com.goldrushmc.bukkit.train.station.tracks;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public interface IDirectedMap {
	
	public BlockFace getDirection();
	
	public Block getNext();
	
	public Block getLast();
	
	public Block getCurrent();
	
	public Block peekNext();
	
	public void setDirection(BlockFace dir);
	
	public boolean hasNext();
	
	public Block getBlockAt(BlockFace dir);
	
	public List<Block> getBlockList();

	public List<Material> getSearchTypes();
	
	public void setSearchTypes(List<Material> types);
	
	public boolean isDone();
	
	public void goOtherWay();
}
