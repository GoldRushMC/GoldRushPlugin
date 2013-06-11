package com.goldrushmc.bukkit.train.scheduling;

import com.goldrushmc.bukkit.train.signs.SignType;
import com.goldrushmc.bukkit.train.station.TrainStation;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Should be set to run every tick, for each world it is in.
 * <p/>
 * Controls the scheduling of trains in a more configurable way.
 *
 * @author Diremonsoon
 */
public class TimeCounter implements Runnable {

    public static boolean reset = false;
    public volatile long timeCount = 0;
    public volatile long currentTime = 0, publik = 0, transport = 0, hub = 0, hybrid = 0;
    private final JavaPlugin plugin;
    private Map<String, Long> times = new HashMap<>();
    private Map<String, List<TrainStation>> stations = new HashMap<>();

    private final World world;

    /**
     * Make sure that the plugin already has a config before initializing this class!!!
     *
     * @param plugin
     * @param world
     */
    public TimeCounter(JavaPlugin plugin, World world) {
        this.world = world;
        this.plugin = plugin;
        //Initialize Lists
        stations.put("Public", new ArrayList<TrainStation>());
        stations.put("Transport", new ArrayList<TrainStation>());
        stations.put("Hybrid", new ArrayList<TrainStation>());
        stations.put("Hub", new ArrayList<TrainStation>());
        //Load config
        FileConfiguration fc = plugin.getConfig();
        if (fc != null) {
            //Corrects the time to minutes, so that we don't have things running at a tick time.
            int timeCorrector = 1200;
            times.put("Public", fc.getLong("station.times.public") * timeCorrector);
            times.put("Transport", fc.getLong("station.times.transport") * timeCorrector);
            times.put("Hub", fc.getLong("station.times.hub") * timeCorrector);
            times.put("Hybrid", fc.getLong("station.times.hybrid") * timeCorrector);
        }
    }

    /**
     * This is meant to run EVERY tick, so we don't want to do too much.
     */
    @Override
    public void run() {

        //Get current time
        long update = this.world.getTime();

        //if the time is changed, we will know. This also applies for new days.
        if ((this.timeCount + 1) != update) reset();

        this.timeCount = update;

        if (reset) {
            this.publik = this.times.get("Public");
            this.transport = this.times.get("Transport");
            this.hub = this.times.get("Hub");
            this.hybrid = this.times.get("Hybrid");
            this.currentTime = 0;
            this.timeCount = 0;
            reset = false;
        }

        //Every 5 minutes, do this.
        if (Math.abs(update - currentTime) == 6000) {
            onUpdateTick();
            this.currentTime = update;
        }

        //Update the departure times for each station. This will be a bit expensive. Every minute, we update ALL time signs.
        if (Math.abs(update - this.currentTime) == 1200) {
            for (TrainStation station : this.stations.get("Public")) {
                if (station.hasDepartingTrain()) {
                    List<Sign> signs = station.getSigns().getSigns(SignType.TRAIN_STATION_TIME);
                    long timeSet = this.times.get("Public");
                    station.updateDepartureTime(0);
                }
            }
            for (TrainStation station : this.stations.get("Transport")) {

            }
            for (TrainStation station : this.stations.get("Hybrid")) {
                if (station.hasDepartingTrain()) {
                    List<Sign> signs = station.getSigns().getSigns(SignType.TRAIN_STATION_TIME);
                    long timeSet = this.times.get("Hybrid");

                }
            }
        }

        if (Math.abs(update - this.publik) == this.times.get("Public")) {
            if (!this.stations.get("Public").isEmpty()) {
                for (TrainStation station : this.stations.get("Public")) {
                    station.pushQueue();
                }
            }
            this.publik = update;
        }
        if (Math.abs(update - this.transport) == this.times.get("Transport")) {
            if (!this.stations.get("Transport").isEmpty()) {
                Bukkit.getLogger().info("Sending Transport Trains!"); //TODO
                for (TrainStation station : this.stations.get("Transport")) {
                    station.pushQueue();
                }
            }
            this.transport = update;
        }
        if (Math.abs(update - this.hybrid) == this.times.get("Hybrid")) {
            if (!this.stations.get("Hybrid").isEmpty()) {
            }
            hybrid = update;
        }
    }

    public void onUpdateTick() {
        List<TrainStation> stations = TrainStation.getTrainStations();
        //If no stations exist, we don't run an update.
        if (stations.isEmpty()) return;

        boolean add = true;
        for (TrainStation station : stations) {
            for (List<TrainStation> stationList : this.stations.values()) {
                if (stationList.contains(station)) {
                    add = false;
                    continue;
                }
            }
            if (add) {
                //TODO Need to add a Hub station class, and add logic to add it to scheduling.
                switch (station.getType()) {
                    case DEFAULT:
                        break;
                    case PASSENGER_TRANS:
                        this.stations.get("Public").add(station);
                        break;
                    case STORAGE_TRANS:
                        this.stations.get("Transport").add(station);
                        break;
                    case HYBRID_TRANS:
                        this.stations.get("Hybrid").add(station);
                }
            }
        }
    }

    public static void reset() {
        reset = true;
    }
}
