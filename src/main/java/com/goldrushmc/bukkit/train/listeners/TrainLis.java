package com.goldrushmc.bukkit.train.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.events.GroupLinkEvent;
import com.goldrushmc.bukkit.defaults.DefaultListener;

public class TrainLis extends DefaultListener {

	public TrainLis(JavaPlugin plugin) {
		super(plugin);
	}

	/**
	 * Checks to make sure that the groups connecting are of the same train, and if they aren't, cancel the event.
	 * 
	 * @param event The {@link GroupLinkEvent} in question.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCartLink(GroupLinkEvent event) {

		//Cancelled by default
		event.setCancelled(true);

		MinecartGroup mg1 = event.getGroup1(), mg2 = event.getGroup2();
		String train1 = mg1.getProperties().getTrainName(), train2 = mg2.getProperties().getTrainName();

		if(train1.equals(train2)) {
			event.setCancelled(false);
		}
	}
	
//	@EventHandler
//	public void onCartDerailed(MemberBlockChangeEvent event) {
//		if(event.getMember().isDerailed()) {
//			for(MinecartMember<?> mm : event.getMember().getGroup()) {
//				mm.getProperties().getOwners().clear();
//			}
//		}
//	}

	/*
	/**
	 *  THIS EVENT IS NOT BEING USED AT THE MOMENT. WE NEED TO FIGURE OUT WHAT TO DO WITH BROKEN CARTS
	 * @param event

	//	@EventHandler(priority = EventPriority.MONITOR)
	public void onGroupBreak(GroupRemoveEvent event) {
		MinecartGroup mg = event.getGroup();
		String train = event.getGroup().getProperties().getTrainName();
		if(TrainFactory.trains.containsKey(train)) {
			TrainFactory.trains.remove(train);
		}

		for(MinecartMember<?> mm : mg) {			
			//Determine class type, and try to remove the minecart or inventory from the appropriate list.
			try {
				MinecartMemberRideable mmr = (MinecartMemberRideable) mm;
				for(List<MinecartMemberRideable> m : TrainFactory.ownerRideable.values()) {
					if(m.contains(mmr)) {
						m.remove(mmr);
						return;
					}
				}
			} catch (ClassCastException e) {
				try {
					MinecartMemberChest mmc = (MinecartMemberChest) mm;
					for(List<MinecartMemberChest> list : TrainFactory.ownerStorage.values()) {
						if(list.contains(mmc)) {
							list.remove(mmc);
							return;
						}
					}
				} catch (ClassCastException c) {}
			}
		}
	}

	/**
	 *  THIS EVENT IS NOT BEING USED AT THE MOMENT. WE NEED TO FIGURE OUT WHAT TO DO WITH BROKEN CARTS
	 * @param event
	 *
	//	@EventHandler(priority = EventPriority.MONITOR)
	public void onCartBreak(MemberRemoveEvent event) {
		MinecartMember<?> mm = event.getMember();
		String train = event.getGroup().getProperties().getTrainName();
		if(TrainFactory.trains.containsKey(train)) {
			TrainFactory.trains.remove(train);
		}

		//Determine class type, and try to remove the minecart or inventory from the appropriate list.
		try {
			MinecartMemberRideable mmr = (MinecartMemberRideable) mm;
			for(List<MinecartMemberRideable> m : TrainFactory.ownerRideable.values()) {
				if(m.contains(mmr)) {
					m.remove(mmr);
					return;
				}
			}
		} catch (ClassCastException e) {
			//If the first cast fails, we try the chest minecart cast.
			try {
				MinecartMemberChest mmc = (MinecartMemberChest) mm;
				for(List<MinecartMemberChest> list : TrainFactory.ownerStorage.values()) {
					//If the minecart is contained within the list, remove it from the list.
					if(list.contains(mmc)) {
						list.remove(mmc);
						return;
					}
				}
			} catch (ClassCastException c) {}
		}
	}

	 */

	
//	/**
//	 * Handles the interaction of each player, determines what method should be called in each case.
//	 * 
//	 * @param event The {@link PlayerInteractEvent} called.
//	 */
//	//	@EventHandler
//	public void onInteraction(PlayerInteractEvent event) {
//		Block block = event.getClickedBlock();
//
//		//If block is null, fail silently.
//		if(block == null) return;
//		//If the block type is a rail, we pass it to the method in charge of rail clicking.
//		//		if(TrainTools.isRail(block)) onRailClick(event);
//	}

	/*
	/**
	 * Controls how the player sets coordinates for the creation of trains.
	 * 
	 * @param event The {@link PlayerInteractEvent} called.
	 /	
	public void onRailClick(PlayerInteractEvent event) {

		Block block = event.getClickedBlock();

		/*The tool of choice is the blaze rod.
	 * We check to make sure the blaze rod has the appropriate itemmeta added to it.
	 *
		if(event.getItem() == null) return;
		if(!event.getItem().getItemMeta().hasLore()) return;
		ItemStack item = event.getItem();
		List<String> lore = item.getItemMeta().getLore();
		boolean isRod = false;
		for(String s : lore) {
			if(s.equalsIgnoreCase("TrainTool")) {
				isRod = true;
			}
		}

		if(isRod) {
			//Check the block type. If it isn't a rail, we don't store the value.
			if(TrainTools.isRail(block)) {

				Player p = event.getPlayer();

				//Will determine what slot in the array to put the location.
				int store = 3;

				//Checking what type of action it was
				if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
					p.sendMessage("You stored the first value!");
					store = 0;
				}
				else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					p.sendMessage("You stored the second value!");
					store = 1;
				}

				//If this is not changed, it was not a relevant action.
				if(store == 3) return;

				//Store the player's selected location in the selections map.
				if(TrainFactory.selections.containsKey(p)) {
					TrainFactory.selections.get(p)[store] = block.getLocation();
				}
				//If the player does not have selections, instantiate one and put the variables in.
				else {
					Location[] loc = new Location[2];
					loc[store] = block.getLocation();
					TrainFactory.selections.put(p, loc);
				}
			}
		}
	}
	 */




}
