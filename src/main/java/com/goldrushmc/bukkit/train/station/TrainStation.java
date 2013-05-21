package com.goldrushmc.bukkit.train.station;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberChest;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberRideable;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.goldrushmc.bukkit.chunkpersist.ChunkLoaderLis;
import com.goldrushmc.bukkit.db.TrainStationLocationTbl;
import com.goldrushmc.bukkit.db.TrainStationTbl;
import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.defaults.DBAccess;
import com.goldrushmc.bukkit.defaults.DBTrainsAccessible;
import com.goldrushmc.bukkit.train.SmallBlockMap;
import com.goldrushmc.bukkit.train.exceptions.MissingSignException;
import com.goldrushmc.bukkit.train.listeners.TrainStationLis;
import com.goldrushmc.bukkit.train.signs.ISignLogic;
import com.goldrushmc.bukkit.train.signs.SignLogic;
import com.goldrushmc.bukkit.train.signs.SignType;
import com.goldrushmc.bukkit.train.util.TrainTools;

/**
 * Will control the functionality behind train stations.
 *
 * @author Diremonsoon 
 * 
 */
public abstract class TrainStation extends BlockFinder{

	//For tracking of the stations.
	protected static List<TrainStation> trainStations = new ArrayList<TrainStation>();
	public static final Material defaultStop = Material.BEDROCK;
	public static DBTrainsAccessible db;

	protected String stationName;
	protected volatile MinecartGroup departingTrain;
	protected ISignLogic signs;
	protected final BlockFace direction;
	protected volatile List<Player> visitors = new ArrayList<Player>();
	protected List<HumanEntity> workers = new ArrayList<HumanEntity>();
	protected final List<Block> trainArea;
	protected volatile List<MinecartGroup> trains = new ArrayList<MinecartGroup>();
	protected final List<Block> rails;
	protected final List<Chunk> chunks;

	/**
	 * We require the JavaPlugin because this class must be able to access the database.
	 * This is the standard constructor, with the default stop block material..
	 * 
	 * @param plugin
	 * @param stationName
	 * @param corners
	 * @param world
	 * @throws Exception 
	 */
	public TrainStation(final JavaPlugin plugin, final String stationName, final List<Location> markers, final World world) throws Exception {
		super(world, markers);
		if(db == null) db = new DBAccess(plugin);
		this.stationName = stationName;
		this.trainArea = generateTrainArea();
		this.rails = findRails();
		this.signs = generateSignLogic();
		//Added for chunk loading/unloading purposes.
		List<Chunk> chunks = new ArrayList<Chunk>();
		for(Block b : this.trainArea) {
			if(chunks.contains(b)) continue;
			chunks.add(b.getChunk());
		}
		this.chunks = chunks;
		Sign dir = this.signs.getSign(SignType.TRAIN_STATION_DIRECTION);
		if(dir == null) throw new MissingSignException(this.signs);
		BlockFace tempDir = null;

		if(dir != null) tempDir = TrainTools.getDirection(dir.getLine(2));

		if(tempDir != null) {
			this.direction = tempDir;	
		}
		else {
			this.direction = BlockFace.SELF;
		}
		//		findWorkers();
		//Add to the list of stations for both the listener and static class instance! IMPORTANT
		trainStations.add(this);
		ChunkLoaderLis.addStation(this);
		TrainStationLis.addStation(this);
	}

	/**
	 * We require the JavaPlugin because this class must be able to access the database.
	 * This is the standard constructor with a custom stop block material.
	 * 
	 * @param plugin
	 * @param stationName
	 * @param corners
	 * @param world
	 * @param stopMat
	 * @throws Exception 
	 */
	public TrainStation(final JavaPlugin plugin, final String stationName, final List<Location> markers, final World world, Material stopMat) throws Exception {
		super(world, markers);
		if(db == null) db = new DBAccess(plugin);
		this.stationName = stationName;
		this.trainArea = generateTrainArea();
		this.rails = findRails();
		this.signs = generateSignLogic();
		//Added for chunk loading/unloading purposes.
		List<Chunk> chunks = new ArrayList<Chunk>();
		for(Block b : this.trainArea) {
			if(chunks.contains(b)) continue;
			chunks.add(b.getChunk());
		}
		this.chunks = chunks;
		Sign dir = this.signs.getSign(SignType.TRAIN_STATION_DIRECTION);
		BlockFace tempDir = null;
		if(dir == null) throw new MissingSignException(this.signs);
		if(dir != null) {
			tempDir = TrainTools.getDirection(dir.getLine(2));
		}
		if(tempDir != null) {
			this.direction = tempDir;	
		}
		else {
			this.direction = BlockFace.SELF;
		}
		//		findWorkers();

		//Add to the list of stations for both the listener and static class instance! IMPORTANT
		trainStations.add(this);
		ChunkLoaderLis.addStation(this);
		TrainStationLis.addStation(this);
	}

