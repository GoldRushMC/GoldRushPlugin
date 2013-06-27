package com.goldrushmc.bukkit.mines;

import com.goldrushmc.bukkit.trainstation.TrainStation;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class MineEvent extends Event implements Cancellable {


    protected final Mine mine;
    private boolean cancelled;

    public MineEvent(final Mine mine) {
        this.mine = mine;
    }

    public Mine getMine() {
        return this.mine;
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
