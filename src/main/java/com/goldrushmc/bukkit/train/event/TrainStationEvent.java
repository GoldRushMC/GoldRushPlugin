package com.goldrushmc.bukkit.train.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import com.goldrushmc.bukkit.train.station.TrainStation;

public abstract class TrainStationEvent extends Event implements Cancellable  {


	protected final TrainStation station;
	private boolean cancelled;
	
	public TrainStationEvent(final TrainStation station) {
		this.station = station;
	}
	
	public TrainStation getTrainStation() {
		return this.station;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;		
	}


}