	public abstract void createTransport();

	/**
	 * Adds the train station to the database, in case of a server wide crash.
	 */
	public void addToDB(List<Location> coords) {
		TrainStationTbl station = new TrainStationTbl();
		station.setStationName(stationName);
		Set<TrainStationLocationTbl> corners = new HashSet<TrainStationLocationTbl>();
		List<Location> locs = coords;
		for(int i = 0; i < 2; i++) {
			TrainStationLocationTbl corner = new TrainStationLocationTbl();
			corner.setStation(station);
			corner.setX(locs.get(i).getBlockX());
			corner.setY(locs.get(i).getBlockY());
			corner.setZ(locs.get(i).getBlockZ());
			corners.add(corner);
		}
		db.getDB().save(corners);
		station.setCorners(corners);
		db.getDB().save(station);
	}

	/**
	 * Provides a standard way to sell carts.
	 * 
	 * @param owner
	 * @param type
	 */
	public abstract void sellCart(Player owner, EntityType type);

	/**
	 * The default way for players to buy carts.
	 * 
	 * @param owner
	 * @param type
	 */
	public abstract void buyCart(Player owner, EntityType type);

	/**
	 * A standard way to update the departure time for a single departing train, in minutes.
	 * 
	 * @param time
	 */
	public void updateDepartureTime(MinecartGroup train, long time) {
		//If there is no departing train, set the time to N/A
		if(train == null) this.signs.getSign(SignType.TRAIN_STATION_TIME).setLine(2, "N/A");
		//If there is a train, set the minutes to depart.
		else this.signs.getSign(SignType.TRAIN_STATION_TIME).setLine(2, time + " minutes");
		//Update the sign regardless, because it needs to change.
		this.signs.getSign(SignType.TRAIN_STATION_TIME).update();
	}

	/**
	 * A standard way to update the carts available for a single departing train.
	 * 
	 * @param type
	 */
	public void updateCartsAvailable(MinecartGroup train, EntityType type) {
		if(train == null) {
			this.signs.getSign(SignType.TRAIN_STATION_CART_COUNT).setLine(2, "0");
			this.signs.getSign(SignType.TRAIN_STATION_CART_COUNT).update();
			return;
		}
		train.size(type);
		int countAvail = 0;
		for(MinecartMember<?> cart : train) {
			if(cart instanceof MinecartMemberFurnace) continue;
			switch(type) {
			case MINECART: if(cart instanceof MinecartMemberRideable) {
				if(!cart.getProperties().hasOwners()) countAvail++;
				break;
			}
			case MINECART_CHEST: if(cart instanceof MinecartMemberChest) {
				if(!cart.getProperties().hasOwners()) countAvail++;
				break;
			}
			default: break;
			}
		}
		this.signs.getSign(SignType.TRAIN_STATION_CART_COUNT).setLine(2, String.valueOf(countAvail));
		this.signs.getSign(SignType.TRAIN_STATION_CART_COUNT).update();
	}

