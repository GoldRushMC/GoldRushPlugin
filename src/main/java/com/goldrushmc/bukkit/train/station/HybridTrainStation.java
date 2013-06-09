package com.goldrushmc.bukkit.train.station;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberChest;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberRideable;
import com.bergerkiller.bukkit.tc.events.MemberBlockChangeEvent;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.goldrushmc.bukkit.train.event.*;
import com.goldrushmc.bukkit.train.exceptions.NoExitAssignedException;
import com.goldrushmc.bukkit.train.exceptions.StopBlockMismatchException;
import com.goldrushmc.bukkit.train.signs.SignType;
import com.goldrushmc.bukkit.train.station.tracks.SmallBlockMap;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the station that is meant for TWO rail tracks. One is for public transport, the other is for goods.
 * <p/>
 * This station class will be the most widely used, as it encompasses public and good transport, which is how the gold rush map was built...
 *
 * @author Diremonsoon
 */
public class HybridTrainStation extends TrainStation {

    private Material stopMat;
    private final int maxStopBlocks = 8;
    private List<Block> stopBlocks;
    private Block mainPublicStop;
    private List<Block> publicStopBlocks = new ArrayList<>();
    private List<Block> goodsStopBlocks = new ArrayList<>();
    private Block mainGoodsStop;
    private volatile MinecartGroup publicTrain;
    private volatile MinecartGroup goodsTrain;
    private final Block exitBlock;

    public HybridTrainStation(JavaPlugin plugin, String stationName, List<Location> markers, World world, boolean train) throws Exception {
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
        //Get the public and goods tracks.
        findTrackTypes();
        //Try to get the passenger exit area, and if it fails, throw an exception.
        List<Sign> s = signs.getSigns(SignType.TRAIN_STATION_PASSENGER_EXIT);
        if (!s.isEmpty()) exitBlock = s.get(0).getBlock();
        else throw new NoExitAssignedException();
        //Remove the sign, since we have the exit block. change it to air.
        signs.removeSign("Passenger Exit");
        exitBlock.setType(Material.AIR);

        //This should work. However, we are making many assumptions and have not considered throwing any exceptions.
        //This is due to LACK OF TIME AND RESOURCES, NOT LAZINESS ( I swear...)

        //Get the main public stop block
        for (Block b : publicStopBlocks) {
            if (b.getRelative(BlockFace.DOWN).getRelative(this.directions.get(0)).getType().equals(stopMat)
                    && b.getRelative(BlockFace.DOWN).getRelative(this.directions.get(1)).getType().equals(stopMat)) {
                this.mainPublicStop = b;
                break;
            }
        }
        //Get the main transport stop block
        for (Block b : goodsStopBlocks) {
            if (b.getRelative(BlockFace.DOWN).getRelative(this.directions.get(0)).getType().equals(stopMat)
                    && b.getRelative(BlockFace.DOWN).getRelative(this.directions.get(1)).getType().equals(stopMat)) {
                this.mainGoodsStop = b;
                break;
            }
        }
        if (train) {
            if (this.rails != null) {
                if (this.stopBlocks.size() > maxStopBlocks) throw new StopBlockMismatchException();
                if (findStillTrains()) {
                    if (this.goodsTrain == null) createGoodsTransport();
                    else if (this.publicTrain == null) createPublicTransport();
                } else {
                    createPublicTransport();
                    createGoodsTransport();
                } //TODO
            }
        }
        add();
    }

    /**
     * Will generate a Public Transportation type train. All rideable minecarts.
     */
    public void createPublicTransport() {

        if (this.trains == null) this.trains = new ArrayList<>();
        int trainNum = this.trains.size() + 1;

        List<EntityType> carts = new ArrayList<>();
        SmallBlockMap sbm = new SmallBlockMap(mainPublicStop);
        BlockFace dir = this.directions.get(1); //TODO
        //Iterate to get the max size of minecarts, or just short of it if the rails end.
        int i = 0;
        while (!sbm.isEnd() || i < 14) {
            sbm = new SmallBlockMap(sbm.getBlockAt(dir));
            if (!sbm.isRail(sbm.getBlockAt(dir))) {
                break;
            }
            carts.add(EntityType.MINECART);
            i++;
        }
        carts.add(EntityType.MINECART_FURNACE);

        //Should make the furnace spawn right on top of the stop block.
        MinecartGroup train = MinecartGroup.spawn(mainPublicStop, dir, carts); //TODO

        TrainProperties tp = train.getProperties();
        tp.setName(stationName + "_" + trainNum);
        tp.setColliding(false);
        tp.setSpeedLimit(0.8);
        tp.setPublic(false);
        tp.setManualMovementAllowed(false);
        tp.setKeepChunksLoaded(true);
        tp.setPickup(false);
        train.setProperties(tp);

        this.addTrain(train);
        this.publicTrain = train;
        this.changeSignLogic(train.getProperties().getTrainName());
        this.updateCartsAvailable(train, EntityType.MINECART);
        this.hasStopped.put(train, true);
    }

