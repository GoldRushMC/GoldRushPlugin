package com.goldrushmc.bukkit.train.station;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberChest;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberFurnace;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberRideable;
import com.bergerkiller.bukkit.tc.events.MemberBlockChangeEvent;
import com.goldrushmc.bukkit.train.exceptions.StopBlockMismatchException;

/**
 * This is the station that is meant for TWO rail tracks. One is for public transport, the other is for goods.
 * <p>
 * This station class will be the most widely used, as it encompasses public and good transport, which is how the gold rush map was built...
 * 
 * @author Diremonsoon
 *
 */
public class CombinedTrainStation extends TrainStation {

	private Material stopMat;
	private final int maxStopBlocks = 8;
	private List<Block> stopBlocks;
	private Block mainPublicStop;
	private List<Block> publicStopBlocks;
	private List<Block> goodsStopBlocks;
	private Block mainGoodsStop;
	private volatile MinecartGroup publicTrain;
	private volatile MinecartGroup goodsTrain;

	public CombinedTrainStation(JavaPlugin plugin, String stationName, List<Location> markers, World world, boolean train) throws Exception {
		super(plugin, stationName, markers, world);

		this.stopMat = defaultStop;
		this.stopBlocks = findStopBlocks(this.stopMat);
		this.directions = new ArrayList<BlockFace>();
		if(this.stopBlocks.get(0).getData() == 0) { this.directions.add(BlockFace.NORTH); this.directions.add(BlockFace.SOUTH); }
		else if(this.stopBlocks.get(0).getData() == 1) { this.directions.add(BlockFace.EAST); this.directions.add(BlockFace.WEST); }
		for(Block b : this.stopBlocks) {
			if(this.stopBlocks.contains(b.getRelative(this.directions.get(0))) 
					&& this.stopBlocks.contains(b.getRelative(this.directions.get(1)))) {
				this.mainPublicStop = b;
				break;
			}
		}
		//Default to the first Block in the list of stopBlocks.
		if(this.mainPublicStop == null) this.mainPublicStop = this.stopBlocks.get(0);
		if(train) {
			if(this.rails != null) {
				if(this.stopBlocks.size() > maxStopBlocks) throw new StopBlockMismatchException();
				if(!findStillTrains()); //TODO
			}
		}
		add();
	}
	
	public Block findMaxDirBlock() {
		for(Block b : this.trainArea) {
			
		}
		return null;
	}
	
	public void findTrackTypes() {
		switch(this.directions.get(0)) {
		case NORTH:
		case SOUTH: {
			for(Block b : this.stopBlocks) {
				
			}
		}
		case WEST:
		case EAST: {
			
		}
		}
		
	}

	@Override
	public StationType getType() {
		return StationType.HYBRID_TRANS;
	}

	@Override
	public boolean buyCart(Player owner, EntityType type) {
		switch(type) {
		case MINECART: {
			if(this.publicTrain == null) { owner.sendMessage("There is no public train to buy from at this time."); return false; }
			for(MinecartMember<?> mm : this.publicTrain) {
				if(mm instanceof MinecartMemberFurnace) continue;
				if(mm instanceof MinecartMemberRideable) {
					if(mm.getProperties().getOwners().isEmpty()) {
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
			if(this.goodsTrain == null) { owner.sendMessage("There is no transport train to buy from at this time."); return false; }
			for(MinecartMember<?> mm : this.goodsTrain) {
				if(mm instanceof MinecartMemberFurnace) continue;
				if(mm instanceof MinecartMemberChest) {
					if(mm.getProperties().getOwners().isEmpty()) {
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
		default: return false;
		}
	}

	@Override
	public boolean sellCart(Player owner, EntityType type) {
		switch(type) {
		case MINECART: {
			if(this.publicTrain == null) { owner.sendMessage("There is no public train to sell from at this time."); return false; }
			for(MinecartMember<?> mm : this.publicTrain) {
				if(mm instanceof MinecartMemberFurnace) continue;
				if(mm instanceof MinecartMemberRideable) {
					if(!mm.getProperties().getOwners().isEmpty()) {
						if(mm.getProperties().getOwners().contains(owner.getName().toLowerCase())) {
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
			if(this.goodsTrain == null) { owner.sendMessage("There is no transport train to sell from at this time."); return false; }
			for(MinecartMember<?> mm : this.goodsTrain) {
				if(mm instanceof MinecartMemberFurnace) continue;
				if(mm instanceof MinecartMemberChest) {
					if(!mm.getProperties().getOwners().isEmpty()) {
						if(mm.getProperties().getOwners().contains(owner.getName().toLowerCase())) {
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
		default: return false;
		}
	}

	@Override
	protected void createWorkers() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean findStillTrains() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasCartsToSell() {
		for(MinecartMember<?> mm : this.goodsTrain) {
			if(mm.getProperties().getOwners().isEmpty()) return true;
		}
		for(MinecartMember<?> mm : this.publicTrain) {
			if(mm.getProperties().getOwners().isEmpty()) return true;
		}
		return false;
	}

	@Override
	public boolean hasDepartingTrain() {
		if(this.publicTrain == null && this.goodsTrain == null) return false;
		return true;
	}

	@Override
	public boolean pushQueue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void changeSignLogic(String trainName) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Block> findStopBlocks(Material m) {
		List<Block> stopBlocks = new ArrayList<Block>();
		for(Block b : this.trainArea) {
			if(this.stopMat.equals(b.getType())) {
				stopBlocks.add(b);
			}
		}
		return stopBlocks;
	}

	@Override
	public List<Block> getStopBlocks() {
		return stopBlocks;
	}

	@Override
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	@EventHandler
	public void onTrainMove(MemberBlockChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	@EventHandler
	public void onSignPlacedWithin(BlockPlaceEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<MinecartGroup> getDepartingTrains() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addDepartingTrain(MinecartGroup train) {
		// TODO Auto-generated method stub

	}

}
