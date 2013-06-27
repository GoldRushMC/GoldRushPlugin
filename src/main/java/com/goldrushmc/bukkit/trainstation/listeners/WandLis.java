package com.goldrushmc.bukkit.trainstation.listeners;

import com.goldrushmc.bukkit.commands.BuildModeCommand;
import com.goldrushmc.bukkit.defaults.DefaultListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WandLis extends DefaultListener {

    public static Map<Player, List<Location>> stationLoc = new HashMap<>();
    public static Map<Player, List<Location>> townLoc = new HashMap<>();
    public static Map<Player, List<Location>> bankLoc = new HashMap<>();

    public WandLis(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onWandUse(PlayerInteractEvent event) {

        //If the item is just a hand, we don't care.
        if(event.getItem() == null) return;

        //If the event is the wrong one, go no further.
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            //Get the player and item held.
            Player p = event.getPlayer();
            ItemStack item = p.getItemInHand();

            BuildModeCommand.BuildMode b = BuildModeCommand.buildMode.get(p);

            //If it isn't a blaze rod, we don't care...
            if (!item.getType().equals(Material.BLAZE_ROD)) return;

            //Make sure the player has permissions to create stations!
            if (!p.hasPermission("goldrushmc.types.create")) {
                p.sendMessage("You do not have permission to create trainstation stations!");
                return;
            }



            //Check the item meta to make sure the tool has the keyword "Creatable".
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if(meta.getLore().contains("Station Mode")) {

                    //Check to make sure the player has a mapped value created already. if not, we make one!
                    if (!stationLoc.containsKey(p)) stationLoc.put(p, new ArrayList<Location>());

                    //Get the location clicked, and the one above it.
                    Location loc = event.getClickedBlock().getLocation();
                    Location temp = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());

                    List<Location> marked = stationLoc.get(p);
                    //we have a max of 2 slots available (one for each diagonal cardinal direction.)
                    if (marked.size() == 2) {
                        p.sendMessage(ChatColor.RED + "Please remove one of your markings before selecting another.");
                        return;
                    }

                    //Add the location to the list.
                    marked.add(loc);

                    //Generate a torch to represent above the marked block.
                    Block block = temp.getBlock();
                    block.setType(Material.TORCH);

                    //Notify player.
                    p.sendMessage("You placed a STATION marker at:" + ChatColor.GREEN + " [" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + " ]");
                } else if(meta.getLore().contains("Bank Mode")) {

                    //Check to make sure the player has a mapped value created already. if not, we make one!
                    if (!bankLoc.containsKey(p)) bankLoc.put(p, new ArrayList<Location>());

                    //Get the location clicked, and the one above it.
                    Location loc = event.getClickedBlock().getLocation();
                    Location temp = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());

                    List<Location> marked = bankLoc.get(p);
                    //we have a max of 2 slots available (one for each diagonal cardinal direction.)
                    if (marked.size() == 2) {
                        p.sendMessage(ChatColor.RED + "Please remove one of your markings before selecting another.");
                        return;
                    }

                    //Add the location to the list.
                    marked.add(loc);

                    //Generate a torch to represent above the marked block.
                    Block block = temp.getBlock();
                    block.setType(Material.REDSTONE_TORCH_ON);

                    //Notify player.
                    p.sendMessage("You placed a BANK marker at:" + ChatColor.YELLOW + " [" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + " ]");

                }
                //Friendly message about the missing features...
                else p.sendMessage(ChatColor.AQUA + "The other feature 'Town' is currently under production, and does not yet exist. \n" +
                                        ChatColor.RESET + "So please switch to the STATION or BANK mode before using this feature." + ChatColor.GREEN + " Have a nice day!");
            }
        }

        //If you hit the block above the marked block!
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Player p = event.getPlayer();
            Location checkUnder = event.getClickedBlock().getLocation();
            Location check = new Location(checkUnder.getWorld(), checkUnder.getX(), checkUnder.getY() - 1, checkUnder.getZ());

            switch(BuildModeCommand.buildMode.get(p)) {
                case STATION:
                    if (stationLoc.containsKey(p)) {
                        List<Location> locs = stationLoc.get(p);
                        if (locs.contains(check)) {
                            locs.remove(check);
                            p.sendMessage(ChatColor.YELLOW + "You have removed one of your markers for STATIONS!");
                        } else {
                            for(List<Location> lists : stationLoc.values()) {
                                if(lists.contains(check)) {
                                    p.sendMessage(ChatColor.RED + "You cannot destroy someone elses markers!");
                                    event.setCancelled(true);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case BANK:
                    if (bankLoc.containsKey(p)) {
                        List<Location> locs = bankLoc.get(p);
                        if (locs.contains(check)) {
                            locs.remove(check);
                            p.sendMessage(ChatColor.YELLOW + "You have removed one of your markers for BANKS!");
                        } else {
                            for(List<Location> lists : bankLoc.values()) {
                                if(lists.contains(check)) {
                                    p.sendMessage(ChatColor.RED + "You cannot destroy someone elses markers!");
                                    event.setCancelled(true);
                                    break;
                                }
                            }
                        }
                    }
                    break;
            }


        }
    }
}
