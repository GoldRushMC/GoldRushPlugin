package com.goldrushmc.bukkit.train.station.tracks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class DirectedMap implements IDirectedMap {
	
	public List<Block> mapped;
	public List<Material> types = new ArrayList<Material>();
	public Block last;
	public Block focus;
	public BlockFace toSearch;
	public Block block;

	public DirectedMap(BlockFace dir, Block start) {
		this.block = start;
		this.toSearch = dir;
		this.mapped = new LinkedList<Block>();
	}
	
	@Override
	public BlockFace getDirection() {
		return toSearch;
	}

	@Override
	public Block getNext() {
		if(focus == block) focus = block.getRelative(toSearch);
		else {
			last = focus;
			focus = focus.getRelative(toSearch);
		}
		mapped.add(focus);
		return focus;
	}
	
	@Override
	public Block peekNext() {
		if(focus == block) return block.getRelative(toSearch);
		else return focus.getRelative(toSearch);
	}

	@Override
	public Block getLast() {
		return last;
	}

	@Override
	public void setDirection(BlockFace dir) {
		this.toSearch = dir;
	}

	@Override
	public boolean hasNext() {
		return types.contains(focus.getRelative(toSearch).getType());
	}

	@Override
	public Block getBlockAt(BlockFace dir) {
		return block.getRelative(dir);
	}

	@Override
	public List<Block> getBlockList() {
		return mapped;
	}

	@Override
	public List<Material> getSearchTypes() {
		return types;
	}

	@Override
	public void setSearchTypes(List<Material> types) {
		this.types = types;		
	}

	@Override
	public Block getCurrent() {
		return focus;
	}

}
