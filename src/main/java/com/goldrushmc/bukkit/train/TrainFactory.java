package com.goldrushmc.bukkit.train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.bergerkiller.bukkit.tc.CollisionMode;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberChest;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberRideable;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.goldrushmc.bukkit.train.util.TrainTools;
import com.goldrushmc.bukkit.train.util.TrainTools.TrainType;

/**
 * <p>This class is in charge of facilitating the TrainCarts plugin.</p>
 * <p>It will be used for creating and deleting trains, as well as keeping track of them.</p>
 * <p>This class's methods are all static, as it is only really used for its methods, and we need static maps (so that multiple instances are not created).
 * 
 * @author Diremonsoon
 * @version 1.0
 */
public class TrainFactory {

	//References the train groups and their names.
	public static Map<String, MinecartGroup> trains = new HashMap<String, MinecartGroup>();

	//References the owners to a list of minecarts they own.
	//Chest Minecart owners
	public static Map<Player, List<MinecartMemberChest>> ownerStorage = new HashMap<Player, List<MinecartMemberChest>>();
	//Rideable Minecart owners
	public static Map<Player, List<MinecartMemberRideable>> ownerRideable = new HashMap<Player, List<MinecartMemberRideable>>();

	public static List<Sign> trainStations;
	
	//Stores the player's rail selections
	public static Map<Player, Location[]> selections = new HashMap<Player, Location[]>();

	/**
	 * <p><b>FOR TESTING PURPOSES ONLY</b></p>
	 * 
	 * <p>Use this to create new Standard TrainTbl on the map.</p>
	 * <p>This is configured so trains ALWAYS SPAWN RIGHT TO LEFT</p>
	 * <p>This means that the train furnace will spawn on the very left, to the perspective of the creator</p>
	 * 
	 * 
	 * <p><b>Standard trains have:</b></p>
	 * <ol>
	 * 	<li>1 Passenger Cart</li>
	 * 	<li>1 Storage Cart</li>
	 * 	<li>1 Furnace Cart (Power)</li>
	 * </ol>
	 * 
	 * @param trainSpawn Location of where to spawn the train
	 * @param nameOfTrain The name of the new train
	 */
	@Deprecated
	public static void newStandardTrain(Player player, String nameOfTrain, TrainType trainType, double speedLimit) {

		//Get the cardinal direction to the left of the player.
		BlockFace leftDir = TrainTools.toTheLeft(TrainTools.getDirection(player));

		Location[] locMap = selections.get(player);
		Location trainSpawn = null;
		if(locMap[0] == null && locMap[1] != null) trainSpawn = locMap[1];
		else if(locMap[0] != null) trainSpawn = locMap[0];
		if(!TrainTools.singleRailCheck(trainSpawn)) return;

		//Specifying the cart types to be created. This is simply one of each.
		List<EntityType> carts = new LinkedList<EntityType>();
		carts.add(EntityType.MINECART_FURNACE);
		carts.add(EntityType.MINECART);
		carts.add(EntityType.MINECART_CHEST);


		makeTrain(trainType, nameOfTrain, trainSpawn.getBlock(), leftDir, carts, speedLimit);
	}



	/**
	 * Use this to create new Custom TrainTbl on the map.
	 * 
	 * <p><b>Custom trains have:</b></p>
	 * <ol>
	 * 	<li>N Passenger Cart(s)</li>
	 * 	<li>N Storage Cart(s)</li>
	 * 	<li>1 Furnace Cart (Power)</li>
	 * </ol>
	 * 
	 * @param trainSpawn Location of where to spawn the train.
	 * @param nameOfTrain The name of the new train.
	 * @param numOfPassengers The amount of passenger seats available.
	 * @param numOfChests The amount of storage chests available.
	 * @param player The player spawning the train.
	 * @param speedLimit the limit of speed.
	 * @param trainType The type of train to be spawned.
	 */
	@Deprecated
	public static void newCustomTrain(Player player, String nameOfTrain, TrainType trainType, int numOfPassengers, int numOfChests, double speedLimit) {

		//Get the cardinal direction to the left of the player.
		BlockFace leftDir = TrainTools.toTheLeft(TrainTools.getDirection(player));

		Location[] locMap = selections.get(player);
		Location trainSpawn = locMap[0];		

		//Just makes sure the locations chosen are appropriate.
		if(!TrainTools.singleRailCheck(trainSpawn)) return;

		//		PathFinder pf = new PathFinder(trainSpawn.getBlock());

		//Measure the distance between the two locations, to see how big they want the train.
		int dist = TrainTools.getDistance(trainSpawn, locMap[1]);
		player.sendMessage("The distance between your two points are: " + dist);

		if(dist < (numOfPassengers + numOfChests)) {
			player.sendMessage("There is not enough room to put a train here!");
			player.sendMessage("----------------------------------------------");
			player.sendMessage("As a standard rule of thumb, count the number of carts of the train, and add 4 to it.");
			player.sendMessage("That is how much space is required.");
			return;
		}

		//Specifying the cart types to be created. This is simply one of each.
		List<EntityType> carts = new LinkedList<EntityType>();

		carts.add(EntityType.MINECART_FURNACE);
		for(int i = 0; i < numOfPassengers; i++) {
			carts.add(EntityType.MINECART);
		}
		for(int i = 0; i < numOfChests; i++) {
			carts.add(EntityType.MINECART_CHEST);
		}


		makeTrain(trainType, nameOfTrain, trainSpawn.getBlock(), leftDir, carts, speedLimit);
	}

