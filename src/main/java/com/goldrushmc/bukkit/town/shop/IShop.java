package com.goldrushmc.bukkit.town.shop;

import org.bukkit.inventory.ItemStack;

public interface IShop<E extends ItemStack> {

	public boolean buyItem(E item);
	
	public boolean sellItem(E item);
}
