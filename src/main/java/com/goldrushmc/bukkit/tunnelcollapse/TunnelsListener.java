package com.goldrushmc.bukkit.tunnelcollapse;

import com.goldrushmc.bukkit.defaults.DefaultListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TunnelsListener extends DefaultListener {

    public TunnelsListener(JavaPlugin instance) {
        super(instance);

    }

    Countdown c = new Countdown(plugin);

    private List<Player> players = new ArrayList<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent evt) {

        Player player = evt.getPlayer();

        players = c.getList();

        if (player.getLocation().getBlockY() <= plugin.getConfig().getInt(
                "depth")) {

            if (!players.contains(player.getName())) {

                players.add(player);

                c.setList(this.players);
                c.setPlayer(player);

                Bukkit.getServer()
                        .getScheduler()
                        .scheduleSyncDelayedTask(this.plugin,
                                new Countdown(this.plugin),
                                this.plugin.getConfig().getLong("delay") * 20);

                Random r = new Random();

                int q = r.nextInt(this.plugin.getConfig().getInt("chance")) + 1;

                if (q == this.plugin.getConfig().getInt("chance")) {

                    int x = player.getLocation().getBlockX(), y = player
                            .getLocation().getBlockY(), z = player
                            .getLocation().getBlockZ();

                    World w = player.getWorld();

                    Location loc1 = new Location(w, x
                            - this.plugin.getConfig().getInt("radius") / 2, y,
                            z - this.plugin.getConfig().getInt("radius") / 2);

                    Location loc2 = new Location(w, x
                            + this.plugin.getConfig().getInt("radius") / 2, y
                            + this.plugin.getConfig().getInt("radius"), z
                            + this.plugin.getConfig().getInt("radius") / 2);

                    loopAndFall(loc1, loc2, w);

                }

            }
        }
    }

    public static void loopAndFall(Location loc1, Location loc2, World w) {

        Bukkit.getServer().broadcastMessage("started loop");

        Integer minx = Math.min(loc1.getBlockX(), loc2.getBlockX()), miny = Math
                .min(loc1.getBlockY(), loc2.getBlockY()), minz = Math.min(
                loc1.getBlockZ(), loc2.getBlockZ()), maxx = Math.max(
                loc1.getBlockX(), loc2.getBlockX()), maxy = Math.max(
                loc1.getBlockY(), loc2.getBlockY()), maxz = Math.max(
                loc1.getBlockZ(), loc2.getBlockZ());

        Bukkit.getServer().broadcastMessage(
                minx.toString() + " " + maxx.toString() + " " + miny.toString() + " "
                        + maxy.toString() + " " + minz.toString() + " " + maxz.toString());

        for (int x = minx; x <= maxx; x++) {
            for (int y = miny; y <= maxy; y++) {
                for (int z = minz; z <= maxz; z++) {

                    Bukkit.getServer().broadcastMessage("looping...");

                    Block b = w.getBlockAt(x, y, z);

                    if (b.getType() == Material.STONE
                            || b.getType() == Material.DIRT
                            || b.getType() == Material.COAL_ORE
                            || b.getType() == Material.IRON_ORE
                            || b.getType() == Material.GOLD_ORE
                            || b.getType() == Material.DIAMOND_ORE
                            || b.getType() == Material.COBBLESTONE
                            || b.getType() == Material.EMERALD_ORE
                            || b.getType() == Material.REDSTONE_ORE
                            || b.getType() == Material.OBSIDIAN) {

                        Bukkit.getServer().broadcastMessage("correct block");

                        if (w.getBlockAt(x, y - 1, z).getType() == Material.AIR) {

                            Bukkit.getServer().broadcastMessage("falling!");

                            w.spawnFallingBlock(new Location(w, x, y, z),
                                    b.getType(), (byte) 0);

                        }

                    }

                }
            }
        }

    }
}
