package com.goldrushmc.bukkit.defaults;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class GoldRushPerms {
	public static final Permission DEFAULT = new Permission("goldrushmc.station", "The player can use any of the train station commands.", PermissionDefault.OP),
	LIST = new Permission("goldrushmc.station.list","", PermissionDefault.TRUE),
	ADD_CART = new Permission("goldrushmc.station.addcart", "The player can buy carts", PermissionDefault.TRUE),
	REMOVE_CART = new Permission("goldrushmc.station.removecart", "The player can sell carts", PermissionDefault.TRUE),
	ADD_SIGN = new Permission("goldrushmc.station.addsign", "The player can add signs to stations", PermissionDefault.OP),
	REMOVE_SIGN = new Permission("goldrushmc.station.removesign", "The player can remove signs from stations", PermissionDefault.OP),
	CHANGE_BLOCK = new Permission("goldrushmc.station.changeblock", "The player can edit blocks within train stations", PermissionDefault.OP),
	REMOVE = new Permission("goldrushmc.station.destroy", "The player can delete train stations", PermissionDefault.OP),
	ADD = new Permission("goldrushmc.station.create", "The player can create train stations", PermissionDefault.OP),
	SCHEDULE = new Permission("goldrushmc.station.schedule", "The player can schedule train cycles", PermissionDefault.OP);
	
	
	
	final Permission perm;
	
	public GoldRushPerms(String perm, String description, PermissionDefault defaultP) {
		this.perm = new Permission(perm, description, defaultP);
	}
}