	/**
	 *  Use this to create new Passenger TrainTbl on the map.
	 *  
	 * <p><b>Passenger trains have:</b></p>
	 * <ol>
	 * 	<li>N Passenger Cart(s)</li>
	 * 	<li>1 Furnace Cart (Power)</li>
	 * </ol>
	 *  
	 * @param trainSpawn Locaton of where to spawn the train
	 * @param nameOfTrain The name of the new train
	 * @param numOfPassengers The amount of passenger seats available
	 * @param player The player spawning the train.
	 * @param speedLimit the limit of speed.
	 * @param trainType The type of train to be spawned.
	 */
	@Deprecated
	public static void newPassengerTrain(Player player, String nameOfTrain, TrainType trainType, int numOfPassengers, double speedLimit) {

		//Get the cardinal direction to the left of the player.
		BlockFace leftDir = TrainTools.toTheLeft(TrainTools.getDirection(player));

		Location[] locMap = selections.get(player);
		Location trainSpawn = locMap[0];		

		//Just makes sure the locations chosen are appropriate.
		if(!TrainTools.singleRailCheck(trainSpawn)) return;

		//		PathFinder pf = new PathFinder(trainSpawn.getBlock());

		//Measure the distance between the two locations, to see how big they want the train.
		int dist = TrainTools.getDistance(trainSpawn, locMap[1]);
		player.sendMessage("The distance between your two points are: " + dist);


		if(dist < numOfPassengers) {
			player.sendMessage("There is not enough room to put a train here!");
			player.sendMessage("----------------------------------------------");
			player.sendMessage("As a standard rule of thumb, count the number of carts of the train, and add 4 to it.");
			player.sendMessage("That is how much space is required.");
			return;
		}

		//Specifying the cart types to be created.
		List<EntityType> carts = new LinkedList<EntityType>();

		carts.add(EntityType.MINECART_FURNACE);
		for(int i = 0; i < numOfPassengers; i++) {
			carts.add(EntityType.MINECART);
		}

		makeTrain(trainType, nameOfTrain, trainSpawn.getBlock(), leftDir, carts, speedLimit);
	}

