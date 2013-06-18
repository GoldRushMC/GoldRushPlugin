package com.goldrushmc.bukkit.trainstation.event;

import com.goldrushmc.bukkit.trainstation.TrainStation;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class EnterTrainStationEvent extends TrainStationEvent {

    protected Player player;
    private static final HandlerList handlers = new HandlerList();

    public EnterTrainStationEvent(final TrainStation station, final Player enterer) {
        super(station);
        this.player = enterer;
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
