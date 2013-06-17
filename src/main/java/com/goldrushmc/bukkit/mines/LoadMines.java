package com.goldrushmc.bukkit.mines;

import com.goldrushmc.bukkit.db.MinesTbl;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class LoadMines {

	List<Mine> mines = new ArrayList<>();

	public LoadMines(JavaPlugin p){

        //temp list to stop repeated re-queering of the database
        List<MinesTbl> mineTableList = Mine.getDB().getMines();

        for(MinesTbl mineEnt: mineTableList) {

        }

        //reset global mines list to loaded mines.
        Mine.setMines(mines);
	}

}