	/**
	 * The standard train creation method. This is an optional way to create a train with one furnace and one chest cart.
	 * 
	 * @param stop The {@link Block} to spawn the train on.
	 */
	public void createBuyableTrain(Block stop) {

		if(this.trains == null) this.trains = new ArrayList<MinecartGroup>();
		int trainNum = this.trains.size() + 1;

		List<EntityType> carts = new ArrayList<EntityType>();
		carts.add(EntityType.MINECART_CHEST);
		carts.add(EntityType.MINECART_FURNACE);
		//Should make the furnace spawn right on top of the stop block.
		MinecartGroup train = MinecartGroup.spawn(stop, this.direction.getOppositeFace(), carts);

		TrainProperties tp = train.getProperties();
		tp.setName(stationName + "_" + trainNum);
		tp.setColliding(true);
		tp.setSpeedLimit(0.4);
		tp.setPublic(false);
		tp.setManualMovementAllowed(false);
		tp.setKeepChunksLoaded(true);
		tp.setPickup(false);
		train.setProperties(tp);

		this.addTrain(train);
		this.findNextDeparture();
		this.changeSignLogic(train.getProperties().getTrainName());
	}


	/**
	 * A method which gets 10 blocks below and 50 blocks above the surface Y coordinate.
	 * @return
	 */
	public List<Block> generateTrainArea() {
		List<Block> full = new ArrayList<Block>();
		for(Block b : this.surface) {
			//Gets 2 blocks below, just in case the train dips.
			//Gets 8 blocks above, in case of incline.
			for(int i = (b.getY() - 10); i < (b.getY() + 50); i++) {
				full.add(world.getBlockAt(b.getX(), i, b.getZ()));
			}
		}
		return full;
	}

	/**
	 * The idea is that we take the buy/sell signs and replace them with people.
	 * How we do that, and which signs turn into them, is up to each station.
	 * 
	 */
	protected abstract void createWorkers();
	
	public void addVisitor(Player visitor) {
		this.visitors.add(visitor);
	}

	public void removeVisitor(Player visitor) {
		this.visitors.remove(visitor);
	}

	protected ISignLogic generateSignLogic() {
		return new SignLogic(this.trainArea);
	}

	public void findStillTrains() {
		MinecartGroup[] trains = MinecartGroup.getGroups();
		for(int i = 0; i < trains.length; i++) {
			if(!trains[i].isMoving()) {
				//At least the front of the train must be WITHIN the station.
				Block ground = trains[i].get(0).getGroundBlock();
				for(Block b : this.trainArea) {
					//If the train is within the station grounds, accept it.
					if(b.equals(ground)) {
						this.trains.add(trains[i]);
					}
				}
			}
		}
	}

	/**
	 * Spawns a cart onto the train scheduled for departure
	 * <p>
	 * DEFAULT IMPLEMENTATION.
	 * 
	 * @param type
	 * @param owner
	 */
	public void addCart(EntityType type, Player owner) {

		//Check if the departing train does not exist. this may happen, for a brief period, between the departing train leaving and the arriving train arriving.
		if(this.departingTrain == null) { owner.sendMessage("There are currently no trains to buy carts for."); return; }

		//Get train name, in case the departing train forgets!
		String trainName = this.departingTrain.getProperties().getTrainName();
		//		int trainSize = this.departingTrain.size() - 1;
		Block toSpawn = null;
		BlockFace dirToLook = this.direction.getOppositeFace();
		//Make sure that we are on the right end of the train, to spawn.
		MinecartMember<?> toJoinTo = null;
		if(this.departingTrain.get(0) instanceof MinecartMemberFurnace) {
			toJoinTo = this.departingTrain.get(this.departingTrain.size() - 1);
		}
		else {
			toJoinTo = this.departingTrain.get(0);
		}
		//Set the block map to the correct minecart member's block.
		SmallBlockMap sbm = new SmallBlockMap(toJoinTo.getBlock());
		if(sbm.isRail(sbm.getBlockAt(dirToLook))) {
			toSpawn = sbm.getBlockAt(dirToLook);
		}
		//If there is no room, do not spawn additional carts.
		if(toSpawn == null) { owner.sendMessage("There is not enough room to spawn additonal carts."); return; }

		Location fixed = toSpawn.getLocation();
		switch(this.direction) {
		case NORTH: fixed.setZ(fixed.getZ() + 0.75); break;
		case SOUTH: fixed.setZ(fixed.getZ() - 0.75); break;
		case EAST: fixed.setX(fixed.getX() - 0.75); break;
		case WEST: fixed.setX(fixed.getX() + 0.75); break;
		default: break;
		}

		//Spawn the cart and join it to the train.
		MinecartMember<?> toJoin = MinecartMemberStore.spawn(fixed, type);
		//Set the new minecarts group properties to the same as the departing train.
		toJoin.getGroup().setProperties(this.departingTrain.getProperties());
		//Set the owner (hoping this works) to the person who bought the cart.
		toJoin.getProperties().setOwner(owner.getName().toLowerCase());
		MinecartGroup.link(toJoin, toJoinTo);
		//Re-set the name of the departing train, after linking.
		this.departingTrain.getProperties().setName(trainName);

		//Send a message saying it has been done.
		if(type.equals(EntityType.MINECART)) owner.sendMessage("You bought a passenger cart");
		else if(type.equals(EntityType.MINECART_CHEST)) owner.sendMessage("You bought a storage cart");
	}


