package com.goldrushmc.bukkit.db.tables;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class StationLocationTbl extends DefaultLocationTbl{

    @ManyToOne
    private StationTbl station;

    public StationTbl getStation() {
        return station;
    }

    public void setStation(StationTbl station) {
        this.station = station;
    }
}
