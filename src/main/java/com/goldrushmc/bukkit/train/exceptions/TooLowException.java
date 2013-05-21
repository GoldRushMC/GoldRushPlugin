package com.goldrushmc.bukkit.train.exceptions;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class TooLowException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 409673882317617457L;
	
	private Location tooLowVal;
	
	public TooLowException(Location tooLowVal) {
		this.tooLowVal = tooLowVal;
	}
	
	public Location getTooLowVal() {
		return this.tooLowVal;
	}
	
	@Override
	public String getMessage() {
		return "The value that is too low is:" + ChatColor.RED + " [ " + tooLowVal.getBlockX() + tooLowVal.getBlockY() + tooLowVal.getBlockZ() + " ]";
	}
}
