package com.goldrushmc.bukkit.trainstation.types;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberChest;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberRideable;
import com.bergerkiller.bukkit.tc.events.MemberBlockChangeEvent;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.goldrushmc.bukkit.db.tables.StationLocationTbl;
import com.goldrushmc.bukkit.db.tables.StationTbl;
import com.goldrushmc.bukkit.trainstation.StationType;
import com.goldrushmc.bukkit.trainstation.TrainStation;
import com.goldrushmc.bukkit.trainstation.event.*;
import com.goldrushmc.bukkit.trainstation.exceptions.MarkerNumberException;
import com.goldrushmc.bukkit.trainstation.exceptions.MissingSignException;
import com.goldrushmc.bukkit.trainstation.exceptions.StopBlockMismatchException;
import com.goldrushmc.bukkit.trainstation.npc.CartTradeable;
import com.goldrushmc.bukkit.trainstation.signs.SignType;
import com.goldrushmc.bukkit.trainstation.tracks.SmallBlockMap;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TrainStationTransport extends TrainStation {

    private final int maxStopBlocks = 4;
    private final Material stopMat;
    private final List<Block> stopBlocks;
    private Block mainStop;
    //	private final List<Chest> lockers;
    //	private Map<Player, Chest> lockerPlayerMap = new HashMap<Player, Chest>();

    public TrainStationTransport(JavaPlugin plugin, String stationName, List<Location> markers, World world, boolean train, boolean toDB) throws MissingSignException, MarkerNumberException, StopBlockMismatchException {
        super(plugin, stationName, markers, world);
        this.stopMat = defaultStop;
        this.stopBlocks = findStopBlocks(this.stopMat);
        this.directions = new ArrayList<>();
        if (this.stopBlocks.get(0).getData() == 0) {
            this.directions.add(BlockFace.NORTH);
            this.directions.add(BlockFace.SOUTH);
        } else if (this.stopBlocks.get(0).getData() == 1) {
            this.directions.add(BlockFace.EAST);
            this.directions.add(BlockFace.WEST);
        }
        for (Block b : this.stopBlocks) {
            if (this.stopBlocks.contains(b.getRelative(this.directions.get(0)))
                    && this.stopBlocks.contains(b.getRelative(this.directions.get(1)))) {
                this.mainStop = b;
                break;
            }
        }
        //Default to the first Block in the list of stopBlocks.
        if (this.mainStop == null) this.mainStop = this.stopBlocks.get(0);
        if (train) {
            if (this.rails != null) {
                if (this.stopBlocks.size() > maxStopBlocks) throw new StopBlockMismatchException();
                if (!findStillTrains()) createTransport();
            }
        }
        add();
        if(toDB) addToDB(markers);
//		createWorkers();
        //		this.lockers = findLockers();
    }

    public TrainStationTransport(JavaPlugin plugin, String stationName, List<Location> markers, World world, Material stopMat, boolean train) throws MissingSignException, MarkerNumberException, StopBlockMismatchException {
        super(plugin, stationName, markers, world, stopMat);
        this.stopMat = stopMat;
        this.stopBlocks = findStopBlocks(stopMat);
        this.directions = new ArrayList<>();
        if (this.stopBlocks.get(0).getData() == 0) {
            this.directions.add(BlockFace.NORTH);
            this.directions.add(BlockFace.SOUTH);
        } else if (this.stopBlocks.get(0).getData() == 1) {
            this.directions.add(BlockFace.EAST);
            this.directions.add(BlockFace.WEST);
        }
        for (Block b : this.stopBlocks) {
            if (this.stopBlocks.contains(b.getRelative(this.directions.get(0)))
                    && this.stopBlocks.contains(b.getRelative(this.directions.get(1)))) {
                this.mainStop = b;
                break;
            }
        }
        if (train) {
            if (this.rails != null) {
                if (this.stopBlocks.size() > maxStopBlocks) throw new StopBlockMismatchException();
                if (!findStillTrains()) createTransport();
            }
        }
        add();
//		createWorkers();
        //		this.lockers = findLockers();
    }

    @Override
    public StationType getType() {
        return StationType.STORAGE_TRANS;
    }

    /**
     * Adds the trainstation types to the database, in case of a server wide crash.
     */
    public void addToDB(List<Location> coords) {
        StationTbl station = new StationTbl();
        station.setName(stationName);
        Set<StationLocationTbl> corners = new HashSet<>();
        for(int i = 0; i < 2; i++) {
            StationLocationTbl corner = new StationLocationTbl();
            corner.initBlock(coords.get(i).getBlock());
            corner.setStation(station);
            corner.setWorld(world.getName());
            corners.add(corner);
        }
        station.setLocations(corners);
        station.setType(getType());
        station.setTrainInStation(hasDepartingTrain());
        db.getDB().save(station);
        db.getDB().save(corners);
    }

    public void removeFromDB() {
        StationTbl station = db.queryTrainStations().where().ieq("name", stationName).findUnique();
        if(station != null) {
            for(StationLocationTbl loc : station.getLocations()) {
                db.getDB().delete(loc);
            }
            db.getDB().delete(station);
        }
    }

    /**
     * A way to find all of the lockers in the selectedArea.
     *
     * @return
     */
    protected List<Chest> findLockers() {
        List<Chest> lockers = new ArrayList<>();
        for (Block b : this.trainArea) {
            if (b.getType().equals(Material.CHEST)) {
                if (b.getState() instanceof Chest) {
                    lockers.add((Chest) b.getState());
                }
            }
        }
        return lockers;
    }

    //	public List<Chest> getLockers() {
    //		return this.lockers;
    //	}

    @Override
    public boolean buyCart(Player owner, EntityType type) {
        if (this.departingTrains.get(0) == null) {
            owner.sendMessage("There is currently no trainstation available to buy from. ");
            return false;
        }
        //The boolean success will be set true if a minecart is actually bought.
        boolean success = false;
        for (MinecartMember<?> cart : this.departingTrains.get(0)) {
            //If the cart is an instance of the furnace, skip it.
            if (cart instanceof MinecartMemberFurnace) continue;
            switch (type) {
                //If the player wants a passenger minecart, do this.
                case MINECART: {
                    if (cart instanceof MinecartMemberRideable) {
                        //If the owners list is empty, set the owner!
                        if (cart.getProperties().getOwners().isEmpty()) {
                            cart.getProperties().setOwner(owner);
                            success = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                //If the player wants a chest minecart, do this.
                case MINECART_CHEST: {
                    if (cart instanceof MinecartMemberChest) {
                        //If the owners list is empty, set the owner!
                        if (cart.getProperties().getOwners().isEmpty()) {
                            cart.getProperties().setOwner(owner);
                            success = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                //This should never happen, but if it does, oh well.
                default:
                    break;
            }
            //If we have already gotten a cart, end the loop.
            if (success) {
                //If the buy was successful, send a confirmation.
                switch (type) {
                    case MINECART:
                        owner.sendMessage("You bought" + ChatColor.BLUE + " passenger cart" + ChatColor.GREEN + " [#" + (cart.getIndex() + 1) + "]"
                                + " from trainstation " + this.departingTrains.get(0).getProperties().getTrainName());
                        break;
                    case MINECART_CHEST:
                        owner.sendMessage("You bought" + ChatColor.BLUE + " storage cart" + ChatColor.GREEN + " [#" + (cart.getIndex() + 1) + "]"
                                + " from trainstation " + this.departingTrains.get(0).getProperties().getTrainName());
                        break;
                    default:
                        break;
                }
                this.updateCartsAvailable(this.departingTrains.get(0), type);
                return true;
            }
        }
        //If the buy wasn't successful, send a message.
        if (!success) {
            switch (type) {
                case MINECART:
                    owner.sendMessage("There are no" + ChatColor.BLUE + " passenger cart(s) " + ChatColor.RESET + "for sale"
                            + " on trainstation " + this.departingTrains.get(0).getProperties().getTrainName());
                    break;
                case MINECART_CHEST:
                    owner.sendMessage("There are no" + ChatColor.BLUE + " storage cart(s) " + ChatColor.RESET + "for sale"
                            + " from trainstation " + this.departingTrains.get(0).getProperties().getTrainName());
                    break;
                default:
                    break;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean sellCart(Player owner, EntityType type) {
        if (this.departingTrains == null) return false;
        //The boolean success will be set true if a minecart is actually bought.
        boolean success = false;
        for (MinecartMember<?> cart : this.departingTrains.get(0)) {
            //If the cart is an instance of the furnace, skip it.
            if (cart instanceof MinecartMemberFurnace) continue;
            switch (type) {
                //If the player wants a passenger minecart, do this.
                case MINECART:
                    if (cart instanceof MinecartMemberRideable) {
                        //If the owners list is not empty, and the owner is the player, remove them!
                        if (!cart.getProperties().getOwners().isEmpty()) {
                            if (cart.getProperties().getOwners().contains(owner.getName().toLowerCase())) {
                                cart.getProperties().getOwners().remove(owner.getName().toLowerCase());
                                success = true;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                    //If the player wants a chest minecart, do this.
                case MINECART_CHEST:
                    if (cart instanceof MinecartMemberChest) {
                        //If the owners list is not empty, and the owner is the player, remove them!
                        if (!cart.getProperties().getOwners().isEmpty()) {
                            if (cart.getProperties().getOwners().contains(owner.getName().toLowerCase())) {
                                cart.getProperties().getOwners().remove(owner.getName().toLowerCase());
                                success = true;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                    //This should never happen, but if it does, oh well.
                default:
                    break;
            }
            if (success) {
                //If the sell was successful, send a confirmation.
                switch (type) {
                    case MINECART:
                        owner.sendMessage("You sold" + ChatColor.BLUE + " passenger cart" + ChatColor.GREEN + " [#" + (cart.getIndex() + 1) + "]");
                        break;
                    case MINECART_CHEST:
                        owner.sendMessage("You sold" + ChatColor.BLUE + " storage cart" + ChatColor.GREEN + " [#" + (cart.getIndex() + 1) + "]");
                        break;
                    default:
                        break;
                }
                this.updateCartsAvailable(this.departingTrains.get(0), type);
                return true;
            }
        }
        //If the sell wasn't successful, send a message.
        if (!success) {
            switch (type) {
                case MINECART:
                    owner.sendMessage("You have no" + ChatColor.BLUE + " passenger cart(s) " + ChatColor.RESET + "to sell...");
                    break;
                case MINECART_CHEST:
                    owner.sendMessage("You have no" + ChatColor.BLUE + " storage cart(s) " + ChatColor.RESET + "to sell...");
                    break;
                default:
                    break;
            }
            return false;
        }
        return false;
    }

    public void createTransport() {

        if (this.trains == null) this.trains = new ArrayList<>();
        int trainNum = this.trains.size() + 1;

        List<EntityType> carts = new ArrayList<>();
        SmallBlockMap sbm = new SmallBlockMap(this.mainStop);
        BlockFace dir = this.directions.get(1); //TODO
        //Iterate to get the max size of minecarts, or just short of it if the rails end.
        for (int i = 0; i < 14; i++) {
            sbm = new SmallBlockMap(sbm.getBlockAt(dir));
            if (!sbm.isRail(sbm.getBlockAt(dir))) {
                break;
            }
            carts.add(EntityType.MINECART_CHEST);
        }
        carts.add(EntityType.MINECART_FURNACE);
        //Should make the furnace spawn right on top of the stop block.
        MinecartGroup train = MinecartGroup.spawn(this.mainStop, this.directions.get(1), carts); //TODO

        TrainProperties tp = train.getProperties();
        tp.setName(stationName + "_" + trainNum);
        tp.setColliding(false);
        tp.setSpeedLimit(0.4);
        tp.setPublic(false);
        tp.setManualMovementAllowed(false);
        tp.setKeepChunksLoaded(true);
        tp.setPickup(false);
        train.setProperties(tp);

        this.addTrain(train);
        if (this.departingTrains.isEmpty()) this.departingTrains.add(train);
        this.changeSignLogic(train.getProperties().getTrainName());
        this.updateCartsAvailable(train, EntityType.MINECART_CHEST);
        this.hasStopped.put(train, true);
    }

    /**
     * Find the stop blocks for a typical transport types.
     * <p/>
     * Assumption:
     * <li> There are a max of 4 stop blocks, all of which are on the same length of track, all clumped together.
     * <li> The blocks will be ordered as such:
     * <ol>
     * <li> 0 : The block which the trainstation shall spawn on (furthest in the trainstation types's designated direction).
     * <li> 1, 2, 3 : The other blocks which will support the departure function.
     */
    @Override
    public List<Block> findStopBlocks(Material m) {
        List<Block> stopBlocks = new ArrayList<>();
        for (Block b : this.trainArea) {
            if (b.getType().equals(m)) {
                stopBlocks.add(b.getRelative(BlockFace.UP));
            }
        }
        return stopBlocks;
    }

    @Override
    public List<Block> getStopBlocks() {
        return stopBlocks;
    }

    @Override
    public boolean pushQueue() {
        if(departingTrains.isEmpty()) return false;
        MinecartGroup mg = this.departingTrains.get(0);
        mg.getProperties().setSpeedLimit(0.4);
        mg.getProperties().setColliding(false);
        for (MinecartMember<?> mm : mg) {
            if (mm instanceof MinecartMemberFurnace) {
                MinecartMemberFurnace power = (MinecartMemberFurnace) mm;
                power.addFuelTicks(1000);
                if (!power.getDirection().equals(this.directions.get(0))) { //TODO
                    mg.reverse();
                }
            }
        }
        this.trains.remove(mg);
        this.departingTrains.remove(0);
        return true;
    }

    @Override
    public void createWorkers() {
        for (Chunk c : this.chunks) {
            Entity[] entities = c.getEntities();
            for (int i = 0; i < entities.length; i++) {
                if (CitizensAPI.getNPCRegistry().isNPC(entities[i])) {
                    NPC npc = CitizensAPI.getNPCRegistry().getNPC(entities[i]);
                    npc.addTrait(CartTradeable.class);
                    this.workers.add(npc);
                }
            }
        }
    }

    @Override
    public void changeSignLogic(String trainName) {
        this.signs.updateTrain(trainName);
    }

    public Block getMainStopBlock() {
        return this.mainStop;
    }

    @Override
    public boolean hasDepartingTrain() {
        return !departingTrains.isEmpty();
    }

    @Override
    public boolean hasCartsToSell() {
        if (!hasDepartingTrain()) return false;

        for (MinecartMember<?> mm : this.departingTrains.get(0)) {
            if (mm instanceof MinecartMemberChest) {
                if (mm.getProperties().getOwners().isEmpty()) return true;
            }
        }
        return false;
    }

    @Override
    @EventHandler
    public void onTrainMove(MemberBlockChangeEvent event) {
        Block to = event.getTo(), from = event.getFrom();
        MinecartMember<?> cart = event.getMember();
        MinecartMemberFurnace furnace = null;
        if (cart instanceof MinecartMemberFurnace) {
            furnace = (MinecartMemberFurnace) cart;
        }

        //We don't care about non-furnaces. Furnaces lead the charge!
        if (furnace == null) return;

        if (this.trainArea.contains(to)) {
            if (!this.trainArea.contains(from)) {
                //Entering types
                TrainEnterStationEvent enter = new TrainEnterStationEvent(this, event.getGroup());
                Bukkit.getServer().getPluginManager().callEvent(enter);
            }

            //The trainstation has hit the stop block, and needs to stop.
            else if (getStopBlocks().contains(to)) {
                //Check to see if there is already a departing trainstation assigned. If so, we don't need to add it again.
                if (this.departingTrains.isEmpty()) {
                    //If there is no departing trainstation yet, we need to add this one.
                    TrainFullStopEvent stop = new TrainFullStopEvent(this, event.getGroup());
                    Bukkit.getServer().getPluginManager().callEvent(stop);
                }
            }
        }
        //Leaving types
        else if (!this.trainArea.contains(to) && this.trainArea.contains(from)) {
            TrainExitStationEvent exit = new TrainExitStationEvent(this, event.getGroup());
            Bukkit.getServer().getPluginManager().callEvent(exit);
        }
    }

    @Override
    @EventHandler
    public void onSignPlacedWithin(BlockPlaceEvent event) {
        if (this.trainArea.contains(event.getBlock())) {
            //If it isn't a sign, we don't care.
            if (!(event.getBlock().getState() instanceof Sign)) return;

            Sign sign = (Sign) event.getBlock().getState();
            //We only want signs that have the symbol {trains} denoted on them.
            if (!sign.getLine(0).equals("{trains}")) return;

            boolean isUseful = this.getSigns().addSign(sign.getLine(1), sign);

            if (isUseful) {

                String trainName = "N/A";
                int cartCount = 0;
                MinecartGroup train = this.departingTrains.get(0);
                if (train != null) {
                    trainName = train.getProperties().getTrainName();
                    cartCount = train.size() - 1;
                }

                SignType type = this.getSigns().getSignType(sign);
                switch (type) {
                    case ADD_RIDE_CART:
                    case ADD_STORAGE_CART:
                    case REMOVE_RIDE_CART:
                    case REMOVE_STORAGE_CART:
                    case TRAIN_STATION_DEPARTING: {
                        sign.setLine(2, trainName);
                        sign.update();
                        event.getPlayer().sendMessage("You placed a " + type.toString() + " Sign!");
                        break;
                    }
                    case TRAIN_STATION_CART_COUNT: {
                        String count = "N/A";
                        if (cartCount != 0) {
                            count = String.valueOf(cartCount);
                        }
                        sign.setLine(2, count);
                        event.getPlayer().sendMessage("You placed a Cart Counter Sign!");
                        break;
                    }
                    case TRAIN_STATION_TIME:
                        break;
                    default:
                        break;
                }
            }
        }

    }

    @Override
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        //We look for the blocks to and from
        Block from = event.getFrom().getBlock(), to = event.getTo().getBlock();

        //Has left the types!
        if (this.trainArea.contains(from) && !this.trainArea.contains(to)) {
            ExitTrainStationEvent exit = new ExitTrainStationEvent(this, event.getPlayer());
            Bukkit.getServer().getPluginManager().callEvent(exit);
        }

        //Has entered the types!
        else if (this.trainArea.contains(to) && !this.trainArea.contains(from)) {
            EnterTrainStationEvent enter = new EnterTrainStationEvent(this, event.getPlayer());
            Bukkit.getServer().getPluginManager().callEvent(enter);
        }

    }

    @Override
    public boolean findStillTrains() {
        MinecartGroup[] trains = MinecartGroup.getGroups();
        for (MinecartGroup train : trains) {
            if (!train.isMoving()) {
                //At least the front of the trainstation must be WITHIN the types.
                for (MinecartMember<?> mm : train) {

                    boolean foundTrain = false;
                    if (!(mm instanceof MinecartMemberFurnace)) continue;

                    if (this.stopBlocks.contains(mm.getBlock())) {
                        this.departingTrains.add(train);
                        this.hasStopped.put(train, true);
                        foundTrain = true;
                        break;
                    } else {
                        for (Block b : this.trainArea) {
                            //If the trainstation is within the types grounds, accept it.
                            if (b.equals(mm.getBlock())) {
                                this.trains.add(train);
                                this.hasStopped.put(train, false);
                                foundTrain = true;
                                break;
                            }
                        }
                    }
                    return foundTrain;
                }
            }
        }
        return false;
    }

    @Override
    public List<MinecartGroup> getDepartingTrains() {
        return departingTrains;
    }

    @Override
    public void addDepartingTrain(MinecartGroup train) {
        departingTrains.add(train);

    }
}
