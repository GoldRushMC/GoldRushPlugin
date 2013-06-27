package com.goldrushmc.bukkit.trainstation;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberChest;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberRideable;
import com.bergerkiller.bukkit.tc.events.MemberBlockChangeEvent;
import com.goldrushmc.bukkit.db.access.DBStationsAccess;
import com.goldrushmc.bukkit.db.access.IStationAccessible;
import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.town.Town;
import com.goldrushmc.bukkit.trainstation.event.StationSignEvent;
import com.goldrushmc.bukkit.trainstation.exceptions.MarkerNumberException;
import com.goldrushmc.bukkit.trainstation.exceptions.MissingSignException;
import com.goldrushmc.bukkit.trainstation.signs.ISignLogic;
import com.goldrushmc.bukkit.trainstation.signs.SignType;
import com.goldrushmc.bukkit.trainstation.signs.StationSignLogic;
import com.goldrushmc.bukkit.trainstation.util.TrainTools;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An Abstract Class which sets up the framework for the trainstation subclasses.
 * <p>
 * Contains some default implementations, and extends the BlockFinder class.
 *
 * @author Diremonsoon
 *
 */
public abstract class TrainStation extends BlockFinder{

    //For tracking of the stations.
    protected static List<TrainStation> trainStations = new ArrayList<>();

    public static final Material defaultStop = Material.BEDROCK;
    public static IStationAccessible db;

    protected String stationName;
    protected volatile List<MinecartGroup> departingTrains = new ArrayList<>();
    protected volatile Map<MinecartGroup, Boolean> hasStopped = new HashMap<>();
    protected ISignLogic signs;
    protected List<BlockFace> directions;
    protected volatile List<Player> visitors = new ArrayList<>();
    protected List<NPC> workers = new ArrayList<>();
    protected final List<Block> trainArea;
    protected final List<Block> trainStationBlocks;
    protected volatile List<MinecartGroup> trains = new ArrayList<>();
    protected final List<Block> rails;
    protected Town town;

    /**
     * We require the JavaPlugin because this class must be able to access the database.
     * This is the standard constructor, with the default stop block material..
     * <p>
     * Make sure that {@link TrainStation#add()} and {@link TrainStation#createWorkers()} is handled.
     *
     * @param plugin
     * @param stationName
     * @param world
     * @throws Exception
     */
    public TrainStation(final JavaPlugin plugin, final String stationName, final List<Location> markers, final World world) throws MissingSignException, MarkerNumberException {
        super(world, markers, plugin);
        if(db == null) db = new DBStationsAccess(plugin);
        this.stationName = stationName;
        this.trainArea = generateTrainArea();
        this.trainStationBlocks = this.findNonAirBlocks();
        this.rails = findRails();
        this.signs = generateSignLogic();
//		List<Sign> dir = this.signs.getSigns(SignType.TRAIN_STATION_DIRECTION);
//		if(dir == null) throw new MissingSignException(this.signs);

//		if(!dir.isEmpty()) {
//			for(Sign s : dir) {
//				tempDir.add(TrainTools.getDirection(s.getLine(2)));	
//			}
//		}
    }

    /**
     * We require the JavaPlugin because this class must be able to access the database.
     * This is the standard constructor with a custom stop block material.
     * <p>
     * Make sure that {@link TrainStation#add()} and {@link TrainStation#createWorkers()} is handled.
     *
     * @param plugin
     * @param stationName
     * @param world
     * @param stopMat
     * @throws Exception
     */
    public TrainStation(final JavaPlugin plugin, final String stationName, final List<Location> markers, final World world, Material stopMat) throws MissingSignException, MarkerNumberException {
        super(world, markers, plugin);
        if(db == null) db = new DBStationsAccess(plugin);
        this.stationName = stationName;
        this.trainArea = generateTrainArea();
        this.trainStationBlocks = this.findNonAirBlocks();
        this.rails = findRails();
        this.signs = generateSignLogic();
//		List<Sign> dir = this.signs.getSigns(SignType.TRAIN_STATION_DIRECTION);
//		if(dir == null) throw new MissingSignException(this.signs);
//		List<BlockFace> tempDir = new ArrayList<BlockFace>();

//		if(!dir.isEmpty()) {
//			for(Sign s : dir) {
//				tempDir.add(TrainTools.getDirection(s.getLine(2)));	
//			}
//		}

//		if(tempDir.isEmpty()) {
//			this.directions = tempDir;
//		}
//		else {
//			this.directions = new ArrayList<BlockFace>();
//		}
    }

