package com.goldrushmc.bukkit.db.access;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.goldrushmc.bukkit.db.tables.StationLocationTbl;
import com.goldrushmc.bukkit.db.tables.StationTbl;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DBStationsAccess implements IStationAccessible {

    private final JavaPlugin plugin;

    public DBStationsAccess(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public EbeanServer getDB() {
        return plugin.getDatabase();
    }

    @Override
    public Query<StationLocationTbl> queryTrainStationLocations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Query<StationTbl> queryTrainStations() {
        return getDB().find(StationTbl.class);
    }

    @Override
    public List<StationTbl> getTrainStations() {
        return queryTrainStations().findList();
    }

    @Override
    public List<StationLocationTbl> getTrainStationLocations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StationTbl getTrainStation(String stationName) {
        return queryTrainStations().where().ieq("name", stationName).findUnique();
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
