package com.goldrushmc.bukkit.db;

import com.avaje.ebean.validation.NotNull;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created with IntelliJ IDEA.
 * User: Lucas
 * Date: 6/17/13
 * Time: 1:03 PM
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public class DefaultLocationTbl {

    @Id
    @GeneratedValue
    protected int id;
    @NotNull
    @Column(name = "X")
    protected double x;
    @NotNull
    @Column(name = "Y")
    protected double y;
    @NotNull
    @Column(name = "Z")
    protected double z;
    @NotNull
    @Column(name = "WORLD")
    protected String world;

    public void initLocation(Location loc) {
        x = loc.getX();
        y = loc.getY();
        z = loc.getZ();
        world = loc.getWorld().getName();
    }

    public void initVector(Vector vector) {
        x = vector.getX();
        y = vector.getY();
        z = vector.getZ();
        world = "N/A";
    }

    public void initBlock(Block b) {
        x = b.getX();
        y = b.getY();
        z = b.getZ();
        world = b.getWorld().getName();
    }


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

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }
}