	/**
	 * Use this to create new Storage TrainTbl on the map.
	 * 
	 * <p><b>Storage trains have:</b></p>
	 * <ol>
	 * 	<li>N Storage Cart(s)</li>
	 * 	<li>1 Furnace Cart (Power)</li>
	 * </ol>
	 * 
	 * @param trainSpawn {@link Location} of where to spawn the train.
	 * @param nameOfTrain The name of the new train.
	 * @param numOfChests The amount of storage {@link Chest}s available.
	 * @param player The player spawning the train.
	 * @param speedLimit the limit of speed.
	 * @param trainType The type of train to be spawned.
	 */
	@Deprecated
	public static void newStorageTrain(Player player, String nameOfTrain, TrainType trainType, int numOfChests, double speedLimit) {
		//Get the cardinal direction to the left of the player.
		BlockFace leftDir = TrainTools.toTheLeft(TrainTools.getDirection(player));

		Location[] locMap = selections.get(player);
		Location trainSpawn = locMap[0];		

		//Just makes sure the locations chosen are appropriate.
		if(!TrainTools.singleRailCheck(trainSpawn)) return;

		//		PathFinder pf = new PathFinder(trainSpawn.getBlock());

		//Measure the distance between the two locations, to see how big they want the train.
		int dist = TrainTools.getDistance(trainSpawn, locMap[1]);
		player.sendMessage("The distance between your two points are: " + dist);

		if(dist < numOfChests) {
			player.sendMessage("There is not enough room to put a train here!");
			player.sendMessage("----------------------------------------------");
			player.sendMessage("As a standard rule of thumb, count the number of carts of the train, and add 4 to it.");
			player.sendMessage("That is how much space is required.");
			return;
		}

		//Specifying the cart types to be created.
		List<EntityType> carts = new LinkedList<EntityType>();
		carts.add(EntityType.MINECART_FURNACE);
		for(int i = 0; i < numOfChests; i++) {
			carts.add(EntityType.MINECART_CHEST);
		}

		makeTrain(trainType, nameOfTrain, trainSpawn.getBlock(), leftDir, carts, speedLimit);
	}
	@Deprecated
	public static void newStorageTrain(Player player, BlockFace face, String nameOfTrain, TrainType trainType, int numOfChests, double speedLimit) {
		//Get the cardinal direction to the left of the player.
//		BlockFace leftDir = TrainTools.toTheLeft(TrainTools.getDirection(player));
		

		Location[] locMap = selections.get(player);
		Location trainSpawn = locMap[0];

		//Measure the distance between the two locations, to see how big they want the train.
		int dist = TrainTools.getDistance(trainSpawn, locMap[1]);
		player.sendMessage("The distance between your two points are: " + dist);

		if(dist < numOfChests) {
			player.sendMessage("There is not enough room to put a train here!");
			player.sendMessage("----------------------------------------------");
			player.sendMessage("As a standard rule of thumb, count the number of carts of the train, and add 4 to it.");
			player.sendMessage("That is how much space is required.");
			return;
		}

		//Specifying the cart types to be created.
		List<EntityType> carts = new LinkedList<EntityType>();
		carts.add(EntityType.MINECART_FURNACE);
		for(int i = 0; i < numOfChests; i++) {
			carts.add(EntityType.MINECART_CHEST);
		}

		makeTrain(trainType, nameOfTrain, trainSpawn.getBlock(), face, carts, speedLimit);
	}

	/**
	 * Creates a train with a low speed, and that is "rideable" by players.
	 * 
	 * @param nameOfTrain The name for the train
	 * @param b The {@link Block} the first minecart will spawn on.
	 * @param face The direction the train will face.
	 * @param carts The {@link EntityType} Minecarts that will represent the train.
	 */
	@Deprecated
	private static void makePublicTrain(String nameOfTrain, Block b, BlockFace face, List<EntityType> carts, double speedLimit) {
		//Add carts to a group to link them
		MinecartGroup mg = MinecartGroup.spawn(b, face, carts);
		TrainProperties tp = TrainProperties.create();

		//Properties of a Public Transportation Train
		tp.setName(nameOfTrain);
		tp.setSpeedLimit(speedLimit);
		tp.setPickup(false);
		tp.setKeepChunksLoaded(true);
		tp.playerCollision = CollisionMode.KILL;
		tp.mobCollision = CollisionMode.KILL;
		mg.setProperties(tp);

		trains.put(nameOfTrain, mg);
	}

	/**
	 * Creates a train with a low speed, and that is for transporation of goods
	 * 
	 * @param nameOfTrain The name for the train
	 * @param b The {@link Block} the first minecart will spawn on.
	 * @param face The direction the train will face.
	 * @param carts The {@link EntityType} Minecarts that will represent the train.
	 */
	@Deprecated
	private static void makeTownTrain(String nameOfTrain, Block b, BlockFace face, List<EntityType> carts) {
		//Add carts to a group to link them
		MinecartGroup mg = MinecartGroup.spawn(b, face, carts);
		TrainProperties tp = TrainProperties.create();

		//Properties of a Town Train
		tp.setName(nameOfTrain);
		tp.setSpeedLimit(0.4);
		tp.setPickup(false);
		tp.setKeepChunksLoaded(true);
		tp.setManualMovementAllowed(false);
		tp.playerCollision = CollisionMode.KILL;
		tp.mobCollision = CollisionMode.KILL;
		mg.setProperties(tp);

		trains.put(nameOfTrain, mg);
	}
	
