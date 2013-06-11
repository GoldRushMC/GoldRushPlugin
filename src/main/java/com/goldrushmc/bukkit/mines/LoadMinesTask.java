package com.goldrushmc.bukkit.mines;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LoadMinesTask implements Runnable {
    JavaPlugin p;

    public LoadMinesTask(JavaPlugin plug) {
        p = plug;
    }

    @Override
    public void run() {
        LoadMines loadMines = new LoadMines(p, p);
        Bukkit.getServer().getScheduler().runTaskLater(p, new GenTask(loadMines), 20);
    }

    class GenTask implements Runnable {
        LoadMines loadMines;

        GenTask(LoadMines lm) {
            loadMines = lm;
        }

        @Override
        public void run() {
            Mine.setMines(loadMines.parseMinesStrings());
            Bukkit.getServer().broadcastMessage("Successfully loaded " + Mine.getMines().size() + " mines!");
        }
    }
}
