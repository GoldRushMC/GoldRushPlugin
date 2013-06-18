package com.goldrushmc.bukkit.mines;

import com.goldrushmc.bukkit.db.tables.MineLocationTbl;
import com.goldrushmc.bukkit.db.tables.MinesTbl;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

@Deprecated
public class SaveMines{

	Plugin p;
	
	public SaveMines(Plugin p) {
		this.p = p;
	}
	
	public void save() {

        for(Mine mine : Mine.getMines()) {
            //Instantiate necessary objects for saving
            MinesTbl mineEnt = new MinesTbl();
            MineLocationTbl tbl1 = new MineLocationTbl(),
                            tbl2 = new MineLocationTbl(),
                            tbl3 = new MineLocationTbl();

            //Initiate the objects with the proper location/vector data (replaces constructor)
            tbl1.initLocation(mine.recCoordOne);
            tbl2.initLocation(mine.recCoordTwo);
            //Mark the entrance as such.
            tbl3.initVector(mine.mineEntrance);
            tbl3.setEntrance(true);

            //Mine Data
            mineEnt.setName(mine.getName());
            mineEnt.setDensity(mine.density);
            mineEnt.setGenerated(mine.isGenerated);
            Set<MineLocationTbl> locs = new HashSet<>();
            locs.add(tbl1);
            locs.add(tbl2);
            locs.add(tbl3);
            mineEnt.setLocations(locs);

            //Location - Mine References
            tbl1.setMine(mineEnt);
            tbl2.setMine(mineEnt);
            tbl3.setMine(mineEnt);

            //Save ALL Stuff to the DB.
            Mine.getDB().getDB().save(mineEnt);
            Mine.getDB().getDB().save(locs);

        }
	}
}
