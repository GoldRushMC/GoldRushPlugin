package com.goldrushmc.bukkit.train.event;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.goldrushmc.bukkit.train.signs.ISignLogic;
import com.goldrushmc.bukkit.train.signs.SignType;
import com.goldrushmc.bukkit.train.station.TrainStation;

public class StationSignEvent extends TrainStationEvent {

	private static final HandlerList handlers = new HandlerList();
	private final Sign sign;
	private final Player player;
	
	public StationSignEvent(final TrainStation station, final Sign sign, final Player player) {
		super(station);
		this.sign = sign;
		this.player = player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	/**
	 * Gets the type of sign involved in the event.
	 * 
	 * @return The {@link SignType}, if any. Will return null if there is none.
	 */
	public SignType getSignType() {
		ISignLogic signList = station.getSigns();
		SignType type = signList.getSignType(sign);
		if(type == null) return SignType.TRAIN_STATION;
		return type;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Sign getSign() {
		return sign;
	}

}
