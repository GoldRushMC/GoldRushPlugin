package com.goldrushmc.bukkit.defaults;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.goldrushmc.bukkit.db.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class DBStationsAccess implements IStationAccessible {

    private final JavaPlugin plugin;

    public DBStationsAccess(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Query<TrainScheduleTbl> querySchedule() {
        return getDB().find(TrainScheduleTbl.class);
    }

    @Override
    public Query<TrainTbl> queryTrains() {
        return getDB().find(TrainTbl.class);
    }

    @Override
    public Query<TrainStatusTbl> queryStatus() {
        return getDB().find(TrainStatusTbl.class);
    }

    @Override
    public List<TrainScheduleTbl> getSchedules() {
        return querySchedule().findList();
    }

    @Override
    public List<TrainTbl> getTrains() {
        return queryTrains().findList();
    }

    @Override
    public List<TrainStatusTbl> getStatuses() {
        return queryStatus().findList();
    }

    @Override
    public TrainScheduleTbl getNextDeparture(TrainTbl train) {
        for (TrainScheduleTbl ts : train.getSchedule()) {
            if (ts.isNext()) return ts;
        }
        return null;
    }

    @Override
    public TrainScheduleTbl getNextDeparture(String trainName) {
        return getNextDeparture(getTrain(trainName));
    }

    @Override
    public TrainTbl getTrain(String trainName) {
        return queryTrains().where().ieq("TRAIN_NAME", trainName).findUnique();
    }

    @Override
    public TrainStatusTbl getTrainStatus(TrainTbl train) {
        return train.getStatus();
    }

    @Override
    public TrainStatusTbl getTrainStatus(String trainName) {
        return getTrainStatus(getTrain(trainName));
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
    public StationTbl getTrainStation(TrainTbl train) {
        // TODO Auto-generated method stub
        return null;
    }

}
