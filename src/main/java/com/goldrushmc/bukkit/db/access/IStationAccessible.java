package com.goldrushmc.bukkit.db.access;

import com.avaje.ebean.Query;
import com.goldrushmc.bukkit.db.tables.StationLocationTbl;
import com.goldrushmc.bukkit.db.tables.StationTbl;

import java.util.List;

public interface IStationAccessible extends DBAccessible {


    Query<StationLocationTbl> queryTrainStationLocations();

    Query<StationTbl> queryTrainStations();

    List<StationTbl> getTrainStations();

    List<StationLocationTbl> getTrainStationLocations();

    StationTbl getTrainStation(String stationName);

}
