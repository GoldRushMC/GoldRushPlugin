package com.goldrushmc.bukkit.train.station;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberChest;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberRideable;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.goldrushmc.bukkit.train.SmallBlockMap;
import com.goldrushmc.bukkit.train.exceptions.StopBlockMismatchException;

public class TrainStationTransport extends TrainStation {

	private final int maxStopBlocks = 4;
	private final Material stopMat;
	private final List<Block> stopBlocks;
	private Block mainStop;
	//	private final List<Chest> lockers;
	//	private Map<Player, Chest> lockerPlayerMap = new HashMap<Player, Chest>();

	public TrainStationTransport(JavaPlugin plugin, String stationName,	List<Location> markers, World world, boolean train) throws Exception {
		super(plugin, stationName, markers, world);
		this.stopMat = defaultStop;
		this.stopBlocks = findStopBlocks(this.stopMat);
		for(Block b : this.stopBlocks) {
			if(b.getRelative(this.direction).getRelative(BlockFace.DOWN).getType().equals(this.stopMat) 
					&& b.getRelative(this.direction.getOppositeFace()).getRelative(BlockFace.DOWN).getType().equals(this.stopMat)) {
				this.mainStop = b;
				break;
			}
		}
		//Default to the first Block in the list of stopBlocks.
		if(this.mainStop == null) this.mainStop = this.stopBlocks.get(0);
		if(train) {
			if(this.rails != null) {
				if(this.stopBlocks.size() > maxStopBlocks) throw new StopBlockMismatchException();
				createTransport();	
			}
		}
		//		this.lockers = findLockers();
	}

	public TrainStationTransport(JavaPlugin plugin, String stationName,	List<Location> markers, World world, Material stopMat, boolean train) throws Exception {
		super(plugin, stationName, markers, world, stopMat);
		this.stopMat = stopMat;
		this.stopBlocks = findStopBlocks(stopMat);
		for(Block b : this.stopBlocks) {
			if(b.getRelative(this.direction).getRelative(BlockFace.DOWN).getType().equals(stopMat) 
					&& b.getRelative(this.direction.getOppositeFace()).getRelative(BlockFace.DOWN).getType().equals(stopMat)) {
				this.mainStop = b;
				break;
			}
		}
		//Default to the first Block in the list of stopBlocks.
		if(this.mainStop == null) this.mainStop = this.stopBlocks.get(0);
		if(train) {
			if(this.rails != null) {
				if(this.stopBlocks.size() > maxStopBlocks) throw new StopBlockMismatchException();
				createTransport();	
			}
		}
		//		this.lockers = findLockers();
	}

