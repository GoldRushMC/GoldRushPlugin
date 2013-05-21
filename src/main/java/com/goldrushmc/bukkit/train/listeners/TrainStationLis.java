package com.goldrushmc.bukkit.train.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;
import com.bergerkiller.bukkit.tc.events.MemberBlockChangeEvent;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.goldrushmc.bukkit.defaults.DefaultListener;
import com.goldrushmc.bukkit.train.TrainFactory;
import com.goldrushmc.bukkit.train.event.EnterTrainStationEvent;
import com.goldrushmc.bukkit.train.event.ExitTrainStationEvent;
import com.goldrushmc.bukkit.train.event.StationSignEvent;
import com.goldrushmc.bukkit.train.event.TrainEnterStationEvent;
import com.goldrushmc.bukkit.train.event.TrainExitStationEvent;
import com.goldrushmc.bukkit.train.event.TrainFullStopEvent;
import com.goldrushmc.bukkit.train.signs.SignType;
import com.goldrushmc.bukkit.train.station.TrainStation;
import com.goldrushmc.bukkit.train.station.TrainStationTransport;

/**
 * Currently, all this class really does is adds and removes {@link Player}s from the {@link TrainStation#visitors} list.
 * 
 * @author Diremonsoon
 *
 */
public class TrainStationLis extends DefaultListener {

	//Both Maps only exist for quick referential access. Otherwise, we would have to iterate through train stations each time.
	private static Map<Block, String> stationArea = new HashMap<Block, String>();
	private static Map<String, TrainStation> stationStore = new HashMap<String, TrainStation>();
	private static Map<MinecartGroup, Boolean> hasStopped = new HashMap<MinecartGroup, Boolean>();

	public TrainStationLis(JavaPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		//We look for the blocks to and from
		Block from = event.getFrom().getBlock(), to = event.getTo().getBlock();

		//Has left the station!
		if(stationArea.containsKey(from) && !stationArea.containsKey(to)) {
			String station = stationArea.get(from);
			ExitTrainStationEvent exit = new ExitTrainStationEvent(stationStore.get(station), event.getPlayer());
			Bukkit.getServer().getPluginManager().callEvent(exit);
		}

		//Has entered the station!
		else if(stationArea.containsKey(to) && !stationArea.containsKey(from)) {
			String station = stationArea.get(to);
			EnterTrainStationEvent enter = new EnterTrainStationEvent(stationStore.get(station), event.getPlayer());
			Bukkit.getServer().getPluginManager().callEvent(enter);
		}
	}

	@EventHandler
	public void onTrainMove(MemberBlockChangeEvent event) {
		Block to = event.getTo(), from = event.getFrom();
		MinecartGroup mg = event.getGroup();
		MinecartMember<?> cart = event.getMember();
		MinecartMemberFurnace furnace = null;
		if(cart instanceof MinecartMemberFurnace) {
			furnace = (MinecartMemberFurnace) cart;
		}

		//We don't care about non-furnaces. Furnaces lead the charge!
		if(furnace == null) return;

		if(stationArea.containsKey(to)) {
			if(!stationArea.containsKey(from)) {
				//Entering station
				String station = stationArea.get(to);
				TrainEnterStationEvent enter = new TrainEnterStationEvent(stationStore.get(station), event.getGroup());
				Bukkit.getServer().getPluginManager().callEvent(enter);	
			}

			//The train has hit the stop block, and needs to stop.
			else if(stationStore.get(stationArea.get(to)).getStopBlocks().contains(to)) {
				String station = stationArea.get(to);
				//Check to see if the train has been stopped. if so, don't call the event.
				if(hasStopped.containsKey(mg)) if(hasStopped.get(mg)) return;
				TrainFullStopEvent stop = new TrainFullStopEvent(stationStore.get(station), event.getGroup());
				Bukkit.getServer().getPluginManager().callEvent(stop);
			}
		}
		//Leaving station
		else if(!stationArea.containsKey(to) && stationArea.containsKey(from)) {
			String station = stationArea.get(from);
			TrainExitStationEvent exit = new TrainExitStationEvent(stationStore.get(station), event.getGroup());
			Bukkit.getServer().getPluginManager().callEvent(exit);
		}
	}

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
		if(!event.getPlayer().hasPermission("goldrushmc.station.signs")) { event.getPlayer().sendMessage(ChatColor.RED + "You don't have permission to use signs!"); return; }
		//We don't want the player holding anything in their hand!
		if(event.getItem() != null) { event.getPlayer().sendMessage("Please put away your things before using signs!"); return; }

		//Collect Player and Sign instances.
		Player p = event.getPlayer();
		Sign sign = (Sign) bs;


