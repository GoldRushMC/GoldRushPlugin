package com.goldrushmc.bukkit.commands;

import com.goldrushmc.bukkit.bank.Bank;
import com.goldrushmc.bukkit.defaults.CommandDefault;
import com.goldrushmc.bukkit.defaults.GoldRushPerms;
import com.goldrushmc.bukkit.defaults.NotInTownException;
import com.goldrushmc.bukkit.trainstation.exceptions.MarkerNumberException;
import com.goldrushmc.bukkit.trainstation.listeners.WandLis;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * User: Diremonsoon
 * Date: 6/20/13
 */
public class CreateBankCommand extends CommandDefault {

    public CreateBankCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        //The only argument we want put in is the name of the bank. Everything else should self-generate.
        if(args.length != 1) { return false;}
        //Check for permissions
        if(!sender.hasPermission(GoldRushPerms.CREATE)) { deny(sender); return true; }
        //Check to see if the sender has a mapping for markers yet.
        if(!WandLis.bankLoc.containsKey(sender)) { sender.sendMessage("Please add some markers first!"); return true; }
        //Check to see if the size of the Location list is 2. If it isn't, we reject the creation.
        List<Location> locs = WandLis.bankLoc.get(sender);
        if(locs.size() != 2) { sender.sendMessage("You need " + (2 - locs.size()) + " more markers before you can create a bank!"); return true; }

        //Attempts to add the bank. If the bank is not fully within a town, or does not have the right amount of markers (somehow) the creation will fail.
        try {
            new Bank(((Player)sender).getWorld(), locs, plugin, args[0], true);
            sender.sendMessage("Bank created successfully!");
            return true;
        } catch (MarkerNumberException | NotInTownException e) {
            sender.sendMessage(e.getMessage());
            return false;
        }
    }
}