	/**
	 * Will add a cart to the specified train (via name) with a specific owner.
	 * 
	 * @param owner The owner of the minecart.
	 * @param trainName The name of the train to add it to.
	 * @param cart The type {@link EntityType} of cart it will be.
	 * 
	 */
	public static void addCart(Player owner, String trainName, EntityType cart) {

		//Iterate through the groups already created, and find the one requested.
		MinecartGroup trainToLink = null;
		MinecartGroup[] groups = MinecartGroup.getGroups();
		for(int i = 0; i < groups.length; i++) {
			if(groups[i].getProperties().getTrainName().equalsIgnoreCase(trainName)) {
				trainToLink = groups[i];
				break;
			}
		}
		
		//If there is no train to link, fail silently.
		if(trainToLink == null) return;
		
		//If the train is moving, we don't want to add carts to it.
		if(trainToLink.isMoving()) { owner.sendMessage(ChatColor.RED + "The train is currently moving. Please wait until it has arrived at a station."); return; }
		
		//If the train size is 15 long, it is the max size, and we should not add more.
		if(trainToLink.size() == 15) { owner.sendMessage("The train is full! Please wait for the next train to come."); return; }

		//Get the last minecart in the train.
		MinecartMember<?> m1 = trainToLink.tail();

		//Small block map to determine location placement.
		Discoverable disco = new SmallBlockMap(m1.getBlock());

		//Get the OPPOSITE of the train's facing direction, that way we can get the location we need (behind the train)
		BlockFace bf = m1.getDirection().getOppositeFace();

		//Switch to determine the location of the new minecart.
		Location toSpawn = null;
		switch(bf) {
		case EAST: toSpawn = disco.getEast().getLocation(); break;
		case NORTH: toSpawn = disco.getNorth().getLocation(); break;
		case SOUTH: toSpawn = disco.getSouth().getLocation(); break;
		case WEST: toSpawn = disco.getWest().getLocation(); break;
		default: break;
		}

		//This is just setting the location of the new minecart. It is required to be an Array by TrainCarts.
		Location[] loc = {toSpawn};

		MinecartGroup mg = MinecartGroup.spawn(loc, cart);

		TrainProperties tp = trainToLink.getProperties();
		//Set the pseudo-train's properties to the train we want to link it to.
		mg.setProperties(tp);

		//Get the minecart (should be the ONLY one at this point) of this "pseudo-train"
		MinecartMember<?> m2 = mg.get(0);

		//Link the new minecart to the train.
		MinecartGroup.link(m1, m2);

		//Check if the minecart is a chest minecart.
		if(m2.getClass().isInstance(new MinecartMemberChest())) {
			//If they are not there, initialize a list and put the first cart in there.
			if(!ownerStorage.containsKey(owner)) {
				List<MinecartMemberChest>	addThis = new ArrayList<MinecartMemberChest>();
				addThis.add((MinecartMemberChest) m2);
				ownerStorage.put(owner, addThis);
			}
			//Otherwise, just add this cart to their list.
			else {
				ownerStorage.get(owner).add((MinecartMemberChest) m2);
			}
			//If not, we consider it a regular minecart, which is rideable.
		} else {
			//If they are not there, initialize a list and put the first cart in there.
			if(!ownerRideable.containsKey(owner)) {
				List<MinecartMemberRideable> addThis = new ArrayList<MinecartMemberRideable>();
				addThis.add((MinecartMemberRideable) m2);
				ownerRideable.put(owner, addThis);
			}
			//Otherwise, just add this cart to their list.
			else {
				ownerRideable.get(owner).add((MinecartMemberRideable) m2);
			}
		}

	}

