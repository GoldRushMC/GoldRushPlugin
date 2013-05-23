package com.goldrushmc.bukkit.mines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.goldrushmc.bukkit.defaults.CommandDefault;

public class MineTool  extends CommandDefault {
	
	public static Map<Player, List<Integer>> mineSize = new HashMap<Player, List<Integer>>();
	public static Map<Player, List<blockBackup>> backList = new HashMap<Player, List<blockBackup>>();

	public MineTool(JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		Player p = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("mine")){ // command label
			if (p instanceof Player){ // instance of player - checks if player
				if (args.length > 0){
					if (args[0].equalsIgnoreCase("create")){ 
							try{
								p.sendMessage("Created mine outline");
								p.sendMessage("Type " + ChatColor.GREEN + "/mine confirm " + 
										ChatColor.WHITE + "to create the mine, " +
										ChatColor.RED + "/mine cancel " + ChatColor.WHITE + "to start again.");
								makeMarkers(p.getWorld(), p, Material.WOOL); //makes a wool outline of the area selected
								return true;
							} catch(Exception ex) {
								p.sendMessage(ChatColor.RED + "Invalid Paramters!");
								return false;
							}
					} else if (args[0].equalsIgnoreCase("tool")){ 
						
						ItemStack panningTool= new ItemStack(Material.CLAY_BALL);
						List<String> lore = new ArrayList<String>();
						lore.add("Right click to denote start of mine");
						ItemMeta meta = panningTool.getItemMeta();
						meta.setLore(lore);
						meta.setDisplayName("Mine Creator");
						panningTool.setItemMeta(meta);
						p.getInventory().addItem(panningTool);
						return true;
						
					} else if (args[0].equalsIgnoreCase("confirm")){ 
						
						int density = 5; //effects the spawn rate of ores and caves, the higher, the less
						
						int size = (MineLis.worldEditPlugin.getSelection(p).getWidth() + 
								MineLis.worldEditPlugin.getSelection(p).getHeight() + 
								MineLis.worldEditPlugin.getSelection(p).getLength()) / 3; //Mean of height/Length/Width
						
						int veins = size / density;
						
						fillSelection(p.getWorld(), p, Material.GLASS); //fills the area with chosen base block
						populateGold(p.getWorld(), p, veins, 10); //populates area with gold veins
						populateTunnels(p.getWorld(), p, 6, size / (density * 2) ); //populates the area with tunnels
						
						return true;
					} else if (args[0].equalsIgnoreCase("cancel")){ 
						undoMarkers(p.getWorld(), p); //undoes the placement of wool outline
						return true;
					} else {
						p.sendMessage("Not a valid command!");
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
		return false;
		}
	}
	
	private void fillSelection(World w, Player p, Material m) {
		int maxx = MineLis.mineMax.get(p).getBlockX();
		int minx = MineLis.mineMin.get(p).getBlockX();
		int maxz = MineLis.mineMax.get(p).getBlockZ();
		int minz = MineLis.mineMin.get(p).getBlockZ();
		int maxy = MineLis.mineMax.get(p).getBlockY();
		int miny = MineLis.mineMin.get(p).getBlockY();
		
		//simple loop to fill area
		for(int x = minx; x <= maxx; x++) {
			for(int z = minz; z <= maxz; z++) {
				for(int y = miny; y <= maxy; y++) {
						w.getBlockAt(x, y, z).setType(m);
				}
			}
		}
	}
	
	private void populateGold(World w, Player p, Integer numVeins, Integer veinLength) {
		
		//loops on number of veins specified
		for(int i = 0; i < numVeins; i++){
			//uses a random set of vectors to get the points for the gold vein to be drew.
			Vector location = randomConstrainedVector(MineLis.mineMin.get(p), MineLis.mineMax.get(p));
			Vector newLocation = randomConstrainedVector(MineLis.mineMin.get(p), MineLis.mineMax.get(p));
			
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
	
	private void populateTunnels(World w, Player p, Integer numBranches, Integer numTunnels) {
		//initial tunnel start point
		Vector startLocation = MineLis.mineLoc.get(p).toVector();
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
				while(true) {
					endLocation = randomConstrainedVector(minArea, maxArea);
					if(MineLis.worldEditPlugin.getSelection(p).contains(endLocation.toLocation(w))) {
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
						for(BlockFace face : BlockFace.values())
							blockToChange.getRelative(face).setType(Material.AIR);
					}
				
					//makes next segment start the end of previous
					startLocation = endLocation;
				} else {
					break;
				}
			}
			//randomises location of next tunnel
			startLocation = randomConstrainedVector(MineLis.mineMin.get(p), MineLis.mineMax.get(p));
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
	
	private void makeMarkers(World w, Player p, Material m) {
		
		int maxx = MineLis.mineMax.get(p).getBlockX();
		int minx = MineLis.mineMin.get(p).getBlockX();
		int maxz = MineLis.mineMax.get(p).getBlockZ();
		int minz = MineLis.mineMin.get(p).getBlockZ();
		int maxy = MineLis.mineMax.get(p).getBlockY();
		int miny = MineLis.mineMin.get(p).getBlockY();
		
		//Backup array
		List<blockBackup> temp = new ArrayList<blockBackup>();
		
		if(backList.containsKey(p)){
			if(backList.get(p).size() > 0) {
				undoMarkers(w, p);
			}
		}
		
		//loops to only fill the edges of the cube
		backList.remove(p);
		for(int x = minx; x <= maxx; x++) {
			for(int z = minz; z <= maxz; z++) {
				for(int y = miny; y <= maxy; y++) {
					if(x == minx && y == miny){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == maxx && y == maxy){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == maxx && y == miny){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == minx && y == maxy){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(z == minz && y == miny){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(z == maxz && y == maxy){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(z == maxz && y == miny){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(z == minz && y == maxy){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == minx && z == minz){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == maxx && z == maxz){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == maxx && z == minz){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					} else if(x == minx && z == maxz){
						temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
						w.getBlockAt(x, y, z).setType(m);
					}
				}
			}
		}
		
		backList.put(p, temp);
	}
	
	private void undoMarkers(World w, Player p) {
		for(int i = 0; i < backList.get(p).size(); i++){
			w.getBlockAt(backList.get(p).get(i).location).setType(backList.get(p).get(i).material);
		}
	}
	
	//class for storing the block backup data
	class blockBackup {
		public Location location;
		public Material material;
	
		public blockBackup(Location loc, Material mat) {
			location = loc;
			material = mat;
		}
	}
}
