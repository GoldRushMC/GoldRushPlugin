package com.goldrushmc.bukkit.train.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.goldrushmc.bukkit.train.station.TrainStation;

public class ExitTrainStationEvent extends TrainStationEvent {

	protected Player player;	
	private static final HandlerList handlers = new HandlerList();
	
	public ExitTrainStationEvent(final TrainStation station, final Player exiter) {
		super(station);
		this.player = exiter;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public Player getPlayer() {
		return this.player;
	}
}
