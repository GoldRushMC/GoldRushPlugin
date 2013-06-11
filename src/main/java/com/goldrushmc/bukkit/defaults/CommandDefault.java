package com.goldrushmc.bukkit.defaults;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CommandDefault implements CommandExecutor {

<<<<<<< HEAD
    public final JavaPlugin plugin;

    public CommandDefault(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);

    public boolean isInt(String num) {
        try {
            @SuppressWarnings("unused")
            int i = Integer.valueOf(num);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean isDouble(String num) {
        try {
            @SuppressWarnings("unused")
            double i = Double.valueOf(num);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void deny(CommandSender sender) {
        sender.sendMessage("You do not have permission to perform this command.");
    }

    public boolean isPlayer(CommandSender sender) {
        return (sender instanceof Player);
    }
=======
	public final JavaPlugin plugin;
	
	public CommandDefault(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);
	
	public boolean isInt(String num) {
		try {
			@SuppressWarnings("unused")
			int i = Integer.valueOf(num);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public boolean isDouble(String num) {
		try {
			@SuppressWarnings("unused")
			double i = Double.valueOf(num);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	public void deny(CommandSender sender) {
		sender.sendMessage("You do not have permission to perform this command.");
	}
	
	public boolean isPlayer(CommandSender sender) {
		return (sender instanceof Player);
	}
>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
}
