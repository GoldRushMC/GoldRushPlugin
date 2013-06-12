package com.goldrushmc.bukkit.db;

import com.avaje.ebean.validation.NotEmpty;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Lex
 * Date: 12/06/13
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "mines_tbl")
public class MinesTbl {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "MINE_NAME")
    @NotEmpty
    private String name;
    @Column(name = "WORLD_NAME")
    @NotEmpty
    private String worldName;
    @Column(name = "LOC_ONE")
    @NotEmpty
    private Location locOne;
    @Column(name = "LOC_TWO")
    @NotEmpty
    private Location locTwo;
    @Column(name = "ENTRANCE_POS")
    @NotEmpty
    private Vector entrancePos;
    @Column(name = "DENSITY")
    @NotEmpty
    private Integer density;
    @Column(name = "IS_GEN")
    @NotEmpty
    private Boolean generated;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public Location getLocOne() {
        return locOne;
    }

    public void setLocOne(Location locOne) {
        this.locOne = locOne;
    }

    public Location getLocTwo() {
        return locTwo;
    }

    public void setLocTwo(Location locTwo) {
        this.locTwo = locTwo;
    }

    public Vector getEntrancePos() {
        return entrancePos;
    }

    public void setEntrancePos(Vector entrancePos) {
        this.entrancePos = entrancePos;
    }

    public Integer getDensity() {
        return density;
    }

    public void setDensity(Integer density) {
        this.density = density;
    }

    public Boolean getGenerated() {
        return generated;
    }

    public void setGenerated(Boolean generated) {
        this.generated = generated;
    }
}
