package com.goldrushmc.bukkit.db.access;

import com.avaje.ebean.Query;
import com.goldrushmc.bukkit.db.tables.MineLocationTbl;
import com.goldrushmc.bukkit.db.tables.MinesTbl;

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
