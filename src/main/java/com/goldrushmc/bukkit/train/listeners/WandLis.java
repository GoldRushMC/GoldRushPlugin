package com.goldrushmc.bukkit.train.listeners;

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

    public static Map<Player, List<Location>> wandLoc = new HashMap<Player, List<Location>>();

    public WandLis(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onWandUse(PlayerInteractEvent event) {

        //If the event is the wrong one, go no further.
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            //Get the player and item held.
            Player p = event.getPlayer();
            ItemStack item = p.getItemInHand();

            //If it isn't a blaze rod, we don't care...
            if (!item.getType().equals(Material.BLAZE_ROD)) return;

            //Make sure the player has permissions to create stations!
            if (!p.hasPermission("goldrushmc.station.create")) {
                p.sendMessage("You do not have permission to create train stations!");
                return;
            }

            //Check to make sure the player has a mapped value created already. if not, we make one!
            if (!wandLoc.containsKey(p)) wandLoc.put(p, new ArrayList<Location>());

            //Check the item meta to make sure the tool has the keyword "Creatable".
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.getLore().contains("Creatable")) {

                    //Get the location clicked, and the one above it.
                    Location loc = event.getClickedBlock().getLocation();
                    Location temp = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());

                    List<Location> marked = wandLoc.get(p);
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
                    p.sendMessage("You placed a marker at:" + ChatColor.GREEN + " [" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + " ]");
                }
            }
        }

        //If you hit the block above the marked block!
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Player p = event.getPlayer();
            Location checkUnder = event.getClickedBlock().getLocation();
            Location check = new Location(checkUnder.getWorld(), checkUnder.getX(), checkUnder.getY() - 1, checkUnder.getZ());
            if (wandLoc.containsKey(p)) {
                List<Location> locs = wandLoc.get(p);
                if (locs.contains(check)) {
                    locs.remove(check);
                    p.sendMessage(ChatColor.YELLOW + "You have removed one of your markers!");
                } else if (wandLoc.containsValue(check)) {
                    p.sendMessage(ChatColor.RED + "You cannot destroy someone elses markers!");
                    event.setCancelled(true);
                }
            }
        }
    }
}