		//If the player is within a station, we need to throw an event to handle this sign click.
		if(stationArea.containsKey(p.getLocation().getBlock())) {
			StationSignEvent sEvent = new StationSignEvent(stationStore.get(stationArea.get(p.getLocation().getBlock())), sign, p);
			Bukkit.getServer().getPluginManager().callEvent(sEvent);
		}
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
		switch(type) {
		case ADD_STORAGE_CART: station.buyCart(player, EntityType.MINECART_CHEST); break;
		case ADD_RIDE_CART: station.buyCart(player, EntityType.MINECART); break;
		case REMOVE_STORAGE_CART: station.sellCart(player, EntityType.MINECART_CHEST); break;
		case REMOVE_RIDE_CART: station.sellCart(player, EntityType.MINECART); break;
		case FIX_BRIDGE: //TODO This will deal with fixing the broken rail pathways. NO logic exists yet for path finding.
		default: break; //If it is none of these, we do not care right now.
		}

	}

	@EventHandler
	public void onEntrance(EnterTrainStationEvent event) {
		TrainStation station = event.getTrainStation();
		Player p = event.getPlayer();
		p.sendMessage("Welcome to " + station.getStationName() + "!");
		if(!station.getVisitors().contains(p)) {
			station.addVisitor(p);
		}
	}

	@EventHandler
	public void onExit(ExitTrainStationEvent event) {
		TrainStation station = event.getTrainStation();
		Player p = event.getPlayer();
		p.sendMessage(station.getStationName() + " wishes you a Goodbye..");
		if(station.getVisitors().contains(p)) {
			station.removeVisitor(p);
		}
	}

	@EventHandler
	public void onTrainDepart(TrainExitStationEvent event) {
		//Train is removed from Train list after it departs. no need to remove it again.
		//		Bukkit.broadcastMessage(event.getTrain().getProperties().getTrainName() + " has left the station " + event.getTrainStation().getStationName());
		hasStopped.put(event.getTrain(), false);
	}

	@EventHandler
	public void onTrainArrive(TrainEnterStationEvent event) {
		MinecartGroup mg = event.getTrain();
		TrainStation station = event.getTrainStation();
		mg.getProperties().setSpeedLimit(0.2);
		mg.getProperties().setColliding(true);
		station.addTrain(mg);
		//		Bukkit.broadcastMessage(mg.getProperties().getTrainName() + " has entered the station " + event.getTrainStation().getStationName());
		hasStopped.put(event.getTrain(), false);

	}

	@EventHandler
	public void onTrainNextToDepart(TrainFullStopEvent event) {
		TrainStation station = event.getTrainStation();
		MinecartGroup train = event.getTrain();
		Block toStop = null;
		//TODO Starting to get a little non-polymorphic. Need to rework stopblocks, or just make many different TrainFullStopEvents.
		if(station instanceof TrainStationTransport) {
			toStop = ((TrainStationTransport) station).getMainStopBlock();
		}
		for(MinecartMember<?> mm : train) {
			if(mm instanceof MinecartMemberFurnace) {
				if(mm.getBlock().equals(toStop)) {
					train.getProperties().setSpeedLimit(0);
					station.setDepartingTrain(train);
					hasStopped.put(train, true);
				}
				else {
					train.getProperties().setSpeedLimit(0.1);
				}
			}
		}
		event.getTrainStation().changeSignLogic(event.getTrain().getProperties().getTrainName());
		event.getTrainStation().setDepartingTrain(train);
	}

	/**
	 * This will facilitate the need to update the {@link TrainFactory#ownerStorage} list, because if the inventory changes, the instance changes.
	 * 
	 * @param e The {@link InventoryMoveItemEvent} associated with the chest.
	 */
	@EventHandler
	public void onInventoryOpening(InventoryOpenEvent e) {
		Inventory inv = e.getInventory();
		if(e.getPlayer() instanceof Player) {
			Player p = (Player) e.getPlayer();
			if(inv.getHolder() instanceof Minecart) {
				Minecart cart = (Minecart) inv.getHolder();
				MinecartMember<?> toCheck = MinecartMemberStore.getAt(cart.getLocation());
				if(toCheck != null && !toCheck.isDerailed()) {
					CartProperties cp = toCheck.getProperties();
					if(!cp.getOwners().contains(p.getName().toLowerCase())) {
						p.sendMessage(ChatColor.RED + "You do not own this chest!");
						e.setCancelled(true);
					}
				}
			}
		}
	}

	/**
	 * Updates the specified station when a new sign is placed.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onSignPlacedWithin(BlockPlaceEvent event) {
		if(stationArea.containsKey(event.getBlock())) {
			TrainStation station = stationStore.get(stationArea.get(event.getBlock()));
			Sign sign = (Sign) event.getBlock().getState();
			//We only want signs that have the symbol {trains} denoted on them.
			if(!sign.getLine(0).equals("{trains}")) return;
			
			boolean isUseful = station.getSigns().addSign(sign.getLine(1), sign);
			
			if(isUseful) {
				
				String trainName = "N/A";
				int cartCount = 0;
				MinecartGroup train = station.getDepartingTrain();
				if(train != null) {
					trainName = train.getProperties().getTrainName();
					cartCount = train.size() - 1;	
				}
				
				SignType type = station.getSigns().getSignType(sign);
				switch(type) {
				case ADD_RIDE_CART:
				case ADD_STORAGE_CART:
				case REMOVE_RIDE_CART:
				case REMOVE_STORAGE_CART:
				case TRAIN_STATION:  {
					sign.setLine(2, trainName); 
					sign.update();
					event.getPlayer().sendMessage("You placed a " + type.toString() + " Sign!"); 
					break;
				}
				case TRAIN_STATION_CART_COUNT:  {
					String count = "N/A";
					if(cartCount != 0) {
						count = String.valueOf(cartCount);
					}
					sign.setLine(2, count); 
					event.getPlayer().sendMessage("You placed a Cart Counter Sign!"); 
					break; 
				}
				case TRAIN_STATION_TIME: break;
				default: break;
				}
			}


		}
	}

	public void populate() {
		if(!TrainStation.getTrainStations().isEmpty()) {
			for(TrainStation station : TrainStation.getTrainStations()) {
				stationStore.put(station.getStationName(), station);
				for(Block b : station.getTrainArea()) {
					stationArea.put(b, station.getStationName());
				}
			}
		}
	}
	public static void addStation(TrainStation station) {
		stationStore.put(station.getStationName(), station);
		for(Block b : station.getTrainArea()) {
			stationArea.put(b, station.getStationName());
		}
	}
	public static void removeStation(TrainStation station) {
		stationStore.remove(station.getStationName());
		for(Block b : station.getTrainArea()) {
			stationArea.remove(b);
		}
	}
}
