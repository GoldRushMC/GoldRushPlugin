package com.goldrushmc.bukkit.mines;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.main.Main;
import com.goldrushmc.bukkit.train.exceptions.MarkerNumberException;

public class Mine extends BlockFinder{
	
	public Vector mineMin, mineMax, mineEntrance;
	Location recCoordOne, recCoordTwo;
	public int length, width, height;
	int goldLeft, genedGold, density;
	Boolean isGenerated = false;
	String name;
	World w;
	
	public Mine(String name, World world, List<Location> coords, JavaPlugin plugin, 
			Vector entrance, Integer dense, Boolean isGen)
			throws MarkerNumberException {
		super(world, coords, plugin);
		
		this.name = name;
		mineEntrance = entrance;
		recCoordOne = coords.get(0);
		plugin.getServer().broadcastMessage(recCoordOne.toVector().toString());
		recCoordTwo = coords.get(1);
		plugin.getServer().broadcastMessage(recCoordTwo.toVector().toString());
		
		mineMax = findMaxBlock();
		mineMin = findMinBlock();
		
		density = dense;
		w = world;
		length = mineMax.getBlockZ() - mineMin.getBlockZ();
		width = mineMax.getBlockX() - mineMin.getBlockX();
		height = mineMax.getBlockY() - mineMin.getBlockY();
		isGenerated = isGen;
	}
	
	private Vector findMaxBlock() {
		//get first max - first block in the array
		Vector max = this.selectedArea.get(0).getLocation().toVector();
		
		for(Block b : this.selectedArea) {
			if(b.getY() < recCoordOne.getBlockY() || b.getY() < recCoordTwo.getBlockY()) {
				Vector check = new Vector(b.getX(), b.getY(), b.getZ());
				max = Vector.getMaximum(max, check);
			}
		}
		
		return max;
	}
	
	private Vector findMinBlock() {
		//get first min - first block in the array
		Vector min = this.selectedArea.get(0).getLocation().toVector();
		
		for(Block b : this.selectedArea) {
			if(b.getY() > recCoordOne.getBlockY() || b.getY() > recCoordTwo.getBlockY()) {
				Vector check = new Vector(b.getX(), b.getY(), b.getZ());
				min = Vector.getMinimum(min, check);
			}
		}
		
		return min;
	}

	@Override
	public void remove() {
		// TODO remove RefreshEvent
		
	}

	@Override
	public void add() { 
		if(!isGenerated) {
			reGenerate();
			isGenerated = true;
		}
		
		
	}

	public void getGoldLeft() {
		goldLeft = 0;
		for(Block b : this.selectedArea) {
			if(b.getType() == Material.GOLD_ORE) {
				goldLeft++;
			}
		}
	}
	
	public String getName() { return name; }
	
	public void reGenerate() {
		plugin.getServer().broadcastMessage(mineMin.toString());
		plugin.getServer().broadcastMessage(mineMax.toString());
		MineGenerator mineGen = new MineGenerator(w, mineMin, mineMax, mineEntrance);
		mineGen.generate(density);
	}
}
