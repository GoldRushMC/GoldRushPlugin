package com.goldrushmc.bukkit.db;

import com.avaje.ebean.validation.NotEmpty;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "block_finder_tbl")
public class BlockFinderTbl {

    public enum ObjectType {MINE, TOWN, STATION, BANK}

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "NAME")
    @NotEmpty
    private String objectName;
    @Column(name = "TYPE")
    @NotEmpty
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;
    @OneToMany(mappedBy = "object")
    Set<StationLocationTbl> locations;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public Set<StationLocationTbl> getLocations() {
        return locations;
    }

    public void setLocations(Set<StationLocationTbl> locations) {
        this.locations = locations;
    }

    public void addLocation(StationLocationTbl location) {
        this.locations.add(location);
    }

    public void removeLocation(StationLocationTbl location) {
        this.locations.remove(location);
    }

}
