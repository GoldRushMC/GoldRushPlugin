package com.goldrushmc.bukkit.db;

import com.avaje.ebean.validation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "location_tbl")
public class LocationTbl {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "X")
    @NotNull
    private double x;
    @Column(name = "Y")
    @NotNull
    private double y;
    @Column(name = "Z")
    @NotNull
    private double z;
    @OneToOne
    private BlockFinderTbl object;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public BlockFinderTbl getStation() {
        return object;
    }

    public void setStation(BlockFinderTbl object) {
        this.object = object;
    }
}
