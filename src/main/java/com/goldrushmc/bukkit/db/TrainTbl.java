package com.goldrushmc.bukkit.db;

import com.avaje.ebean.validation.NotEmpty;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "train_tbl")
public class TrainTbl {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "TRAIN_NAME")
    @NotEmpty
    private String name;
    @Column(name = "WORLD")
    @NotEmpty
    private String worldName;
    @OneToMany(mappedBy = "train")
    private Set<CartListTbl> carts;
    @OneToMany(mappedBy = "train")
    private Set<TrainScheduleTbl> schedule;
    @OneToOne
    private TrainStatusTbl status;
    @ManyToOne
    private TrainStationTbl station;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrainName() {
        return name;
    }

    public void setTrainName(String trainName) {
        this.name = trainName;
    }

    public Set<TrainScheduleTbl> getSchedule() {
        return schedule;
    }

    public void setSchedule(Set<TrainScheduleTbl> schedule) {
        this.schedule = schedule;
    }

    public TrainStatusTbl getStatus() {
        return status;
    }

    public void setStatus(TrainStatusTbl status) {
        this.status = status;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public TrainStationTbl getStation() {
        return station;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStation(TrainStationTbl station) {
        this.station = station;
    }

    /**
     * @return the carts
     */
    public Set<CartListTbl> getCarts() {
        return carts;
    }

    /**
     * @param carts the carts to set
     */
    public void setCarts(Set<CartListTbl> carts) {
        this.carts = carts;
    }
}
