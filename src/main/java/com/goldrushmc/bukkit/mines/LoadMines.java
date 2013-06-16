package com.goldrushmc.bukkit.mines;

import com.goldrushmc.bukkit.db.MinesTbl;
import com.goldrushmc.bukkit.train.exceptions.MarkerNumberException;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoadMines {

	List<Mine> mines = new ArrayList<>();

	public LoadMines(Plugin p, JavaPlugin pl){

        //temp list to stop repeated re-queering of the database
        List<MinesTbl> mineTableList = p.getDatabase().find(MinesTbl.class).findList();

        for(MinesTbl mineEnt: mineTableList) {
            //map list values to variables for use in mine creation
            String name = mineEnt.getName();
            String worldName = mineEnt.getWorldName();
            List<Location> locList = new ArrayList<>();
                locList.add(mineEnt.getLocOne());
                locList.add(mineEnt.getLocTwo());
            Vector entrancePos = mineEnt.getEntrancePos();
            int density = mineEnt.getDensity();
            Boolean isGen = mineEnt.getGenerated();

            //attempt to recreate the mine into a temporary mine variable
            try {
                Mine tempMine =  new Mine(name, p.getServer().getWorld(worldName), locList, pl, entrancePos, density, isGen);
                mines.add(tempMine);
            } catch (MarkerNumberException e) {
                p.getLogger().info("Error creating Mine, incorrect number of Coords!");
            }
        }

        //reset global mines list to loaded mines.
        Mine.setMines(mines);
	}

}
