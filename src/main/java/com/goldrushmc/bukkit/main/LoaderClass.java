package com.goldrushmc.bukkit.main;

import com.goldrushmc.bukkit.db.access.DBMinesAccess;
import com.goldrushmc.bukkit.db.access.DBMinesAccessible;
import com.goldrushmc.bukkit.db.access.DBStationsAccess;
import com.goldrushmc.bukkit.db.access.IStationAccessible;
import com.goldrushmc.bukkit.db.tables.MineLocationTbl;
import com.goldrushmc.bukkit.db.tables.MinesTbl;
import com.goldrushmc.bukkit.db.tables.StationLocationTbl;
import com.goldrushmc.bukkit.db.tables.StationTbl;
import com.goldrushmc.bukkit.mines.Mine;
import com.goldrushmc.bukkit.trainstation.TrainStation;
import com.goldrushmc.bukkit.trainstation.exceptions.MarkerNumberException;
import com.goldrushmc.bukkit.trainstation.types.PublicTrainStation;
import com.goldrushmc.bukkit.trainstation.types.TrainStationTransport;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Diremonsoon
 * Date: 6/17/13
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoaderClass implements Runnable {

    private IStationAccessible dbStation;
    private DBMinesAccessible dbMine;
    private JavaPlugin p;

    public LoaderClass(JavaPlugin p) {
        this.p = p;
        //Make sure the DB accessors are instantiated
        //If they are not, instantiate them.
        if(TrainStation.getDb() == null) TrainStation.db = new DBStationsAccess(p);
        if(Mine.getDB() == null) Mine.db = new DBMinesAccess(p);

        //Map the references for easier access.
        dbStation = TrainStation.getDb();
        dbMine = Mine.getDB();
    }

    public void run() {
        //Get lists of the stations and mines, if they exist.
        List<StationTbl> stations = dbStation.getTrainStations();
        List<MinesTbl> mines = dbMine.getMines();

        //Start the loading of Stations!
        if(stations != null) {
            for(StationTbl s : stations) {

                Set<StationLocationTbl> locs = s.getLocations();
                List<Location> markers = new ArrayList<>();
                if(locs != null) {
                    //Convert locations to a usable list.
                    for(StationLocationTbl locTbl : locs) {
                        markers.add(new Location(Bukkit.getWorld(locTbl.getWorld()), locTbl.getX(), locTbl.getY(), locTbl.getZ()));
                    }
                }
                //Measure how many stations we loaded.
                int loadedBack = 0;

                //Attempt to create the trainstation stations.
                try{
                    switch(s.getType()) {
                        case PASSENGER_TRANS:
                            new PublicTrainStation(p, s.getName(), markers, markers.get(0).getWorld(), !s.isTrainInStation(), false);
                            loadedBack++;
                            break;
                        case STORAGE_TRANS:
                            new TrainStationTransport(p, s.getName(), markers, markers.get(0).getWorld(), !s.isTrainInStation(), false);
                            loadedBack++;
                            break;
                    }
                } catch (Exception e) {
                    if(s.getName() != null) {
                        Bukkit.getLogger().warning("Could not load Station " + s.getName() + " due to problems in the DB!");
                    } else {
                        Bukkit.getLogger().warning("Could not load the trainstation types... Name was missing, as well as other things from the DB.");
                    }
                    //Signify an error.
                    loadedBack = -1;
                }

                if(loadedBack != -1) Bukkit.getLogger().info(p.getName() + " [ " + loadedBack + " ] " + " Stations have been loaded successfully!");
            }

        } else { Bukkit.getLogger().info(p.getName() + " :: No Stations to load!"); }

        //Start the loading of Mines!
        if(mines != null) {
           for(MinesTbl mineEnt : mines) {
               //map list values to variables for use in mine creation
               String name = mineEnt.getName();
               Set<MineLocationTbl> locs = mineEnt.getLocations();
               String worldName = "";
               ArrayList<Location> markers = new ArrayList<>();
               Vector entrance = null;
               if(locs != null) {
                   for(MineLocationTbl l : locs) {
                       if(worldName.equals("")) worldName = l.getWorld();

                       if(!l.isEntrance()) markers.add(new Location(Bukkit.getWorld(l.getWorld()), l.getX(), l.getY(), l.getZ()));
                       else entrance = new Vector(l.getX(), l.getY(), l.getZ());
                   }
               }
               int density = mineEnt.getDensity();
               Boolean isGen = mineEnt.isGenerated();

               int loadedBack = 0;
               //attempt to recreate the mine into a temporary mine variable
               try {
                   new Mine(name, markers.get(0).getWorld(), markers, p, entrance, density, isGen);
                   loadedBack++;
               } catch (MarkerNumberException e) {
                   loadedBack = -1;
                   p.getLogger().info("Error creating Mine, incorrect number of Coords!");
               }

               if(loadedBack != -1) Bukkit.getLogger().info(p.getName() + " [ " + loadedBack + " ] " + " Mines have been loaded successfully!");
           }
        } else { Bukkit.getLogger().info(p.getName() + " :: No Mines to load!"); }
    }
}
