package com.goldrushmc.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.defaults.CommandDefault;
import com.goldrushmc.bukkit.train.TrainFactory;

public class AddTrain extends CommandDefault {

	public AddTrain(JavaPlugin plugin) { super(plugin); }

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!sender.hasPermission("goldrushmc.train.add")) {
			sender.sendMessage("You don't have permission to add trains!");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("Ride")) {
			TrainFactory.addCart((Player) sender, args[1], EntityType.MINECART);
			return true;
		}
		
		else if(args[0].equalsIgnoreCase("Store")) {
			TrainFactory.addCart((Player) sender, args[1], EntityType.MINECART_CHEST);
			return true;
		}
		
		return false;
	}

}
