package com.goldrushmc.bukkit.mines;

import com.goldrushmc.bukkit.db.MinesTbl;
import org.bukkit.plugin.Plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SaveMines {

	List<Mine> mineList;
	Plugin p;
	
	public SaveMines(Plugin p) {
		this.mineList = Mine.getMines();
		this.p = p;
	}
	
	public Boolean save() {

        for(Mine mine : mineList) {
            MinesTbl mineEnt = new MinesTbl();
            mineEnt.setName(mine.getName());
            mineEnt.setWorldName(mine.getWorld().getName());
            mineEnt.setLocOne(mine.recCoordOne);
            mineEnt.setLocTwo(mine.recCoordTwo);
            mineEnt.setEntrancePos(mine.mineEntrance);
            mineEnt.setDensity(mine.density);
            mineEnt.setGenerated(mine.isGenerated);

            p.getDatabase().save(mineEnt);
        }
        return true;
	}
}
