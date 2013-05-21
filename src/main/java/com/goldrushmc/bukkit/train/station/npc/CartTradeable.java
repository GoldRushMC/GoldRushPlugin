package com.goldrushmc.bukkit.train.station.npc;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;

import org.bukkit.event.EventHandler;

import com.bergerkiller.bukkit.tc.controller.MinecartMember;

public class CartTradeable extends Trait {

	protected CartTradeable(String name) {
		super(name);
	}
	
	@Persist private boolean hasCarts = false;
	private List<MinecartMember<?>> cartInventory = new ArrayList<MinecartMember<?>>();
	
	@Override
	public void load(DataKey key) {
		
	}
	
	@Override
	public void save(DataKey key) {
		
	}
	
	@Override
	public void run() {
		
	}
	
	@Override
	public void onAttach() {
		
	}
	
	@Override
	public void onSpawn() {
		
	}
	
	@Override
	public void onDespawn() {
		
	}
	
	@Override
	public void onRemove() {
		
	}
	
	@EventHandler
	public void leftClick(NPCLeftClickEvent e) {

	}
	
	@EventHandler
	public void rightClick(NPCRightClickEvent e) {
		if(!cartInventory.isEmpty()) {
			e.getClicker().sendMessage("The current cost of a cart is: ");
		}
	}

}
