package com.goldrushmc.bukkit.tunnelcollapse;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Countdown extends BukkitRunnable {

    public Player player = null;
    public List<Player> ps = new ArrayList<Player>();

    public Plugin plugin;

    Countdown(Plugin instance) {

        this.plugin = instance;

    }

    public void setPlayer(Player p) {

        this.player = p;

    }

    public void setList(List<Player> list) {

        this.ps = list;

    }

    public List<Player> getList() {

        return this.ps;
    }

    public void run() {

        this.ps.remove(this.player);
    }


}
