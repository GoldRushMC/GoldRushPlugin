package com.goldrushmc.bukkit.train.listeners;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberChest;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberRideable;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.goldrushmc.bukkit.defaults.DefaultListener;
import com.goldrushmc.bukkit.train.event.*;
import com.goldrushmc.bukkit.train.signs.SignType;
import com.goldrushmc.bukkit.train.station.*;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Currently, all this class really does is adds and removes {@link Player}s from the {@link TrainStation#visitors} list.
 *
 * @author Diremonsoon
 */
public class TrainStationLis extends DefaultListener {

    public TrainStationLis(JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Handles the {@link StationSignEvent} for each station.
     *
     * @param event
     */
    @EventHandler
    public void onStationSignUse(StationSignEvent event) {
        Player player = event.getPlayer();
        SignType type = event.getSignType();
        TrainStation station = event.getTrainStation();

        //Determine which thing to perform.
        switch (type) {
            case ADD_STORAGE_CART:
                station.buyCart(player, EntityType.MINECART_CHEST);
                break;
            case ADD_RIDE_CART:
                station.buyCart(player, EntityType.MINECART);
                break;
            case REMOVE_STORAGE_CART:
                station.sellCart(player, EntityType.MINECART_CHEST);
                break;
            case REMOVE_RIDE_CART:
                station.sellCart(player, EntityType.MINECART);
                break;
            case FIX_BRIDGE: //TODO This will deal with fixing the broken rail pathways. SOME logic exists yet for path finding.
            default:
                break; //If it is none of these, we do not care right now.
        }

    }

    @EventHandler
    public void onEntrance(EnterTrainStationEvent event) {
        TrainStation station = event.getTrainStation();
        Player p = event.getPlayer();
        p.sendMessage("Welcome to " + station.getStationName() + "!");
        if (!station.getVisitors().contains(p)) {
            station.addVisitor(p);
        }
    }

    @EventHandler
    public void onExit(ExitTrainStationEvent event) {
        TrainStation station = event.getTrainStation();
        Player p = event.getPlayer();
        p.sendMessage(station.getStationName() + " wishes you a Goodbye..");
        if (station.getVisitors().contains(p)) {
            station.removeVisitor(p);
        }
    }

    @EventHandler
    public void onTrainDepart(TrainExitStationEvent event) {
        MinecartGroup mg = event.getTrain();
        mg.getProperties().setColliding(false);
        event.getTrainStation().removeTrainStopped(mg);
    }

    @EventHandler
    public void onTrainArrive(TrainEnterStationEvent event) {
        MinecartGroup mg = event.getTrain();
        TrainStation station = event.getTrainStation();
        mg.getProperties().setSpeedLimit(0.2);
        mg.getProperties().setColliding(true);
        station.addTrain(mg);
        station.addTrainSlow(mg);
    }

    @EventHandler
    public void onTrainNextToDepart(TrainFullStopEvent event) {
        TrainStation station = event.getTrainStation();
        MinecartGroup train = event.getTrain();
        Block toStop = null;
        if (station.getType().equals(StationType.STORAGE_TRANS)) {
            toStop = ((TrainStationTransport) station).getMainStopBlock();

            //We check to see if the train has already stopped. if it has, we don't want to bug it!
            if (station.hasStopped(train)) return;

            for (MinecartMember<?> mm : train) {
                if (mm instanceof MinecartMemberFurnace) {
                    if (mm.getBlock().equals(toStop)) {
                        train.getProperties().setSpeedLimit(0);
                        station.addDepartingTrain(train);
                        station.changeSignLogic(event.getTrain().getProperties().getTrainName());
                        station.updateCartsAvailable(train, EntityType.MINECART_CHEST);
                        station.addTrainStopped(train);
                        break;
                    } else {
                        train.getProperties().setSpeedLimit(0.1);
                    }
                }
            }
        } else if (station.getType().equals(StationType.PASSENGER_TRANS)) {
            toStop = ((PublicTrainStation) station).getMainStopBlock();

            //We check to see if the train has already stopped. if it has, we don't want to bug it!
            if (station.hasStopped(train)) return;

            for (MinecartMember<?> mm : train) {
                if (mm instanceof MinecartMemberFurnace) {
                    if (mm.getBlock().equals(toStop)) {
                        train.getProperties().setSpeedLimit(0);
                        station.addDepartingTrain(train);
                        station.changeSignLogic(event.getTrain().getProperties().getTrainName());
                        station.updateCartsAvailable(train, EntityType.MINECART);
                        train.eject();
                        station.addTrainStopped(train);
                        break;
                    } else {
                        train.getProperties().setSpeedLimit(0.1);
                    }
                }
            }
        } else if (station.getType().equals(StationType.HYBRID_TRANS)) {
            HybridTrainStation hts = (HybridTrainStation) station;
            if (hts.hasStopped(train)) return;
            //We need to check the middle of the train, to determine what type it is.
            if (train.middle() instanceof MinecartMemberChest) toStop = hts.getMainGoodsStop();
            else if (train.middle() instanceof MinecartMemberRideable) toStop = hts.getMainPublicStop();

            for (MinecartMember<?> mm : train) {
                if (mm instanceof MinecartMemberFurnace) {
                    if (train.middle() instanceof MinecartMemberChest) {
                        if (mm.getBlock().equals(toStop)) {
                            train.getProperties().setSpeedLimit(0);
                            station.addDepartingTrain(train);
                            station.changeSignLogic(event.getTrain().getProperties().getTrainName());
                            station.updateCartsAvailable(train, EntityType.MINECART_CHEST);
                            station.addTrainStopped(train);
                            break;
                        }
                    } else if (train.middle() instanceof MinecartMemberRideable) {
                        if (mm.getBlock().equals(toStop)) {
                            train.getProperties().setSpeedLimit(0);
                            station.addDepartingTrain(train);
                            station.changeSignLogic(event.getTrain().getProperties().getTrainName());
                            station.updateCartsAvailable(train, EntityType.MINECART);
                            train.eject();
                            station.addTrainStopped(train);
                            break;
                        }
                    } else {
                        train.getProperties().setSpeedLimit(0.1);
                    }
                }
            }

        }
    }

    /**
     * Controls chest ownership for carts.
     *
     * @param e The {@link InventoryMoveItemEvent} associated with the chest.
     */
    @EventHandler
    public void onInventoryOpening(InventoryOpenEvent e) {
        Inventory inv = e.getInventory();
        if (e.getPlayer() instanceof Player) {
            Player p = (Player) e.getPlayer();
            if (inv.getHolder() instanceof Minecart) {
                Minecart cart = (Minecart) inv.getHolder();
                MinecartMember<?> toCheck = MinecartMemberStore.getAt(cart.getLocation());
                if (toCheck != null && !toCheck.isDerailed()) {
                    CartProperties cp = toCheck.getProperties();
                    if (!cp.getOwners().contains(p.getName().toLowerCase())) {
                        p.sendMessage(ChatColor.RED + "You do not own this chest!");
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
