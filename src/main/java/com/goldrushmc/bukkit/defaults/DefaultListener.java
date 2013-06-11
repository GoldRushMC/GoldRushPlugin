package com.goldrushmc.bukkit.defaults;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class DefaultListener implements Listener {

    protected final JavaPlugin plugin;

    public DefaultListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }


}
