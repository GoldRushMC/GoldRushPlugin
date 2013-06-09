package com.goldrushmc.bukkit.db;

import com.avaje.ebean.validation.NotEmpty;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "train_station_tbl")
public class TrainStationTbl {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "STATION_NAME")
    @NotEmpty
    private String stationName;
    @ManyToOne
    private TownTbl town;
    @OneToMany(mappedBy = "origin")
    private List<TrainScheduleTbl> departures;
    @OneToMany(mappedBy = "destination")
    private List<TrainScheduleTbl> arrivals;
    @OneToMany(mappedBy = "station")
    Set<TrainStationLocationTbl> corners;
    @OneToMany(mappedBy = "station")
    Set<TrainTbl> trains;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Set<TrainStationLocationTbl> getCorners() {
        return corners;
    }

    public void setCorners(Set<TrainStationLocationTbl> corners) {
        this.corners = corners;
    }

    public Set<TrainTbl> getTrains() {
        return trains;
    }

    public void setTrains(Set<TrainTbl> trains) {
        this.trains = trains;
    }

    public TownTbl getTown() {
        return town;
    }

    public void setTown(TownTbl town) {
        this.town = town;
    }

    public List<TrainScheduleTbl> getDepartures() {
        return departures;
    }

    public void addDeparture(TrainScheduleTbl departure) {
        this.departures.add(departure);
    }

    public void removeDeparture(TrainScheduleTbl departure) {
        this.departures.remove(departure);
    }

    public List<TrainScheduleTbl> getArrivals() {
        return arrivals;
    }

    public void addArrival(TrainScheduleTbl arrival) {
        this.departures.add(arrival);
    }

    public void removeArrival(TrainScheduleTbl arrival) {
        this.departures.remove(arrival);
    }

    public void setDepartures(List<TrainScheduleTbl> departures) {
        this.departures = departures;
    }

    public void setArrivals(List<TrainScheduleTbl> arrivals) {
        this.arrivals = arrivals;
    }
}