	/**
	 * A way to find all of the lockers in the selectedArea.
	 * 
	 * @return
	 */
	protected List<Chest> findLockers() {
		List<Chest> lockers = new ArrayList<Chest>();
		for(Block b : this.trainArea) {
			if(b.getType().equals(Material.CHEST)) {
				if(b.getState() instanceof Chest) {
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
	public void buyCart(Player owner, EntityType type) {
		if(this.departingTrain == null) return;
		//The boolean success will be set true if a minecart is actually bought.
		boolean success = false;
		for(MinecartMember<?> cart : this.departingTrain) {
			//If the cart is an instance of the furnace, skip it.
			if(cart instanceof MinecartMemberFurnace) continue;
			switch(type) {
			//If the player wants a passenger minecart, do this.
			case MINECART: { 
				if(cart instanceof MinecartMemberRideable) {
					//If the owners list is empty, set the owner!
					if(cart.getProperties().getOwners().isEmpty()) {
						cart.getProperties().setOwner(owner);
						success = true;
						break;
					}
				} else {break;}
			}
			//If the player wants a chest minecart, do this.
			case MINECART_CHEST: { 
				if(cart instanceof MinecartMemberChest) {
					//If the owners list is empty, set the owner!
					if(cart.getProperties().getOwners().isEmpty()) {
						cart.getProperties().setOwner(owner);
						success = true;
						break;
					}
				} else {break;}
			}
			//This should never happen, but if it does, oh well.
			default: break;
			}
			//If we have already gotten a cart, end the loop.
			if(success) {
				//If the buy was successful, send a confirmation.
				switch(type) {
				case MINECART: owner.sendMessage("You bought" + ChatColor.BLUE + " passenger cart" + ChatColor.GREEN + " [#" + (cart.getIndex() + 1) + "]"); break;
				case MINECART_CHEST: owner.sendMessage("You bought" + ChatColor.BLUE + " storage cart" + ChatColor.GREEN + " [#" + (cart.getIndex() + 1) + "]"); break;
				default: break;
				}
				this.updateCartsAvailable(this.departingTrain, type);
				break;
			}
		}
		//If the buy wasn't successful, send a message.
		if(!success) {
			switch(type) {
			case MINECART: owner.sendMessage("There are no" + ChatColor.BLUE + " passenger cart(s) " + ChatColor.RESET + "for sale..."); break;
			case MINECART_CHEST: owner.sendMessage("There are no" + ChatColor.BLUE + " storage cart(s) " + ChatColor.RESET + "for sale..."); break;
			default: break;
			}
		}

	}

	@Override
	public void sellCart(Player owner, EntityType type) {
		if(this.departingTrain == null) return;
		//The boolean success will be set true if a minecart is actually bought.
		boolean success = false;
		for(MinecartMember<?> cart : this.departingTrain) {
			//If the cart is an instance of the furnace, skip it.
			if(cart instanceof MinecartMemberFurnace) continue;
			switch(type) {
			//If the player wants a passenger minecart, do this.
			case MINECART: if(cart instanceof MinecartMemberRideable) {
				//If the owners list is not empty, and the owner is the player, remove them!
				if(!cart.getProperties().getOwners().isEmpty()) {
					if(cart.getProperties().getOwners().contains(owner.getName().toLowerCase())) {
						cart.getProperties().getOwners().remove(owner.getName().toLowerCase());
						success = true;
						break;	
					}
				}
			} else {break;}
			//If the player wants a chest minecart, do this.
			case MINECART_CHEST: if(cart instanceof MinecartMemberChest) {
				//If the owners list is not empty, and the owner is the player, remove them!
				if(!cart.getProperties().getOwners().isEmpty()) {
					if(cart.getProperties().getOwners().contains(owner.getName().toLowerCase())) {
						cart.getProperties().getOwners().remove(owner.getName().toLowerCase());
						success = true;
						break;	
					}
				} 
			} else {break;}
			//This should never happen, but if it does, oh well.
			default: break;
			}
			if(success) {
				//If the sell was successful, send a confirmation.
				switch(type) {
				case MINECART: owner.sendMessage("You sold" + ChatColor.BLUE + " passenger cart" + ChatColor.GREEN + " [#" + (cart.getIndex() + 1) + "]"); break;
				case MINECART_CHEST: owner.sendMessage("You sold" + ChatColor.BLUE + " storage cart" + ChatColor.GREEN + " [#" + (cart.getIndex() + 1) + "]"); break;
				default: break;
				}
				this.updateCartsAvailable(this.departingTrain, type);
				break;
			}
		}
		//If the sell wasn't successful, send a message.
		if(!success) { 
			switch(type) {
			case MINECART: owner.sendMessage("You have no" + ChatColor.BLUE + " passenger cart(s) " + ChatColor.RESET + "to sell..."); break;
			case MINECART_CHEST: owner.sendMessage("You have no" + ChatColor.BLUE + " storage cart(s) " + ChatColor.RESET + "to sell..."); break;
			default: break;
			}
		}
	}

	@Override
	public void createTransport() {

		if(this.trains == null) this.trains = new ArrayList<MinecartGroup>();
		int trainNum = this.trains.size() + 1;

		List<EntityType> carts = new ArrayList<EntityType>();
		SmallBlockMap sbm = new SmallBlockMap(this.mainStop);
		BlockFace dir = this.direction.getOppositeFace();
		//Iterate to get the max size of minecarts, or just short of it if the rails end.
		for(int i = 0; i < 14; i++) {
			sbm = new SmallBlockMap(sbm.getBlockAt(dir));
			if(!sbm.isRail(sbm.getBlockAt(dir))) {
				break;
			}
			carts.add(EntityType.MINECART_CHEST);
		}
		carts.add(EntityType.MINECART_FURNACE);
		//Should make the furnace spawn right on top of the stop block.
		MinecartGroup train = MinecartGroup.spawn(this.mainStop, this.direction.getOppositeFace(), carts);
		for(MinecartMember<?> mm : train) {
			if(mm instanceof MinecartMemberChest) {
				//				ItemStack coal = new ItemStack(Material.COAL, 64);
				//				MinecartMemberChest coalChest = (MinecartMemberChest) mm;
				//			coalChest.getEntity().getInventory().addItem(new ItemStack[]{coal, coal, coal, coal, coal, coal});			
			}
			if(mm instanceof MinecartMemberFurnace) {
			}
		}

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
		this.findNextDeparture();
		this.changeSignLogic(train.getProperties().getTrainName());
		this.updateCartsAvailable(train, EntityType.MINECART_CHEST);
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
	public MinecartGroup findNextDeparture() {
		for(MinecartGroup mg : this.trains) {
			for(MinecartMember<?> mm : mg) {
				if(mm instanceof MinecartMemberFurnace) {
					if(this.stopBlocks.contains(mm.getBlock())) {
						return mg;
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean pushQueue() {
		MinecartGroup mg = this.departingTrain;
		if(mg == null) return false;
		mg.getProperties().setSpeedLimit(0.4);
		mg.getProperties().setColliding(false);
		for(MinecartMember<?> mm : mg) {
			if(mm instanceof MinecartMemberFurnace) {
				MinecartMemberFurnace power = (MinecartMemberFurnace) mm;
				power.addFuelTicks(1000);
				if(!power.getDirection().equals(this.direction)) {
					mg.reverse();
				}
			}
		}
		this.trains.remove(mg);
		if(this.trains.isEmpty()) return true;
		for(MinecartGroup train : this.trains) {
			train.setForwardForce(0.4);
		}
		return true;
	}
	
	@Override
	public void createWorkers() {
	}

	@Override
	public void changeSignLogic(String trainName) {
		this.signs.updateTrain(trainName);
	}

	public Block getMainStopBlock() {
		return this.mainStop;
	}
}
