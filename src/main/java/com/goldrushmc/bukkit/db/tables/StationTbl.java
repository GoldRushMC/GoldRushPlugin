package com.goldrushmc.bukkit.db.tables;

import com.avaje.ebean.validation.NotNull;
import com.goldrushmc.bukkit.trainstation.StationType;

import javax.persistence.*;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Lucas
 * Date: 6/17/13
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "station_tbl")
public class StationTbl {

    @Id
    @GeneratedValue
    private int id;
    @NotNull
    @Column(name = "STATION_NAME")
    private String name;
    @NotNull
    @Column(name = "HAS_TRAIN")
    private boolean trainInStation;
    @NotNull
    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private StationType type;
    @OneToMany(mappedBy = "station")
    private Set<StationLocationTbl> locations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTrainInStation() {
        return trainInStation;
    }

    public void setTrainInStation(boolean trainInStation) {
        this.trainInStation = trainInStation;
    }

    public Set<StationLocationTbl> getLocations() {
        return locations;
    }

    public void setLocations(Set<StationLocationTbl> locations) {
        this.locations = locations;
    }

    public void addLocation(StationLocationTbl loc) {
        locations.add(loc);
    }

    public void removeLocation(StationLocationTbl loc) {
        locations.remove(loc);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StationType getType() {
        return type;
    }

    public void setType(StationType type) {
        this.type = type;
    }
}
