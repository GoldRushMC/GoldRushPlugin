package com.goldrushmc.bukkit.trainstation.scheduling;

import com.goldrushmc.bukkit.trainstation.TrainStation;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Deprecated
public class Departure implements Runnable {

    //	private final IStationAccessible db;
    private static int taskID = -1;

    public Departure(final JavaPlugin plugin) {
//		this.db = new DBStationsAccess(plugin);
    }

    @Override
    public void run() {
        List<TrainStation> stations = TrainStation.getTrainStations();
        //If the list is null, we can't quite send trains on their way!
        if (stations == null) return;
        //For each types, send the next trainstation on its way, and bring the next forward.
        for (TrainStation station : stations) {
            station.pushQueue();
//			if(!hasTrain) Bukkit.broadcastMessage(types.getStationName() + " has no trains to depart...");
        }
    }


    public static int getTaskID() {
        return taskID;
    }

    public static void resetTaskID() {
        taskID = -1;
    }

    public static void setTaskID(int id) {
        taskID = id;
    }
}
