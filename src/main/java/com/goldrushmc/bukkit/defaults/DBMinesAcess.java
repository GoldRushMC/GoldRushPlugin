package com.goldrushmc.bukkit.defaults;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.goldrushmc.bukkit.db.MineLocationTbl;
import com.goldrushmc.bukkit.db.MinesTbl;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lucas
 * Date: 6/17/13
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBMinesAcess implements DBMinesAccessible {

    public JavaPlugin plugin;

    public DBMinesAcess(JavaPlugin plugin) {
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
}