    public void createGoodsTransport() {

        if (this.trains == null) this.trains = new ArrayList<MinecartGroup>();
        int trainNum = this.trains.size() + 1;

        List<EntityType> carts = new ArrayList<>();
        SmallBlockMap sbm = new SmallBlockMap(mainGoodsStop);
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
        MinecartGroup train = MinecartGroup.spawn(mainGoodsStop, dir, carts); //TODO

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
        this.goodsTrain = train;
        this.changeSignLogic(train.getProperties().getTrainName());
        this.updateCartsAvailable(train, EntityType.MINECART_CHEST);
        this.hasStopped.put(train, true);
    }

    /**
     * MAKING THE ASSUMPTION THAT TRAIN STATION MOST-WEST TRACKS ARE PUBLIC, AND MOST-EAST ARE TRANSPORT.
     * ALSO, MOST-NORTH ARE PUBLIC AND MOST-SOUTH ARE TRANSPORT.
     * <p/>
     * Also, We assume (I know, double assumptions = bad...) that the tracks will be reasonably placed and they are in a straight line for AT LEAST
     * 15 blocks.
     * <p/>
     * Assumption #3: We assert that there will be 2 tracks, no more, no less, in parallel.
     * <p/>
     * We also need the stop blocks to be NEXT TO EACH OTHER. If they aren't, this will get awkward fast...
     */
    public void findTrackTypes() {
        //We need the min value for
        int minVal = Integer.MAX_VALUE;
        switch (this.directions.get(0)) {
            case NORTH:
            case SOUTH: {
                //Iterate through the blocks, and find the lowest x value possible.
                for (Block b : this.stopBlocks) if (b.getX() < minVal) minVal = b.getX();
            /*
			 * Now split the blocks into their respective sides.
			 * This works fine, but will cause problems if the blocks are not next to each other...
			 */
                for (Block b : this.stopBlocks) {
                    if (b.getX() == minVal) {
                        this.publicStopBlocks.add(b);
                    } else if (b.getX() > minVal) {
                        this.goodsStopBlocks.add(b);
                    }
                }
                break;
            }
            case WEST:
            case EAST: {
                //Iterate through the blocks, and find the lowest x value possible.
                for (Block b : this.stopBlocks) if (b.getZ() < minVal) minVal = b.getZ();
			/*
			 * Now split the blocks into their respective sides.
			 * This works fine, but will cause problems if the blocks are not next to each other...
			 */
                for (Block b : this.stopBlocks) {
                    if (b.getZ() == minVal) {
                        this.publicStopBlocks.add(b);
                    } else if (b.getZ() > minVal) {
                        this.goodsStopBlocks.add(b);
                    }
                }
                break;
            }
            default:
                break;
        }

    }

    @Override
    public StationType getType() {
        return StationType.HYBRID_TRANS;
    }

    @Override
    public boolean buyCart(Player owner, EntityType type) {
        switch (type) {
            case MINECART: {
                if (this.publicTrain == null) {
                    owner.sendMessage("There is no public train to buy from at this time.");
                    return false;
                }
                for (MinecartMember<?> mm : this.publicTrain) {
                    if (mm instanceof MinecartMemberFurnace) continue;
                    if (mm instanceof MinecartMemberRideable) {
                        if (mm.getProperties().getOwners().isEmpty()) {
                            mm.getProperties().setOwner(owner);
                            owner.sendMessage("You just bought public cart: " + ChatColor.YELLOW + "(" + mm.getIndex() + ")"
                                    + " from train: " + this.publicTrain.getProperties().getTrainName());
                            return true;
                        }
                    }
                }
                owner.sendMessage("There are currently no public carts available for purchase...");
                return false;
            }
            case MINECART_CHEST: {
                if (this.goodsTrain == null) {
                    owner.sendMessage("There is no transport train to buy from at this time.");
                    return false;
                }
                for (MinecartMember<?> mm : this.goodsTrain) {
                    if (mm instanceof MinecartMemberFurnace) continue;
                    if (mm instanceof MinecartMemberChest) {
                        if (mm.getProperties().getOwners().isEmpty()) {
                            mm.getProperties().setOwner(owner);
                            owner.sendMessage("You just bought transport cart: " + ChatColor.YELLOW + "(" + mm.getIndex() + ")"
                                    + " from train: " + this.goodsTrain.getProperties().getTrainName());
                            return true;
                        }
                    }
                }
                owner.sendMessage("There are currently no transport carts available for purchase...");
                return false;
            }
            default:
                return false;
        }
    }

