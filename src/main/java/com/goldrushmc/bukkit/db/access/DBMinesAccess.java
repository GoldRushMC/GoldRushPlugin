package com.goldrushmc.bukkit.db.access;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.goldrushmc.bukkit.db.tables.MineLocationTbl;
import com.goldrushmc.bukkit.db.tables.MinesTbl;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * User: Diremonsoon
 * Date: 6/17/13
 * Time: 2:41 PM
 */
public class DBMinesAccess implements DBMinesAccessible {

    public JavaPlugin plugin;

    public DBMinesAccess(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Query<MineLocationTbl> queryMineLocations() {
        return getDB().find(MineLocationTbl.class);
    }

    @Override
    public Query<MinesTbl> queryMines() {
        return getDB().find(MinesTbl.class);
    }

    @Override
    public List<MinesTbl> getMines() {
        return queryMines().findList();
    }

    @Override
    public List<MineLocationTbl> getMineLocations() {
        return queryMineLocations().findList();
    }

    @Override
    public MinesTbl getMine(String mineName) {
        return queryMines().where().ieq("name", mineName).findUnique();
    }

    @Override
    public MinesTbl getMine(MineLocationTbl location) {
        for(MinesTbl m : getMines()) {
            if(m.getLocations() != null) {
                if(m.getLocations().contains(location)) {
                    return m;
                }
            }
        }
        return null;
    }

    @Override
    public EbeanServer getDB() {
         return plugin.getDatabase();
    }

    @Override
    public void save(Object o) {
        getDB().save(o);
    }

    @Override
    public void delete(Object o) {
        getDB().delete(o);
    }
}
