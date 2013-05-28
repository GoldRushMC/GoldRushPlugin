package com.goldrushmc.bukkit.mines;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class MineGenerator {
	
	public Vector mineMin, mineMax, mineEntrance;
	public int length, width, height;
	World w;
	
	public MineGenerator(World world, Vector min, Vector max, Vector ent) {
		mineEntrance = ent;
		mineMax = max;
		mineMin = min;
		w = world;
		length = mineMax.getBlockZ() - mineMin.getBlockZ();
		width = mineMax.getBlockX() - mineMin.getBlockX();
		height = mineMax.getBlockY() - mineMin.getBlockY();
	}
	
	/**
	 * Will generate the mine randomly smaller density = more features.
	 *
	 * @param density will control how many veins/tunnels will be created, leave null for default (2). 
	 * 
	 */
	public Boolean generate(Integer density) {
		
		if(density == null) { density = 2; }
		
		int size = (width + height + length) / 3; //Mean of height/Length/Width
		
		int veins = size / density;
		
		fillSelection(w, Material.GLASS); //fills the area with chosen base block
		populateGold(w, veins, 10); //populates area with gold veins
		populateTunnels(w, 6, size / (density * 2) ); //populates the area with tunnels
		populateGravel(w, size / (density * 2)); //populates the area with gravel blobs
		return true;		
	}
	
	public Boolean contains(Vector queryBlock) {
		if(mineMax.getBlockX() > queryBlock.getBlockX() &&
				mineMax.getBlockY() > queryBlock.getBlockY() &&
				mineMax.getBlockZ() > queryBlock.getBlockZ() &&
				mineMin.getBlockX() < queryBlock.getBlockX() &&
				mineMin.getBlockY() < queryBlock.getBlockY() &&
				mineMin.getBlockZ() < queryBlock.getBlockZ()) {
			return true;
		} else { return false; }
	}
	
	private void fillSelection(World w, Material m) {
		int maxx = mineMax.getBlockX();
		int minx = mineMin.getBlockX();
		int maxz = mineMax.getBlockZ();
		int minz = mineMin.getBlockZ();
		int maxy = mineMax.getBlockY();
		int miny = mineMin.getBlockY();
		
		//simple loop to fill area
		for(int x = minx; x <= maxx; x++) {
			for(int z = minz; z <= maxz; z++) {
				for(int y = miny; y <= maxy; y++) {
						w.getBlockAt(x, y, z).setType(m);
				}
			}
		}
	}
	
	private void changeSides(Block b, Material m) {
		Block north = b.getRelative(BlockFace.NORTH);
		Block west = b.getRelative(BlockFace.WEST);
		Block east = b.getRelative(BlockFace.EAST);
		Block south = b.getRelative(BlockFace.SOUTH);
		Block northEast = b.getRelative(BlockFace.NORTH_EAST);
		Block northWest = b.getRelative(BlockFace.NORTH_WEST);
		Block southEast = b.getRelative(BlockFace.SOUTH_EAST);
		Block southWest = b.getRelative(BlockFace.SOUTH_WEST);
		Block northUp = north.getRelative(BlockFace.UP);
		Block eastUp = east.getRelative(BlockFace.UP);
		Block westhUp = west.getRelative(BlockFace.UP);
		Block southUp = south.getRelative(BlockFace.UP);
		Block northEastUp = northEast.getRelative(BlockFace.UP);
		Block northWestUp = northWest.getRelative(BlockFace.UP);
		Block southEastUp = southEast.getRelative(BlockFace.UP);
		Block southWestUp = southWest.getRelative(BlockFace.UP);
		Block northDown = north.getRelative(BlockFace.DOWN);
		Block eastDown = east.getRelative(BlockFace.DOWN);
		Block westhDown = west.getRelative(BlockFace.DOWN);
		Block southDown = south.getRelative(BlockFace.DOWN);
		Block northEastDown = northEast.getRelative(BlockFace.DOWN);
		Block northWestDown = northWest.getRelative(BlockFace.DOWN);
		Block southEastDown = southEast.getRelative(BlockFace.DOWN);
		Block southWestDown = southWest.getRelative(BlockFace.DOWN);
		Block up = b.getRelative(BlockFace.UP);
		Block down = b.getRelative(BlockFace.DOWN);
		
		Block northNorth = north.getRelative(BlockFace.NORTH);
		Block southSouth = south.getRelative(BlockFace.SOUTH);
		Block eastEast = east.getRelative(BlockFace.EAST);
		Block westWest = west.getRelative(BlockFace.WEST);
		Block upUp = up.getRelative(BlockFace.UP);
		Block downDown = down.getRelative(BlockFace.DOWN);
		
		north.setType(m);
		west.setType(m);
		east.setType(m);
		south.setType(m);
		northEast.setType(m);
		northWest.setType(m);
		southEast.setType(m);
		southWest.setType(m);
		northUp.setType(m);
		eastUp.setType(m);
		westhUp.setType(m);
		southUp.setType(m);
		northEastUp.setType(m);
		northWestUp.setType(m);
		southEastUp.setType(m);
		southWestUp.setType(m);
		northDown.setType(m);
		eastDown.setType(m);
		westhDown.setType(m);
		southDown.setType(m);
		northEastDown.setType(m);
		northWestDown.setType(m);
		southEastDown.setType(m);
		southWestDown.setType(m);
		up.setType(m);
		down.setType(m);
		
		northNorth.setType(m);
		southSouth.setType(m);
		eastEast.setType(m);
		westWest.setType(m);
		upUp.setType(m);
		downDown.setType(m);
		
	}
	
	private void populateGold(World w, Integer numVeins, Integer veinLength) {
		
		//loops on number of veins specified
		for(int i = 0; i < numVeins; i++){
			//uses a random set of vectors to get the points for the gold vein to be drew.
			Vector location = randomConstrainedVector(mineMin, mineMax);
			Vector newLocation = randomConstrainedVector(mineMin, mineMax);
			
			//limit the length of ore veins
			int distance = (int) Math.floor(location.distance(newLocation) - 1);
			if((int) Math.floor(location.distance(newLocation) - 1) > veinLength) {
				distance = veinLength;
			}
			
			BlockIterator bli = new BlockIterator(w, location, new Vector(newLocation.getBlockX()-location.getBlockX(), newLocation.getBlockY()-location.getBlockY(), newLocation.getBlockZ()-location.getBlockZ()), 0, distance - 1);
		
			//BlockIterator draws lines of a block between two specified Vectors
			Location blockToAdd;
			while(bli.hasNext()) {
				blockToAdd = bli.next().getLocation();
				w.getBlockAt(new Location(blockToAdd.getWorld(), blockToAdd.getBlockX(), blockToAdd.getBlockY(), blockToAdd.getBlockZ())).setType(Material.GOLD_ORE);
			}
		}
	}
	
	private void populateGravel(World w, Integer numPatches) {
		for(int i = 0; i < numPatches; i++) {
			Vector pos = randomConstrainedVector(mineMin, mineMax);
			
			Block blockToChange = w.getBlockAt(pos.toLocation(w));
			blockToChange.setType(Material.GRAVEL);
			
			//also changes adjacent blocks
			changeSides(blockToChange, Material.GRAVEL);
		}
	}
	
	private void populateTunnels(World w, Integer numBranches, Integer numTunnels) {
		//initial tunnel start point
		Vector startLocation = mineEntrance;
		Vector minArea = new Vector(0,0,0), maxArea = new Vector(0,0,0);
		
		//loops through num of tunnels
		for(int tunnels = 0; tunnels < numTunnels; tunnels++) {
			//loops through num of branches, each one starts at the previous end.
			for(int branch = 0; branch < numBranches; branch++) {
				//specifies the maximum distances the random Vector can be from the Last point
				minArea = new Vector(startLocation.getBlockX() - 8, startLocation.getBlockY() - 3, startLocation.getBlockZ() - 8);
				maxArea = new Vector(startLocation.getBlockX() + 8, startLocation.getBlockY() + 3, startLocation.getBlockZ() + 8);		
			
				Vector endLocation = new Vector(0,0,0);
				
				int limit = 0; // counter used in loop to prevent infinite state
				Boolean nextPointFound = false;
				
				//Loops until the random point is within the mine area
				while(!nextPointFound) {
					endLocation = randomConstrainedVector(minArea, maxArea);
					if(contains(endLocation)) {
						nextPointFound = true;
						break;
					}				
					limit++;
					if(limit == 50) { nextPointFound = false; break; }
				}
		
				if(nextPointFound){
					BlockIterator bli = new BlockIterator(w, startLocation, new Vector(endLocation.getBlockX()-startLocation.getBlockX(), endLocation.getBlockY()-startLocation.getBlockY(), endLocation.getBlockZ()-startLocation.getBlockZ()), 0, (int) Math.floor(startLocation.distance(endLocation)+ 1));
		
					Location blockToAdd;
					while(bli.hasNext()) {
						blockToAdd = bli.next().getLocation();
						Block blockToChange = w.getBlockAt(new Location(blockToAdd.getWorld(), blockToAdd.getBlockX(), blockToAdd.getBlockY(), blockToAdd.getBlockZ()));
						blockToChange.setType(Material.AIR);
						
						//also changes adjacent blocks
						changeSides(blockToChange, Material.AIR);
					}
				
					//makes next segment start the end of previous
					startLocation = endLocation;
				} else {
					break;
				}
			}
			//randomises location of next tunnel
			startLocation = randomConstrainedVector(mineMin, mineMax);
		}		
	}
	
	//returns a vector that is within the quad of the two specified vectors
	private Vector randomConstrainedVector(Vector minVector, Vector maxVector) {
		Random rand = new Random();
		int x, y, z;
		int min, max;

		max = maxVector.getBlockX();
		min = minVector.getBlockX();
		x = rand.nextInt(max - min + 1) + min;
		
		max = maxVector.getBlockY();
		min = minVector.getBlockY();
		y = rand.nextInt(max - min + 1) + min;
		
		max = maxVector.getBlockZ();
		min = minVector.getBlockZ();
		z = rand.nextInt(max - min + 1) + min;
		
		return new Vector(x, y, z);
	}
}