	/**
	 * Removes a cart from the departing train.
	 * 
	 * @param type
	 * @param remover
	 */
	public void removeCart(EntityType type, Player remover) {
		if(this.departingTrain == null) { remover.sendMessage("There is no train in the station."); return; }
		MinecartGroup train = this.departingTrain;
		for(MinecartMember<?> cart : train) {
			CartProperties cartP = cart.getProperties();
			if(cartP.getOwners().contains(remover.getName().toLowerCase())) {
				train.removeSilent(cart); 
				cart.getGroup().destroy();
				return;
			}
		}
		remover.sendMessage("You have no train carts to remove.");
	}

	/**
	 * Finds the next train queued for departure. Its front cart (furnace cart) will be just above the stop block.
	 * 
	 * @return
	 */
	public abstract MinecartGroup findNextDeparture();

	public MinecartGroup getDepartingTrain() {
		return departingTrain;
	}

	public void setDepartingTrain(MinecartGroup train) {
		this.departingTrain = train;
	}

	/**
	 * Intended for departing the current queued train, and moving all of the others (if any) closer to the stop block.
	 * 
	 * @return true if there is a train to depart.
	 */
	public abstract boolean pushQueue();
	/**
	 * Changes the signs to reflect the buying and selling of carts for the specified train.
	 * 
	 * @param trainName
	 */
	public abstract void changeSignLogic(String trainName);

	/**
	 * Gets the block that trains will stop on, specified by a specific material.
	 * The material must be a unique one, <u>only used once</u> in the whole station.
	 * 
	 * @param m
	 * @return
	 */
	public abstract List<Block> findStopBlocks(Material m);

	public abstract List<Block> getStopBlocks(); 

	/**
	 * This finds the rails which are within the train station.
	 * <p>
	 * This DOES NOT SHOW PATHWAYS. That is to be determined for each subclass.
	 * 
	 * @return
	 */
	public List<Block> findRails() {
		List<Block> rails = new ArrayList<Block>();
		for(Block b : this.selectedArea) {
			if(TrainTools.isRail(b)) {
				rails.add(b);
			}
		}
		return rails;
	}

	//TODO Various Getters and Setters for the Train Station class.

	public void addTrain(MinecartGroup train) {
		this.trains.add(train);
	}
	public void removeTrain(MinecartGroup train) {
		this.trains.remove(train);
	}

	public List<Block> getTrainArea() { return trainArea; }

	/**
	 * Gets all of the existing train stations.
	 * 
	 * @return
	 */
	public static List<TrainStation> getTrainStations() {return trainStations;}

	/**
	 * Could potentially return null, if no train stations exist.
	 * 
	 * @return
	 */
	public static DBTrainsAccessible getDb() {	return db;}

	public String getStationName() {return stationName;}

	public void setStationName(String stationName) {this.stationName = stationName;}

	public ISignLogic getSigns() {return signs;}

	public BlockFace getDirection() { return direction;}

	public List<Player> getVisitors() {	return visitors;}	

	public List<HumanEntity> getWorkers() {	return workers;}

	public List<MinecartGroup> getTrains() {return trains;}

	public List<Block> getRails() {return rails;}

	/**
	 * @return the chunks
	 */
	public List<Chunk> getChunks() {
		return chunks;
	}
}
