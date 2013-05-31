package com.goldrushmc.bukkit.train.station.npc;

import java.math.BigDecimal;

import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import com.goldrushmc.bukkit.train.station.TrainStation;

public class CartTradeable extends Trait {

	public CartTradeable() {
		super("CartTrader");
	}

	@Persist("gameTime") private long gameTime = 0;
	@Persist("hasCarts") private boolean hasCarts = false;
	private TrainStation station;
	@Persist("costPerCart") private int costOfCart = 5;
	@Persist("markDown") private int markDown = 2;
	private EntityType cartType;

	@Override
	public void run() {
	}

	@Override
	public void onAttach() {
		TrainStation station = TrainStation.getTrainStationAt(this.npc.getBukkitEntity().getLocation());
		if(station != null) {
			this.station = station;
			Bukkit.getLogger().info(this.getNPC().getFullName() + " has join station " + this.station.getStationName());
			hasCarts = this.station.hasCartsToSell();
			switch(station.getType()) {
			case DEFAULT: break;
			case PASSENGER_TRANS: this.cartType = EntityType.MINECART; break;
			case STORAGE_TRANS: this.cartType = EntityType.MINECART_CHEST; break;
			}
		}
	}

	/**
	 * Handles the buying and selling of carts
	 * @param e
	 */
	@EventHandler
	public void leftClick(NPCLeftClickEvent e) {
		//Checks to make sure the NPC is the same one as the attached one.
		if(this.getNPC() != e.getNPC()) return;
		if(!this.hasCarts) return;
		Player patron = e.getClicker();
		//Logic for Buying
		if(!patron.isSneaking()) {
			//Make sure they have gold nuggets in their inventory.
			if(patron.getInventory().contains(Material.GOLD_NUGGET)) {
				ItemStack[] contents = patron.getInventory().getContents();
				//Iterate through the contents of the player's inventory.
				for(int i = 0; i < contents.length; i++) {
					//If the item is null, skip it.
					if(contents[i] == null) continue;
					//If the item is gold nugget, try to sell a cart.
					if(contents[i].getType().equals(Material.GOLD_NUGGET)) {
						ItemStack goldHeld = contents[i];
						//If they have more than 5 gold, get
						if(goldHeld.getAmount() >= this.costOfCart) {
							int initial = goldHeld.getAmount();
							boolean success = this.station.buyCart(patron, cartType);
							if(success) { patron.getInventory().getItem(i).setAmount(initial - this.costOfCart); return;}

						} else {
							patron.sendMessage("You need: " + ChatColor.YELLOW + (this.costOfCart - goldHeld.getAmount()) + ChatColor.RESET + " more gold nuggets to buy a cart!");
							return;
						}
					}
				}
			} else {
				patron.sendMessage(ChatColor.RED + "You need gold nuggets to buy a cart!");
			}
		}
		//Shift + Left Clicking Sells Carts, at a lower price than bought.
		else if(patron.isSneaking()) {
			boolean success = this.station.sellCart(patron, cartType);
			if(success) {
				//Divide, but give the benefit to the seller. Round up.

				//Set final stack to the number found by dividing.
				patron.getInventory().addItem(new ItemStack[]{new ItemStack(Material.GOLD_NUGGET, (calcSell()))});
			}
		}
	}

	/**
	 * Calculate the amount to give back to customers.
	 * 
	 * @return
	 */
	private int calcSell() {
		BigDecimal base = new BigDecimal(this.costOfCart), percent = new BigDecimal(this.markDown);
		BigDecimal result = base.divide(percent, BigDecimal.ROUND_UP);

		return result.toBigInteger().intValue();
	}

	@EventHandler
	public void rightClick(NPCRightClickEvent e) {
		//Checks to make sure the NPC is the same one as the attached one.
		if(this.getNPC() != e.getNPC()) return;
		
		if(!this.hasCarts) return;
		//Shift + Right Click sells ALL CARTS for the player.
		if(e.getClicker().isSneaking()) {
			int moneyBack = 0;
			while(this.station.sellCart(e.getClicker(), this.cartType)) {
				moneyBack += calcSell();
			}
			if(moneyBack != 0) {
				e.getClicker().sendMessage("You recovered " + ChatColor.GREEN + moneyBack + ChatColor.YELLOW + " gold nugget(s)");
				return;
			}
			else {
				return;
			}
		}
		//Right clicking normally just gives the current cost of a cart.
		else {
			e.getClicker().sendMessage("The current cost of a cart is: " + ChatColor.GREEN + this.costOfCart + ChatColor.YELLOW + " nugget(s).");
		}

	}

}
