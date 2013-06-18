package com.goldrushmc.bukkit.trainstation.event;

import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.type.MinecartMemberChest;
import com.goldrushmc.bukkit.trainstation.TrainStation;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TrainFullStopEvent extends TrainStationEvent {

    private static final HandlerList handlers = new HandlerList();
    private final MinecartGroup train;

    public TrainFullStopEvent(TrainStation station, final MinecartGroup train) {
        super(station);
        this.train = train;
    }

    public boolean hasCargo() {
        for (MinecartMember<?> mm : train) {
            if (mm instanceof MinecartMemberChest) {
                Inventory inv = ((MinecartMemberChest) mm).getEntity().getInventory();
                ItemStack[] items = inv.getContents();
                int notCoal = 0;
                //Find out how much stuff is NOT coal.
                for (int i = 0; i < items.length; i++) {
                    if (!items[i].getType().equals(Material.COAL)) {
                        notCoal++;
                    }
                }
                //If it is not coal, even one inventory slot in one inventory, it is considered precious cargo.
                if (notCoal > 0) {
                    return true;
                }
            }
        }
        return false;
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