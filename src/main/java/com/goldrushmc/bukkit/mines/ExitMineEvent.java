package com.goldrushmc.bukkit.mines;

import com.goldrushmc.bukkit.trainstation.TrainStation;
import com.goldrushmc.bukkit.trainstation.event.TrainStationEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class ExitMineEvent extends MineEvent {

    protected Player player;
    private static final HandlerList handlers = new HandlerList();

    public ExitMineEvent(final Mine mine, final Player exiter) {
        super(mine);
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
