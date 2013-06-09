package com.goldrushmc.bukkit.commands;

import com.goldrushmc.bukkit.defaults.CommandDefault;
import com.goldrushmc.bukkit.defaults.GoldRushPerms;
import com.goldrushmc.bukkit.train.scheduling.Departure;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * This command will probably not exist in the future.
 * <p/>
 * Sets the schedule for train stations, as a generic schedule. Need to add in stuff to facilitate specific train station scheduling.
 *
 * @author Lucas
 */
public class TrainCycleCommand extends CommandDefault {

    public TrainCycleCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission(GoldRushPerms.SCHEDULE)) {
            deny(sender);
            return true;
        }

        int taskID = Departure.getTaskID();
        BukkitScheduler s = Bukkit.getScheduler();

        if (args[0].equalsIgnoreCase("Start") && taskID == 0) {
            sender.sendMessage("Starting Train Cycle...");
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Departure(plugin), 100, 400);
            return true;
        } else if (taskID != 0) {
            sender.sendMessage("Train Cycle already started!");
            return true;
        } else if (args[0].equalsIgnoreCase("Stop") && (s.isCurrentlyRunning(taskID) || s.isQueued(taskID))) {
            sender.sendMessage("Stopping Train Cycle...");
            Bukkit.getScheduler().cancelTask(taskID);
            Departure.resetTaskID();
            return true;
        } else {
            sender.sendMessage("Train Cycle is not running.");
            return true;
        }
    }
}
