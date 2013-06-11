package com.goldrushmc.bukkit.tunnelcollapse;

import com.goldrushmc.bukkit.defaults.CommandDefault;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TunnelCollapseCommand extends CommandDefault {

    public TunnelCollapseCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {

        if (cmd.getName().equalsIgnoreCase("fall")) {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                int x = player.getLocation().getBlockX(), y = player
                        .getLocation().getBlockY(), z = player.getLocation()
                        .getBlockZ();

                World w = player.getWorld();

                Location loc1 = new Location(w, x
                        - this.plugin.getConfig().getInt("radius") / 2, y, z
                        - this.plugin.getConfig().getInt("radius") / 2);

                Location loc2 = new Location(w, x
                        + this.plugin.getConfig().getInt("radius") / 2, y
                        + this.plugin.getConfig().getInt("radius"), z
                        + this.plugin.getConfig().getInt("radius") / 2);

                TunnelsListener.loopAndFall(loc1, loc2, w);

            }

        }

        return false;
    }

}
