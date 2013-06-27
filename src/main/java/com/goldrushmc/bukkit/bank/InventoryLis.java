package com.goldrushmc.bukkit.bank;

import com.goldrushmc.bukkit.db.tables.PlayerTbl;
import com.goldrushmc.bukkit.defaults.DefaultListener;
import com.goldrushmc.bukkit.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class InventoryLis extends DefaultListener {

    public InventoryLis(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInvetoryItemCLicked(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if (e.getCurrentItem().getTypeId() == 34) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDropped(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getTypeId() == 34) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        List<ItemStack> drops = e.getDrops();
        for (int i = 0; i < drops.size(); i++) {
            if (drops.get(i).getTypeId() == 34) {
                e.getDrops().remove(i);
                i = i - 1;
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent e) {
        //Schedule run to fill player's inventory in 30 ticks
        Bukkit.getScheduler().runTaskLater(plugin, new FillInv(e.getPlayer()), 30);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        //Schedule run to fill player's inventory in 30 ticks
        if (e.getPlayer().getGameMode().equals(GameMode.ADVENTURE))
            Bukkit.getScheduler().runTaskLater(plugin, new FillInv(e.getPlayer()), 30);
    }

    @EventHandler
    public void onPlayerLoginAdd(PlayerLoginEvent e){
        Player p = e.getPlayer();
        Main.playerList.add(p);

        //DB Stuff
        PlayerTbl player = Main.db.getPlayer(p.getName());
        if(player != null) {
            player.setOnline(true);
            Main.db.getDB().save(player);
        } else {
            PlayerTbl newPlayer = new PlayerTbl();
            newPlayer.setOnline(true);
            newPlayer.setName(p.getName());
            Main.db.getDB().save(newPlayer);
        }
        //End DB Stuff
    }

    @EventHandler
    public void onPlayerLoginOut(PlayerQuitEvent e){
        Player p = e.getPlayer();
        Main.playerList.remove(p);

        //DB stuff
        PlayerTbl player = Main.db.getPlayer(p.getName());
        if(player != null) {
            player.setOnline(false);
            Main.db.getDB().save(player);
            //Just in case, if the player gets removed from the DB in the time they log in and out.
        } else {
            PlayerTbl newPlayer = new PlayerTbl();
            newPlayer.setOnline(false);
            newPlayer.setName(p.getName());
            Main.db.getDB().save(newPlayer);
        }
        //End DB Stuff
    }

    //Create class which will fill the inventory.
    class FillInv implements Runnable {
        protected final Player p;

        private FillInv(final Player p) {
            this.p = p;
        }

        @Override
        public void run() {
            if (p.getGameMode().equals(GameMode.ADVENTURE))
                for (int i = 9; i < 27; i++) {
                    p.getInventory().setItem(i, new ItemStack(Material.PISTON_EXTENSION));
                }
        }
    }
}
