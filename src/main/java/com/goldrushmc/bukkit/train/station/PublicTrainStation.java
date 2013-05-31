package com.goldrushmc.bukkit.train.station;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberRideable;
import com.bergerkiller.bukkit.tc.events.MemberBlockChangeEvent;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.goldrushmc.bukkit.train.SmallBlockMap;
import com.goldrushmc.bukkit.train.event.EnterTrainStationEvent;
import com.goldrushmc.bukkit.train.event.ExitTrainStationEvent;
import com.goldrushmc.bukkit.train.event.TrainEnterStationEvent;
import com.goldrushmc.bukkit.train.event.TrainExitStationEvent;
import com.goldrushmc.bukkit.train.event.TrainFullStopEvent;
import com.goldrushmc.bukkit.train.exceptions.StopBlockMismatchException;
import com.goldrushmc.bukkit.train.signs.SignType;
import com.goldrushmc.bukkit.train.station.npc.CartTradeable;

public class PublicTrainStation extends TrainStation {

	private final int maxStopBlocks = 4;
	private final Material stopMat;
	private final List<Block> stopBlocks;
	private Block mainStop;

	public PublicTrainStation(JavaPlugin plugin, String stationName, List<Location> markers, World world, boolean train) throws Exception {
		super(plugin, stationName, markers, world);

		this.stopMat = defaultStop;
		this.stopBlocks = findStopBlocks(this.stopMat);
		this.directions = new ArrayList<BlockFace>();
		if(this.stopBlocks.get(0).getData() == 0) { this.directions.add(BlockFace.NORTH); this.directions.add(BlockFace.SOUTH); }
		else if(this.stopBlocks.get(0).getData() == 1) { this.directions.add(BlockFace.EAST); this.directions.add(BlockFace.WEST); }
		for(Block b : this.stopBlocks) {
			if(this.stopBlocks.contains(b.getRelative(this.directions.get(0))) 
			&& this.stopBlocks.contains(b.getRelative(this.directions.get(1)))) {
				this.mainStop = b;
				break;
			}
		}
		if(train) {
			if(this.rails != null) {
				if(this.stopBlocks.size() > maxStopBlocks) throw new StopBlockMismatchException();
				if(!findStillTrains()) createTransport();	
			}
		}
		add();
	}

	public PublicTrainStation(JavaPlugin plugin, String stationName, List<Location> markers, World world, Material stopMat, boolean train) throws Exception {
		super(plugin, stationName, markers, world, stopMat);

		this.stopMat = stopMat;
		this.stopBlocks = findStopBlocks(stopMat);
		this.directions = new ArrayList<BlockFace>();
		if(this.stopBlocks.get(0).getData() == 0) { this.directions.add(BlockFace.NORTH); this.directions.add(BlockFace.SOUTH); }
		else if(this.stopBlocks.get(0).getData() == 1) { this.directions.add(BlockFace.EAST); this.directions.add(BlockFace.WEST); }
		for(Block b : this.stopBlocks) {
			if(this.stopBlocks.contains(b.getRelative(this.directions.get(0))) 
			&& this.stopBlocks.contains(b.getRelative(this.directions.get(1)))) {
				this.mainStop = b;
				break;
			}
		}
		if(train) {
			if(this.rails != null) {
				if(this.stopBlocks.size() > maxStopBlocks) throw new StopBlockMismatchException();
				if(!findStillTrains()) createTransport();
			}
		}
		add();
	}

	@Override
	public StationType getType() {
		return StationType.PASSENGER_TRANS;
	}

	@Override
	public boolean buyCart(Player owner, EntityType type) {
		if(!type.equals(EntityType.MINECART)) return false;
		if(this.departingTrain == null) { owner.sendMessage("There is currently no train available to buy from. "); return false; }

		//The boolean success will be set true if a minecart is actually bought.
		boolean success = false;
		for(MinecartMember<?> cart : this.departingTrain) {
			//If the cart is an instance of the furnace, skip it.
			if(cart instanceof MinecartMemberFurnace) continue;
			//If the player wants a passenger minecart, do this.
			if(cart instanceof MinecartMemberRideable) {
				//If the owners list is empty, set the owner!
				if(cart.getProperties().getOwners().isEmpty()) {
					cart.getProperties().setOwner(owner);
					success = true;
				}
				//If we have already gotten a cart, end the loop.
				if(success) {
					//If the buy was successful, send a confirmation.
					owner.sendMessage("You bought" + ChatColor.BLUE + " passenger cart" + ChatColor.GREEN + " [#" + (cart.getIndex() + 1) + "]"
							+ " from train " + this.departingTrain.getProperties().getTrainName());
					this.updateCartsAvailable(this.departingTrain, type);
					return true;
				}
			}
		}
		//If the buy wasn't successful, send a message.
		if(!success) {
			owner.sendMessage("There are no" + ChatColor.BLUE + " passenger cart(s) " + ChatColor.RESET + "for sale"
					+ " on train " + this.departingTrain.getProperties().getTrainName());
		}
		return false;
	}

