package com.goldrushmc.bukkit.train.station.npc;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager.Profession;

import com.goldrushmc.bukkit.train.station.TrainStation;

import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.AbstractNPC;
import net.citizensnpcs.api.util.DataKey;

public class KioskWorker extends AbstractNPC {

	Profession profession;
	private TrainStation station;
	
	public KioskWorker(int id) {
		super(id, "Kiosk Worker");
		this.profession = Profession.LIBRARIAN;
		this.addTrait(CartTradeable.class);
	}

	@Override
	public boolean despawn(DespawnReason reason) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LivingEntity getBukkitEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Navigator getNavigator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSpawned() {
		return false;
	}

	@Override
	public void load(DataKey key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBukkitEntityType(EntityType type) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean spawn(Location location) {
		// TODO Auto-generated method stub
		return false;
	}

}
