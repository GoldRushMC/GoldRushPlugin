package com.goldrushmc.bukkit.defaults;

import com.avaje.ebean.Query;
import com.goldrushmc.bukkit.db.MineLocationTbl;
import com.goldrushmc.bukkit.db.MinesTbl;

import java.util.List;

public interface DBMinesAccessible extends DBAccessible{

    /**
     * Queries the DB for the MineLocationTbl class.
     *
     * @return
     */
    Query<MineLocationTbl> queryMineLocations();

    /**
     * Queries the DB for the MineTbl class.
     *
     * @return
     */
    Query<MinesTbl> queryMines();

    List<MinesTbl> getMines();

    List<MineLocationTbl> getMineLocations();

    MinesTbl getMine(String mineName);

    MinesTbl getMine(MineLocationTbl location);
}
