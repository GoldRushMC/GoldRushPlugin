package com.goldrushmc.bukkit.train.station.npc;

import com.goldrushmc.bukkit.train.station.TrainStation;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartTradeable extends Trait {

<<<<<<< HEAD
    public CartTradeable() {
        super("CartTrader");
    }

    @Persist("gameTime")
    private long gameTime = 0;
    @Persist("hasCarts")
    private boolean hasCarts = false;
    private TrainStation station;
    @Persist("costPerPublicCart")
    private int costOfPublicCart = 5;
    @Persist("costPerGoodsCart")
    private int costOfGoodsCart = 6;
    @Persist("markDown")
    private int markDown = 2;
    private EntityType cartType;

    @Override
    public void run() {

        if(!getNPC().isSpawned()) return;

        //Every 200 ticks, we update the price.
        long update = getNPC().getBukkitEntity().getWorld().getTime();
        if(update - gameTime == 200) {
            gameTime = update;
        }
    }

    @Override
    public void onAttach() {
        TrainStation station = TrainStation.getTrainStationAt(this.npc.getBukkitEntity().getLocation());
        if (station != null) {
            this.station = station;
            hasCarts = station.hasCartsToSell();
            switch (station.getType()) {
                case DEFAULT:
                    break;
                case PASSENGER_TRANS:
                    this.cartType = EntityType.MINECART;
                    break;
                case STORAGE_TRANS:
                    this.cartType = EntityType.MINECART_CHEST;
                    break;
                case HYBRID_TRANS:
                    this.cartType = EntityType.MINECART_HOPPER;
            }
        }
    }

    /**
     * Handles the buying and selling of carts
     *
     * @param e
     */
    @EventHandler
    public void leftClick(NPCLeftClickEvent e) {
        //Checks to make sure the NPC is the same one as the attached one.
        if (this.getNPC() != e.getNPC()) return;
        if (!this.hasCarts) return;
        Player patron = e.getClicker();

        //Logic for Buying Goods Carts
        if (!patron.isSneaking()) {
            List<ItemStack> golds = getGoldForPlayer(patron);
            if(!golds.isEmpty()) {
                int totalWorth = getFullWorth(golds);
                if(totalWorth >= costOfGoodsCart) {
                    if(station.buyCart(patron, EntityType.MINECART_CHEST)) {
                        //Organize the gold nuggets, so that we don't have to worry about people trolling and putting nuggets in awkward counts.
                        golds = organizeGold(golds);
                        for(ItemStack nugget : golds) {
                            if(nugget.getAmount() >= costOfGoodsCart) {
                                nugget.setAmount(nugget.getAmount() - costOfGoodsCart);
                                break;
                            }
                        }
                    }
                }
                else {
                    patron.sendMessage("You need " + (costOfGoodsCart - totalWorth) + " additional " + ChatColor.GOLD + " gold nugget(s)" + ChatColor.RESET + " to buy a Goods Cart.");
                }
            }
            else {
                patron.sendMessage("You need gold nuggets to buy carts!");
            }
        }
        //Shift + Left Clicking Sells Goods Carts, at a lower price than bought.
        else if (patron.isSneaking()) {
            if (station.sellCart(patron, EntityType.MINECART_CHEST)) {
                //Divide, but give the benefit to the seller. Round up.
                //Set final stack to the number found by dividing.
                patron.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, (calcSell(EntityType.MINECART_CHEST))));
            }
        }
    }

    private List<ItemStack> organizeGold(List<ItemStack> golds) {
        List<ItemStack> notMax = new ArrayList<>();
        //Iterate and find gold stacks that are NOT at 64.
        for(ItemStack gold : golds) {
            if(gold.getAmount() != gold.getMaxStackSize()) notMax.add(gold);
        }

        //Determine how many stacks to make.
        int amountOfWorth = 0;
        for(ItemStack gold : notMax) {
            amountOfWorth += gold.getAmount();
        }
        //Divide to get the amount we should make.
        int toMake = amountOfWorth / notMax.size();
        //Get leftovers.
        int leftOver = amountOfWorth % notMax.size();

        List<ItemStack> newStacks = new ArrayList<>();

        //Iterate and make the new stacks
        while(newStacks.size() != toMake) {
            newStacks.add(new ItemStack(Material.GOLD_NUGGET, 64));
        }
        //Add in the left-overs
        newStacks.add(new ItemStack(Material.GOLD_NUGGET, leftOver));

        return newStacks;
    }

    private List<ItemStack> getGoldForPlayer(Player patron) {

        List<ItemStack> golds = new ArrayList<>();

        for(ItemStack is : patron.getInventory().getContents()) {
            if(is.getType().equals(Material.GOLD_NUGGET)) {
                golds.add(is);
            }
        }

        return golds;
    }

//    private int getFullWorth(Player patron) {
//        List<ItemStack> golds = getGoldForPlayer(patron);
//
//        int total = 0;
//        for(ItemStack nugget : golds) total += nugget.getAmount();
//        return total;
//
//    }

    private int getFullWorth(List<ItemStack> golds) {
        int total = 0;
        for(ItemStack nugget : golds) total += nugget.getAmount();
        return total;

    }



    /**
     * Calculate the amount to give back to customers.
     *
     * @return  The amount to give back to the consumer
     */
    private int calcSell(EntityType type) {
        BigDecimal base = null, percent = new BigDecimal(markDown), result = null;
        switch (type) {
            case MINECART:
                base = new BigDecimal(costOfPublicCart);
                break;
            case MINECART_CHEST:
                base = new BigDecimal(costOfGoodsCart);
                break;
            //The default is just the cost of a public cart.
            default:
                base = new BigDecimal(costOfPublicCart);
                break;

        }
        result = base.divide(percent, BigDecimal.ROUND_UP);
        return result.toBigInteger().intValue();
    }

    @EventHandler
    public void rightClick(NPCRightClickEvent e) {
        //Checks to make sure the NPC is the same one as the attached one.
        if (this.getNPC() != e.getNPC()) return;
        if (!this.hasCarts) return;
        Player patron = e.getClicker();

        //Logic for Buying Public Carts
        if (!patron.isSneaking()) {
            List<ItemStack> golds = getGoldForPlayer(patron);
            if(!golds.isEmpty()) {
                int totalWorth = getFullWorth(golds);
                if(totalWorth >= costOfPublicCart) {
                    if(station.buyCart(patron, EntityType.MINECART)) {
                        //Organize the gold nuggets, so that we don't have to worry about people trolling and putting nuggets in awkward counts.
                        golds = organizeGold(golds);
                        for(ItemStack nugget : golds) {
                            if(nugget.getAmount() >= costOfPublicCart) {
                                nugget.setAmount(nugget.getAmount() - costOfPublicCart);
                                break;
                            }
                        }
                    }
                }
                else {
                    patron.sendMessage("You need " + (costOfPublicCart - totalWorth) + " additional " + ChatColor.GOLD + " gold nugget(s)" + ChatColor.RESET + " to buy a Public Cart.");
                }
            }
            else {
                patron.sendMessage("You need gold nuggets to buy carts!");
            }
        }
        //Shift + Right Clicking Sells Public Carts, at a lower price than bought.
        else if (patron.isSneaking()) {
            if (station.sellCart(patron, EntityType.MINECART)) {
                //Divide, but give the benefit to the seller. Round up.
                //Set final stack to the number found by dividing.
                patron.getInventory().addItem(new ItemStack(Material.GOLD_NUGGET, (calcSell(EntityType.MINECART))));
            }
        }
    }
=======
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

>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
}