    @Override
    public boolean sellCart(Player owner, EntityType type) {
        switch (type) {
            case MINECART: {
                if (this.publicTrain == null) {
                    owner.sendMessage("There is no public train to sell from at this time.");
                    return false;
                }
                for (MinecartMember<?> mm : this.publicTrain) {
                    if (mm instanceof MinecartMemberFurnace) continue;
                    if (mm instanceof MinecartMemberRideable) {
                        if (!mm.getProperties().getOwners().isEmpty()) {
                            if (mm.getProperties().getOwners().contains(owner.getName().toLowerCase())) {
                                mm.getProperties().clearOwners();
                                owner.sendMessage("You just sold public cart: " + ChatColor.YELLOW + "(" + mm.getIndex() + ")"
                                        + " from train " + this.publicTrain.getProperties().getTrainName());
                                return true;
                            }
                        }
                    }
                }
                owner.sendMessage("You don't have any public carts to sell...");
                return false;
            }
            case MINECART_CHEST: {
                if (this.goodsTrain == null) {
                    owner.sendMessage("There is no transport train to sell from at this time.");
                    return false;
                }
                for (MinecartMember<?> mm : this.goodsTrain) {
                    if (mm instanceof MinecartMemberFurnace) continue;
                    if (mm instanceof MinecartMemberChest) {
                        if (!mm.getProperties().getOwners().isEmpty()) {
                            if (mm.getProperties().getOwners().contains(owner.getName().toLowerCase())) {
                                mm.getProperties().setOwner(owner);
                                owner.sendMessage("You just sold transport cart: " + ChatColor.YELLOW + "(" + mm.getIndex() + ")"
                                        + " from train " + this.publicTrain.getProperties().getTrainName());
                                return true;
                            }
                        }
                    }
                }
                owner.sendMessage("You don't have any transport carts to sell...");
                return false;
            }
            default:
                return false;
        }
    }

    @Override
    protected void createWorkers() {
        for (Chunk c : this.chunks) {
            Entity[] entities = c.getEntities();
            for (Entity e : entities) {
                if (this.trainArea.contains(e.getLocation().getBlock())) {
                    //See if the Entity is convertable to an NPC.
                    if (CitizensAPI.getNPCRegistry().isNPC(e)) {
                        //We only want NPC Humans to be converted and used... We don't want cows and chickens...
                        if (e instanceof HumanEntity) {
                            //Convert it!!!
                            this.workers.add(CitizensAPI.getNPCRegistry().getNPC(e));
                        }
                    }
                }
            }
        }

    }

    @Override
    public boolean findStillTrains() {
        MinecartGroup[] trains = MinecartGroup.getGroups();
        int trainsFound = 0;
        for (MinecartGroup mg : trains) {
            Block b = mg.head().getBlock();
            if (!mg.isMoving() && this.rails.contains(b)) {
                if (this.mainGoodsStop.equals(b)) {
                    this.goodsTrain = mg;
                } else if (this.mainPublicStop.equals(b)) {
                    this.publicTrain = mg;
                }
                this.trains.add(mg);
                trainsFound++;
            }
        }
        return trainsFound > 0;
    }

    @Override
    public boolean hasCartsToSell() {
        for (MinecartMember<?> mm : this.goodsTrain) {
            if (mm.getProperties().getOwners().isEmpty()) return true;
        }
        for (MinecartMember<?> mm : this.publicTrain) {
            if (mm.getProperties().getOwners().isEmpty()) return true;
        }
        return false;
    }

    @Override
    public boolean hasDepartingTrain() {
        return (this.publicTrain == null && this.goodsTrain == null);
    }