	@Override
	public boolean sellCart(Player owner, EntityType type) {
		if(!type.equals(EntityType.MINECART)) return false;
		if(this.departingTrain == null) { owner.sendMessage("There is currently no train available to sell from. "); return false; }

		//The boolean success will be set true if a minecart is actually bought.
		boolean success = false;
		for(MinecartMember<?> cart : this.departingTrain) {
			//If the cart is an instance of the furnace, skip it.
			if(cart instanceof MinecartMemberFurnace) continue;
			//If the player wants a passenger minecart, do this.
			if(cart instanceof MinecartMemberRideable) {
				//If the owners list is not empty, and the owner is the player, remove them!
				if(!cart.getProperties().getOwners().isEmpty()) {
					if(cart.getProperties().getOwners().contains(owner.getName().toLowerCase())) {
						cart.getProperties().getOwners().remove(owner.getName().toLowerCase());
						success = true;
					}
				}
			} 
			if(success) {
				//If the sell was successful, send a confirmation.
				owner.sendMessage("You sold" + ChatColor.BLUE + " passenger cart" + ChatColor.GREEN + " [#" + (cart.getIndex() + 1) + "]");
				this.updateCartsAvailable(this.departingTrain, type);
				return true;
			}
		}
		//If the sell wasn't successful, send a message.
		if(!success) { 
			owner.sendMessage("You have no" + ChatColor.BLUE + " passenger cart(s) " + ChatColor.RESET + "to sell...");
		}
		return false;
	}

	/**
	 * Will generate a Public Transportation type train. All rideable minecarts.
	 */
	public void createTransport() {

		if(this.trains == null) this.trains = new ArrayList<MinecartGroup>();
		int trainNum = this.trains.size() + 1;

		List<EntityType> carts = new ArrayList<EntityType>();
		SmallBlockMap sbm = new SmallBlockMap(this.mainStop);
		BlockFace dir = this.directions.get(0).getOppositeFace(); //TODO
		//Iterate to get the max size of minecarts, or just short of it if the rails end.
		int i = 0;
		while(!sbm.isEnd() || i < 14) {
			sbm = new SmallBlockMap(sbm.getBlockAt(dir));
			if(!sbm.isRail(sbm.getBlockAt(dir))) {
				break;
			}
			carts.add(EntityType.MINECART);
			i++;
		}
		carts.add(EntityType.MINECART_FURNACE);

		//Should make the furnace spawn right on top of the stop block.
		MinecartGroup train = MinecartGroup.spawn(this.mainStop, this.directions.get(0).getOppositeFace(), carts); //TODO

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
		if(this.departingTrain == null) this.departingTrain = train;
		this.changeSignLogic(train.getProperties().getTrainName());
		this.updateCartsAvailable(train, EntityType.MINECART);
		this.hasStopped.put(train, true);
	}

