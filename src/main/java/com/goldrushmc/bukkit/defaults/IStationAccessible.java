package com.goldrushmc.bukkit.defaults;

import com.avaje.ebean.Query;
import com.goldrushmc.bukkit.db.*;

import java.util.List;

public interface IStationAccessible extends DBAccessible{


    Query<StationLocationTbl> queryTrainStationLocations();

    Query<StationTbl> queryTrainStations();

    /**
     * Queries the DB for the TrainScheduleTbl class.
     *
     * @return
     */
    Query<TrainScheduleTbl> querySchedule();

    /**
     * Queries the DB for the TrainTbl class.
     *
     * @return
     */
    Query<TrainTbl> queryTrains();

    /**
     * Queries the DB for the TrainStatusTbl class.
     *
     * @return
     */
    Query<TrainStatusTbl> queryStatus();

    List<StationTbl> getTrainStations();

    List<StationLocationTbl> getTrainStationLocations();

    /**
     * Gets all of the current schedules for all trains.
     *
     * @return
     */
    List<TrainScheduleTbl> getSchedules();

    /**
     * Gets all of the current trains in the server.
     *
     * @return
     */
    List<TrainTbl> getTrains();

    /**
     * Gets all of the current statuses.
     *
     * @return
     */
    List<TrainStatusTbl> getStatuses();

    StationTbl getTrainStation(String stationName);

    StationTbl getTrainStation(TrainTbl train);

    /**
     * Gets the next train schedule for the specified train via {@link TrainTbl} class.
     *
     * @param train The {@link TrainTbl} class specified.
     * @return
     */
    TrainScheduleTbl getNextDeparture(TrainTbl train);

    /**
     * Gets the next train schedule for the specified train via Train Name.
     *
     * @param trainName The name of the Train.
     * @return
     */
    TrainScheduleTbl getNextDeparture(String trainName);

    /**
     * Gets a {@link TrainTbl} class based off of its name.
     *
     * @param trainName The name of the train.
     * @return
     */
    TrainTbl getTrain(String trainName);

    /**
     * Gets the train's current status via {@link TrainTbl} class.
     *
     * @param train The {@link TrainTbl} class specified.
     * @return
     */
    TrainStatusTbl getTrainStatus(TrainTbl train);

    /**
     * Gets the train's current status via Train Name.
     *
     * @param trainName The {@link TrainTbl} class specified.
     * @return
     */
    TrainStatusTbl getTrainStatus(String trainName);

}