	/**
	 * Removes the specified minecart from the train, by getting the inventory the player owns. 
	 * 
	 * @param trainName The train to remove the cart from.
	 * @param owner The owner of the minecart.
	 * @param cartType The type of cart to delete.
	 */
	public static void removeCart(String trainName, Player owner, EntityType cartType) {

		//Iterate through the groups already created, and find the one requested.
		MinecartGroup train = null;
		MinecartGroup[] groups = MinecartGroup.getGroups();
		for(int i = 0; i < groups.length; i++) {
			if(groups[i].getProperties().getTrainName().equalsIgnoreCase(trainName)) {
				train = groups[i];
				break;
			}
		}
		
		//Make sure the train actually exists.
		if(train == null) { owner.sendMessage("It appears this train does not exist."); return; }
		
		//If the train is moving, we don't want to add carts to it.
		if(train.isMoving()) { owner.sendMessage(ChatColor.RED + "The train is currently moving. Please wait until it has arrived at a station."); return; }

		/*
		 * This list will be occupied by either the chest owner or ride owner list. 
		 * We don't know, so we don't give it a type.
		 */
		List<?> listOf = null;
		//If its a chest minecart
		if(cartType.equals(EntityType.MINECART_CHEST)) {
			listOf = ownerStorage.get(owner);
			MinecartMemberChest toRemove = null;
			if(listOf != null) {
				int i = listOf.size() - 1;
				if(i == -1) return;
				toRemove = (MinecartMemberChest) listOf.get(i);
			}
			if(toRemove == null) return;
			
			/*
			 * Iterate through the minecart members in each train.
			 * As luck would have it, trains are iterable, because they are extending ArrayList<MinecartMember<?>>.
			 */
			for(MinecartMember<?> cart : train) {
				try {
					MinecartMemberChest chestCart = (MinecartMemberChest) cart;

					if(chestCart == toRemove) {
						ownerStorage.get(owner).remove(chestCart);
						train.removeSilent(cart);
						cart.getGroup().destroy();
						train.respawn();
						break;
					}
				} catch (ClassCastException e) {}
			}
		}
		//If its a regular minecart
		else if(cartType.equals(EntityType.MINECART)) {
			listOf = ownerRideable.get(owner);
			MinecartMemberRideable toRemove = null;
			if(listOf != null) {
				int i = listOf.size() - 1;
				if(i == -1) return;
				toRemove = (MinecartMemberRideable) listOf.get(i);
			}
			if(toRemove == null) return;

			for(MinecartMember<?> cart : train) {
				try {
					MinecartMemberRideable rideCart = (MinecartMemberRideable) cart;

					if(rideCart == toRemove) {
						ownerRideable.get(owner).remove(rideCart);
						train.removeSilent(cart);
						cart.getGroup().destroy();
						train.respawn();
						break;
					}
				} catch (ClassCastException e) {}
			}
		}
	}

	/**
	 * Finds the owner of the inventory through a backwards process.
	 * 
	 * @param inv The inventory in question.
	 * @return The owner of the inventory.
	 */
	public static Player findOwnerByInv(Inventory inv) {
		for(Player p : ownerStorage.keySet()) {
			for(List<MinecartMemberChest> mmcList : ownerStorage.values()) {
				for(MinecartMemberChest mmc : mmcList) {
					if(mmc.getEntity().getInventory() == inv) {
						return p;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets the chest minecart list associated with the player.
	 * 
	 * @param p The player.
	 * @return The {@code List<MinecartMemberChest>}
	 */
	public static List<MinecartMemberChest> getInventoryList(Player p) {
		return ownerStorage.get(p);
	}

	/**
	 * Gets the rideable minecart list associated with the player.
	 * 
	 * @param p The player.
	 * @return The {@code List<MinecartMemberRideable>}
	 */
	public static List<MinecartMemberRideable> getRideList(Player p) {
		return ownerRideable.get(p);
	}

	public static boolean hasInvList(Player p) {
		return ownerStorage.containsKey(p);
	}

	public static boolean hasRideList(Player p) {
		return ownerRideable.containsKey(p);
	}
	
	public static boolean hasCarts(Player p) {
		List<?> cartList1 = ownerStorage.get(p), cartList2 = ownerRideable.get(p);
		if((cartList1.size() + cartList2.size()) == 0) return false;
		else return true;
	}
	
	public static boolean hasRideCarts(Player p) {
		List<?> rides = ownerRideable.get(p);
		if(rides != null) {
			if(rides.size() == 0) {
				return false;
			}
			else return true;
		}
		else return false;
	}
	
	public static boolean hasStoreCarts(Player p) {
		List<?> rides = ownerStorage.get(p);
		if(rides != null) {
			if(rides.size() == 0) {
				return false;
			}
			else return true;
		}
		else return false;
	}

	/**
	 * Determines what type of train it will be. Either public or private at the moment.
	 * 
	 * @param trainType The {@link TrainType} type of train it will be. either {@link TrainType#PUBLIC} or {@link TrainType#PRIVATE} for now.
	 * @param trainName The name of the train to be created.
	 * @param b The {@link Block} the train will start on.
	 * @param face the {@link BlockFace} the train will be facing.
	 * @param carts The type of carts in this train.
	 * @param speedLimit How fast the cart is capable of going.
	 */
	private static void makeTrain(TrainType trainType, String trainName, Block b, BlockFace face, List<EntityType> carts, double speedLimit) {
		switch(trainType) {
		case PRIVATE: break;
		case PUBLIC: makePublicTrain(trainName, b, face, carts, speedLimit);  break;
		case TOWN: makeTownTrain(trainName, b, face, carts);	break;
		case TRANSPORT: break;
		default: break;
		}
	}
}