	/**
	 * Find the stop blocks for a typical transport station.
	 * <p>
	 * Assumption:
	 * <li> There are a max of 4 stop blocks, all of which are on the same length of track, all clumped together.
	 * <li> The blocks will be ordered as such:
	 * <ol>
	 * <li> 0 : The block which the train shall spawn on (furthest in the train station's designated direction).
	 * <li> 1, 2, 3 : The other blocks which will support the departure function.
	 * 
	 * 
	 */
	@Override
	public List<Block> findStopBlocks(Material m) {
		List<Block> stopBlocks = new ArrayList<Block>();
		for(Block b : this.trainArea) {
			if(b.getType().equals(m)) {
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
		MinecartGroup mg = this.departingTrain;
		if(mg == null) return false;
		mg.getProperties().setSpeedLimit(0.6);
		mg.getProperties().setColliding(false);
		for(MinecartMember<?> mm : mg) {
			if(mm instanceof MinecartMemberFurnace) {
				MinecartMemberFurnace power = (MinecartMemberFurnace) mm;
				power.addFuelTicks(1000);
				if(!power.getDirection().equals(this.directions.get(0))) { //TODO
					mg.reverse();
				}
			}
		}
		this.trains.remove(mg);
		this.departingTrain = null;
		return true;
	}

	@Override
	public void createWorkers() {
		for(Chunk c : this.chunks) {
			Entity[] entities = c.getEntities();
			for(int i = 0; i < entities.length; i++) {
				if(CitizensAPI.getNPCRegistry().isNPC(entities[i])) {
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
		if(this.departingTrain == null) return false;
		else return true;
	}

	@Override
	public boolean hasCartsToSell() {
		if(!hasDepartingTrain()) return false;

		for(MinecartMember<?> mm : this.departingTrain) {
			if(mm instanceof MinecartMemberRideable) {
				if(mm.getProperties().getOwners().isEmpty()) return true;
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
		if(cart instanceof MinecartMemberFurnace) {
			furnace = (MinecartMemberFurnace) cart;
		}

		//We don't care about non-furnaces. Furnaces lead the charge!
		if(furnace == null) return;

		if(this.trainArea.contains(to)) {
			if(!this.trainArea.contains(from)) {
				//Entering station
				TrainEnterStationEvent enter = new TrainEnterStationEvent(this, event.getGroup());
				Bukkit.getServer().getPluginManager().callEvent(enter);	
			}

			//The train has hit the stop block, and needs to stop.
			else if(getStopBlocks().contains(to)) {
				//Check to see if there is already a departing train assigned. If so, we don't need to add it again.
				if(this.departingTrain == null) {
					//If there is no departing train yet, we need to add this one.
					TrainFullStopEvent stop = new TrainFullStopEvent(this, event.getGroup());
					Bukkit.getServer().getPluginManager().callEvent(stop);
				}
			}
		}
		//Leaving station
		else if(!this.trainArea.contains(to) && this.trainArea.contains(from)) {
			TrainExitStationEvent exit = new TrainExitStationEvent(this, event.getGroup());
			Bukkit.getServer().getPluginManager().callEvent(exit);
		}
	}

	@Override
	@EventHandler
	public void onSignPlacedWithin(BlockPlaceEvent event) {
		if(this.trainArea.contains(event.getBlock())) {
			//If it isn't a sign, we don't care.
			if(!(event.getBlock().getState() instanceof Sign)) return;

			Sign sign = (Sign) event.getBlock().getState();
			//We only want signs that have the symbol {trains} denoted on them.
			if(!sign.getLine(0).equals("{trains}")) return;

			boolean isUseful = this.getSigns().addSign(sign.getLine(1), sign);

			if(isUseful) {

				String trainName = "N/A";
				int cartCount = 0;
				MinecartGroup train = this.getDepartingTrain();
				if(train != null) {
					trainName = train.getProperties().getTrainName();
					cartCount = train.size() - 1;	
				}

				SignType type = this.getSigns().getSignType(sign);
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

	@Override
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		//We look for the blocks to and from
		Block from = event.getFrom().getBlock(), to = event.getTo().getBlock();

		//Has left the station!
		if(this.trainArea.contains(from) && !this.trainArea.contains(to)) {
			ExitTrainStationEvent exit = new ExitTrainStationEvent(this, event.getPlayer());
			Bukkit.getServer().getPluginManager().callEvent(exit);
		}

		//Has entered the station!
		else if(this.trainArea.contains(to) && !this.trainArea.contains(from)) {
			EnterTrainStationEvent enter = new EnterTrainStationEvent(this, event.getPlayer());
			Bukkit.getServer().getPluginManager().callEvent(enter);
		}

	}

	@Override
	public boolean findStillTrains() {
		MinecartGroup[] trains = MinecartGroup.getGroups();
		for(int i = 0; i < trains.length; i++) {
			if(!trains[i].isMoving()) {
				//At least the front of the train must be WITHIN the station.
				for(MinecartMember<?> mm : trains[i]) {

					boolean foundTrain = false;
					if(!(mm instanceof MinecartMemberFurnace)) continue;

					if(this.stopBlocks.contains(mm.getBlock())) {
						this.departingTrain = trains[i];
						foundTrain = true;
						Bukkit.getLogger().info("Found a still train! Added to departing.");
						break;
					} else {
						for(Block b : this.trainArea) {
							//If the train is within the station grounds, accept it.
							if(b.equals(mm.getBlock())) {
								this.trains.add(trains[i]);
								foundTrain = true;
								Bukkit.getLogger().info("Found a still train! Added to regular list.");
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
}
