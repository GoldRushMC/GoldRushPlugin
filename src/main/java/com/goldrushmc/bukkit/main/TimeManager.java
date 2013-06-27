package com.goldrushmc.bukkit.main;

import com.goldrushmc.bukkit.defaults.DefaultListener;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created with IntelliJ IDEA.
 * User: Lex
 * Date: 16/06/13
 * Time: 21:37
 * To change this template use File | Settings | File Templates.
 */
public class TimeManager implements Runnable{

    JavaPlugin p;

    public TimeManager(JavaPlugin plugin) {
        p = plugin;
    }

    @Override
    public void run() {
        for(World world : p.getServer().getWorlds()){
            if(world.getTime() <= 16000) {
                world.setTime(world.getTime() - 1);
            }
        }
        p.getServer().getScheduler().runTaskLater(p, new TimeManager(p), 2);
    }
}