    @Override
    public boolean pushQueue() {
        if (this.publicTrain == null && this.goodsTrain == null) return false;
        if (this.publicTrain != null) {
            //Iterate, find the fuel cart, and add fuel ticks. This is about 4 1/2 minutes of fuel time. Guesstimate.
            for (MinecartMember<?> mm : this.publicTrain) {
                if (!(mm instanceof MinecartMemberFurnace)) continue;
                ((MinecartMemberFurnace) mm).addFuelTicks(5600);
            }
            //Set the speed limit.
            this.publicTrain.getProperties().setSpeedLimit(0.8);
            //Clear the focus from the train.
            this.publicTrain = null;
        }
        if (this.goodsTrain != null) {
            //Iterate, find the fuel cart, and add fuel ticks. This is about 4 1/2 minutes of fuel time. Guesstimate.
            for (MinecartMember<?> mm : this.publicTrain) {
                if (!(mm instanceof MinecartMemberFurnace)) continue;
                ((MinecartMemberFurnace) mm).addFuelTicks(5600);
            }
            //Set the speed limit.
            this.goodsTrain.getProperties().setSpeedLimit(0.4);
            //Clear the focus from the train.
            this.goodsTrain = null;
        }
        return true;
    }

    @Override
    public void changeSignLogic(String trainName) {
        this.signs.updateTrain(trainName);
    }

    @Override
    public List<Block> findStopBlocks(Material m) {
        List<Block> stopBlocks = new ArrayList<>();
        for (Block b : this.trainArea) {
            if (this.stopMat.equals(b.getType())) {
                //Get the block ABOVE the block we find. This should be a rail.
                stopBlocks.add(b.getRelative(BlockFace.UP));
            }
        }
        return stopBlocks;
    }

    @Override
    public List<Block> getStopBlocks() {
        return stopBlocks;
    }

    //Listener Stuff TODO

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
                //Entering station
                TrainEnterStationEvent enter = new TrainEnterStationEvent(this, event.getGroup());
                Bukkit.getServer().getPluginManager().callEvent(enter);
            }

            //The train has hit the stop block, and needs to stop.
            else if (getStopBlocks().contains(to)) {
                //Check to see if there is already a departing train assigned. If so, we don't need to add it again.
                if (this.departingTrains.isEmpty()) {
                    //If there is no departing train yet, we need to add this one.
                    TrainFullStopEvent stop = new TrainFullStopEvent(this, event.getGroup());
                    Bukkit.getServer().getPluginManager().callEvent(stop);
                }
            }
        }
        //Leaving station
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

        //Has left the station!
        if (this.trainArea.contains(from) && !this.trainArea.contains(to)) {
            ExitTrainStationEvent exit = new ExitTrainStationEvent(this, event.getPlayer());
            Bukkit.getServer().getPluginManager().callEvent(exit);
        }

        //Has entered the station!
        else if (this.trainArea.contains(to) && !this.trainArea.contains(from)) {
            EnterTrainStationEvent enter = new EnterTrainStationEvent(this, event.getPlayer());
            Bukkit.getServer().getPluginManager().callEvent(enter);
        }
    }


    @Override
    public List<MinecartGroup> getDepartingTrains() {
        //Check if both trains are null, and add only the ones that are not.
        List<MinecartGroup> twoTrains = new ArrayList<>();
        if (this.publicTrain != null) twoTrains.add(this.publicTrain);
        if (this.goodsTrain != null) twoTrains.add(this.goodsTrain);
        return twoTrains;
    }

    @Override
    public void addDepartingTrain(MinecartGroup train) {
        for (MinecartMember<?> mm : train) {
            //We don't care about Not-Furnace carts.
            if (!(mm instanceof MinecartMemberFurnace)) return;
            //Public transport train
            if (this.publicStopBlocks.contains(mm.getBlock())) {
                this.publicTrain = train;
            }
            //Goods transport train
            else if (this.goodsStopBlocks.contains(mm.getBlock())) {
                this.goodsTrain = train;
            }
        }
    }

    public Block getMainPublicStop() {
        return mainPublicStop;
    }

    public Block getMainGoodsStop() {
        return mainGoodsStop;
    }

    public List<Block> getPublicStopBlocks() {
        return publicStopBlocks;
    }

    public List<Block> getGoodsStopBlocks() {
        return goodsStopBlocks;
    }

    public Block getExitBlock() {
        return exitBlock;
    }
}