    /**
     * Shows what type of types class this is, using the enum {@link com.goldrushmc.bukkit.trainstation.StationType}
     * @return
     */
    public abstract StationType getType();

    @Override
    public void add() {
        //Add to the list of stations for both the listener and static class instance! IMPORTANT
        trainStations.add(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void remove() {

        //Unregister handlers for events.
        ChunkUnloadEvent.getHandlerList().unregister(this);
        PlayerMoveEvent.getHandlerList().unregister(this);
        MemberBlockChangeEvent.getHandlerList().unregister(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
        //BlockFinder Class stuff.
        BlockPlaceEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockDamageEvent.getHandlerList().unregister(this);

        //Remove from the DB.
        removeFromDB();

        //Remove ALL NPC's in the area.
        for(NPC npc : this.workers) CitizensAPI.getNPCRegistry().deregister(npc);

        //Clear sign logic and change signs to air.
        this.signs = null;
        for(Block b : this.trainArea) {
            if(b.getState() instanceof Sign) {
                b.setType(Material.AIR);
            }
        }

        //Clear trains.
        this.departingTrains = null;
        this.trains = null;

        //Clear visitors
        this.visitors = null;

        //Clear name
        this.stationName = null;

        //Remove from the list
        trainStations.remove(this);
    }


    @Override
    public List<Block> findNonAirBlocks() {
        List<Block> nonAir = new ArrayList<>();
        for(Block b : this.trainArea) {
            if(!b.getType().equals(Material.AIR)) {
                nonAir.add(b);
            }
        }
        return nonAir;
    }

    /**
     * Adds the trainstation types to the database, in case of a server wide crash.
     */
    public abstract void addToDB(List<Location> coords);

    public abstract void removeFromDB();


    /**
     * Provides a standard way to sell carts.
     *
     * @param owner
     * @param type
     */
    public abstract boolean sellCart(Player owner, EntityType type);

    /**
     * The default way for players to buy carts.
     *
     * @param owner
     * @param type
     */
    public abstract boolean buyCart(Player owner, EntityType type);

    /**
     * A standard way to update the departure time for a single departing trainstation, in minutes.
     *
     * @param time
     */
    public void updateDepartureTime(long time) {
        for(Sign sign : this.signs.getSigns(SignType.TRAIN_STATION_TIME)) {
            sign.setLine(2, time + " minutes");
            sign.update();
        }
    }

    /**
     * A standard way to update the carts available for a single departing trainstation.
     *
     * @param type
     */
    public void updateCartsAvailable(MinecartGroup train, EntityType type) {
        if(train == null) {
            for(Sign sign : this.signs.getSigns(SignType.TRAIN_STATION_CART_COUNT)) {
                sign.setLine(2, "NO TRAIN");
                sign.update();
            }
            return;
        }
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
        for(Sign sign : this.signs.getSigns(SignType.TRAIN_STATION_CART_COUNT)) {
            sign.setLine(2, String.valueOf(countAvail));
            sign.update();
        }
    }

//	/**
//	 * The standard trainstation creation method. This is an optional way to create a trainstation with one furnace and one chest cart.
//	 * 
//	 * @param stop The {@link Block} to spawn the trainstation on.
//	 */
//	public void createBuyableTrain(Block stop) {
//
//		if(this.trains == null) this.trains = new ArrayList<MinecartGroup>();
//		int trainNum = this.trains.size() + 1;
//
//		List<EntityType> carts = new ArrayList<EntityType>();
//		carts.add(EntityType.MINECART_CHEST);
//		carts.add(EntityType.MINECART_FURNACE);
//		//Should make the furnace spawn right on top of the stop block.
//		MinecartGroup trainstation = MinecartGroup.spawn(stop, this.direction.getOppositeFace(), carts);
//
//		TrainProperties tp = trainstation.getProperties();
//		tp.setName(stationName + "_" + trainNum);
//		tp.setColliding(true);
//		tp.setSpeedLimit(0.4);
//		tp.setPublic(false);
//		tp.setManualMovementAllowed(false);
//		tp.setKeepChunksLoaded(true);
//		tp.setPickup(false);
//		trainstation.setProperties(tp);
//
//		this.addTrain(trainstation);
//		if(this.departingTrain == null) this.departingTrain = trainstation;
//		this.changeSignLogic(trainstation.getProperties().getTrainName());
//	}


    /**
     * A method which gets 10 blocks below and 50 blocks above the surface Y coordinate.
     * @return
     */
    public List<Block> generateTrainArea() {
        List<Block> full = new ArrayList<>();
        for(Block b : this.surface) {
            //Gets 2 blocks below, just in case the trainstation dips.
            //Gets 8 blocks above, in case of incline.
            for(int i = (b.getY() - 10); i < (b.getY() + 50); i++) {
                full.add(world.getBlockAt(b.getX(), i, b.getZ()));
            }
        }
        return full;
    }

    /**
     * The idea is that we take the buy/sell signs and replace them with people.
     * How we do that, and which signs turn into them, is up to each types.
     *
     */
    protected abstract void createWorkers();

    public void addTrainStopped(MinecartGroup train) {
        this.hasStopped.put(train, true);
    }

    public void addTrainSlow(MinecartGroup train) {
        this.hasStopped.put(train, false);
    }

    public boolean hasStopped(MinecartGroup train) {
        if(this.hasStopped.containsKey(train)) {
            return this.hasStopped.get(train);
        }
        else return false;
    }

    public void removeTrainStopped(MinecartGroup train) {
        this.hasStopped.remove(train);
    }

    public void addVisitor(Player visitor) {
        this.visitors.add(visitor);
    }

    public void removeVisitor(Player visitor) {
        this.visitors.remove(visitor);
    }

    protected ISignLogic generateSignLogic() throws MissingSignException{
        return new StationSignLogic(this.trainArea);
    }

    /**
     * Finds pre-existing trains within the types.
     * @return
     */
    public abstract boolean findStillTrains();

	/*
	 * Spawns a cart onto the trainstation scheduled for departure
	 * <p>
	 * DEFAULT IMPLEMENTATION.
	 * 
	 * @param type
	 * @param owner
	 */
//	public abstract void addCart(EntityType type, Player owner);
//	public void addCart(EntityType type, Player owner) {

//		//Check if the departing trainstation does not exist. this may happen, for a brief period, between the departing trainstation leaving and the arriving trainstation arriving.
//		if(this.departingTrains == null) { owner.sendMessage("There are currently no trains to buy carts for."); return; }
//
//		//Get trainstation name, in case the departing trainstation forgets!
//		String trainName = this.departingTrains.getProperties().getTrainName();
//		//		int trainSize = this.departingTrain.size() - 1;
//		Block toSpawn = null;
////		BlockFace dirToLook = this.direction.getOppositeFace();
//		BlockFace dirToLook = null;
//		//Make sure that we are on the right end of the trainstation, to spawn.
//		MinecartMember<?> toJoinTo = null;
//		if(this.departingTrains.get(0) instanceof MinecartMemberFurnace) {
//			toJoinTo = this.departingTrains.get(this.departingTrains.size() - 1);
//		}
//		else {
//			toJoinTo = this.departingTrains.get(0);
//		}
//		//Set the block map to the correct minecart member's block.
//		SmallBlockMap sbm = new SmallBlockMap(toJoinTo.getBlock());
//		if(sbm.isRail(sbm.getBlockAt(dirToLook))) {
//			toSpawn = sbm.getBlockAt(dirToLook);
//		}
//		//If there is no room, do not spawn additional carts.
//		if(toSpawn == null) { owner.sendMessage("There is not enough room to spawn additonal carts."); return; }
//
//		Location fixed = toSpawn.getLocation();
////		switch(this.direction) {
////		case NORTH: fixed.setZ(fixed.getZ() + 0.75); break;
////		case SOUTH: fixed.setZ(fixed.getZ() - 0.75); break;
////		case EAST: fixed.setX(fixed.getX() - 0.75); break;
////		case WEST: fixed.setX(fixed.getX() + 0.75); break;
////		default: break;
////		}
//
//		//Spawn the cart and join it to the trainstation.
//		MinecartMember<?> toJoin = MinecartMemberStore.spawn(fixed, type);
//		//Set the new minecarts group properties to the same as the departing trainstation.
//		toJoin.getGroup().setProperties(this.departingTrains.getProperties());
//		//Set the owner (hoping this works) to the person who bought the cart.
//		toJoin.getProperties().setOwner(owner.getName().toLowerCase());
//		MinecartGroup.link(toJoin, toJoinTo);
//		//Re-set the name of the departing trainstation, after linking.
//		this.departingTrains.getProperties().setName(trainName);
//
//		//Send a message saying it has been done.
//		if(type.equals(EntityType.MINECART)) owner.sendMessage("You bought a passenger cart");
//		else if(type.equals(EntityType.MINECART_CHEST)) owner.sendMessage("You bought a storage cart");
//	}


	/*
	 * Removes a cart from the departing trainstation.
	 * 
	 * @param type
	 * @param remover
	 */
//	public void removeCart(EntityType type, Player remover) {
//		if(this.departingTrains == null) { remover.sendMessage("There is no trainstation in the types."); return; }
//		MinecartGroup trainstation = this.departingTrains;
//		for(MinecartMember<?> cart : trainstation) {
//			CartProperties cartP = cart.getProperties();
//			if(cartP.getOwners().contains(remover.getName().toLowerCase())) {
//				trainstation.removeSilent(cart);
//				cart.getGroup().destroy();
//				return;
//			}
//		}
//		remover.sendMessage("You have no trainstation carts to remove.");
//	}

    /**
     * returns a list of trains (may only contain one trainstation if the types is designed to only have one.
     *
     * @return
     */
    public abstract List<MinecartGroup> getDepartingTrains();

    /**
     * Shows whether or not the types's departing trainstation has any carts left to sell.
     *
     * @return
     */
    public abstract boolean hasCartsToSell();

    /**
     * Shows whether or not a types has a trainstation to depart.
     *
     * @return
     */
    public abstract boolean hasDepartingTrain();

    /**
     * Adds a departing trainstation to the list. Only truly applies if the types has more than one stop location.
     *
     * @param train
     */
    public abstract void addDepartingTrain(MinecartGroup train);

    /**
     * Intended for departing the current queued trainstation, and moving all of the others (if any) closer to the stop block.
     *
     * @return true if there is a trainstation to depart.
     */
    public abstract boolean pushQueue();
    /**
     * Changes the signs to reflect the buying and selling of carts for the specified trainstation.
     *
     * @param trainName
     */
    public abstract void changeSignLogic(String trainName);

    /**
     * Gets the block that trains will stop on, specified by a specific material.
     * The material must be a unique one, <u>only used once</u> in the whole types.
     *
     * @param m
     * @return
     */
    public abstract List<Block> findStopBlocks(Material m);

    /**
     * Gets the list of stop blocks for a trainstation types.
     *
     * @return
     */
    public abstract List<Block> getStopBlocks();

    /**
     * This is a default way to find the rails which are within the trainstation types.
     * <p>
     * This DOES NOT SHOW PATHWAYS. That is to be determined for each subclass.
     *
     * @return
     */
    public List<Block> findRails() {
        List<Block> rails = new ArrayList<>();
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

    /**
     * Returns the area 50 blocks above, and 10 blocks below, the trainstation types minimum y value.
     *
     * @return A {@code List<Block>}
     */
    public List<Block> getTrainArea() { return trainArea; }

    public static TrainStation getTrainStationAt(Location loc) {
        for(TrainStation station : trainStations) {
            if(station.getTrainArea().contains(loc.getBlock())) {
                return station;
            }
        }
        return null;
    }

    /**
     * Gets all of the existing trainstation stations.
     *
     * @return
     */
    public static List<TrainStation> getTrainStations() {return trainStations;}

    /**
     * Could potentially return null, if no trainstation stations exist.
     *
     * @return
     */
    public static IStationAccessible getDb() {	return db;}

    public String getStationName() {return stationName;}

    public void setStationName(String stationName) {this.stationName = stationName;}

    public ISignLogic getSigns() {return signs;}

    public BlockFace getDirection() { return directions.get(0);}

    public List<Player> getVisitors() {	return visitors;}

    public List<NPC> getWorkers() {	return workers;}

    public List<MinecartGroup> getTrains() {return trains;}

    public List<Block> getRails() {return rails;}

    public Town getTown() { return town; }

    public void setTown(Town town) { this.town = town; }

    /**
     * @return the chunks
     */
    public List<Chunk> getChunks() {return chunks;}


    //TODO Listener Stuff

    @EventHandler(priority = EventPriority.MONITOR)
    public abstract void onPlayerMove(PlayerMoveEvent event);

    /**
     * Handles when a trainstation moves onto Train Station territory.
     * @param event
     */
    @EventHandler
    public abstract void onTrainMove(MemberBlockChangeEvent event);

    /**
     * Does work with sign clicking events.
     *
     * @param event The {@link Sign} click.
     */
    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {

        //We don't care about air blocks!
        if(event.getClickedBlock() == null) return;

        Block b = event.getClickedBlock();
        BlockState bs = b.getState();

        //Player can only right click to get this event to work. Otherwise we fail silently.
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        //We don't care if it isn't a sign.
        if(!(b.getType().equals(Material.SIGN) || b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN))) return;
        //Make sure the player has permission to use signs at all.
        if(!event.getPlayer().hasPermission("goldrushmc.types.signs")) { event.getPlayer().sendMessage(ChatColor.RED + "You don't have permission to use signs!"); return; }
        //We don't want the player holding anything in their hand!
        if(event.getItem() != null) { event.getPlayer().sendMessage("Please put away your things before using signs!"); return; }

        //Collect Player and Sign instances.
        Player p = event.getPlayer();
        Sign sign = (Sign) bs;


        //If the player is within a types, we need to throw an event to handle this sign click.
        if(this.trainArea.contains(p.getLocation().getBlock())) {
            StationSignEvent sEvent = new StationSignEvent(this, sign, p);
            Bukkit.getServer().getPluginManager().callEvent(sEvent);
        }
    }

    /**
     * Updates the specified types when a new sign is placed.
     *
     * @param event
     */
    @EventHandler
    public abstract void onSignPlacedWithin(BlockPlaceEvent event);

    @Override
    @EventHandler
    public void onPlayerDamageAttempt(BlockDamageEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlock();

        //If the block to be damaged is a part of a trainstation types, we cancel it.
        if(this.trainStationBlocks.contains(b)) {
            //If the player has permission to edit the blocks, we are ok with them doing so.
            if(p.hasPermission("goldrushmc.types.edit")) return;
            p.sendMessage(ChatColor.DARK_RED + "You cannot damage this block! it belongs to a trainstation types!");
            event.setCancelled(true);
        }
    }

    @Override
    @EventHandler
    public void onPlayerBreakAttempt(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlock();
        //If the block to be broken is a part of a trainstation types, we cancel it.
        if(this.trainStationBlocks.contains(b)) {
            //If the player has permission to edit the blocks, we are ok with them doing so.
            if(p.hasPermission("goldrushmc.types.edit")) return;

            p.sendMessage(ChatColor.DARK_RED + "You cannot break this block! it belongs to a trainstation types!");
            event.setCancelled(true);
        }
    }

    @Override
    @EventHandler
    public void onPlayerPlaceAttempt(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlockAgainst();

        //If the block to be broken is a part of a trainstation types, we cancel it.
        if(this.trainStationBlocks.contains(b)) {
            //If the player has permission to edit the blocks, we are ok with them doing so.
            if(p.hasPermission("goldrushmc.types.edit")) return;
            p.sendMessage(ChatColor.DARK_RED + "You cannot blocks here! The area belongs to a trainstation types!");
            event.setCancelled(true);
        }
    }
}
