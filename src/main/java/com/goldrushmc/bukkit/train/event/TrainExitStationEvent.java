package com.goldrushmc.bukkit.train.event;

import org.bukkit.event.HandlerList;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.goldrushmc.bukkit.train.station.TrainStation;

public class TrainExitStationEvent extends TrainStationEvent {

	private static final HandlerList handlers = new HandlerList();
	private final MinecartGroup train;

	public TrainExitStationEvent(final TrainStation station, final MinecartGroup train) {
		super(station);
		this.train = train;
		
	}
	
	public MinecartGroup getTrain() {
		return train;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
