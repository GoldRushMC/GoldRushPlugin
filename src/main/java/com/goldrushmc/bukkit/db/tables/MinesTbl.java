package com.goldrushmc.bukkit.db.tables;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

import javax.persistence.*;
import java.util.Set;

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
    @Column(name = "DENSITY")
    @NotNull
    private int density;
    @Column(name = "IS_GEN")
    @NotNull
    private boolean generated;
    @OneToMany(mappedBy = "mine")
    private Set<MineLocationTbl> locations;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MineLocationTbl> getLocations() {
        return locations;
    }

    public void setLocations(Set<MineLocationTbl> locations) {
        this.locations = locations;
    }

    public int getDensity() {
        return density;
    }

    public void setDensity(int density) {
        this.density = density;
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public void addMineLocation(MineLocationTbl loc) {
        locations.add(loc);
    }

    public void removeMineLocation(MineLocationTbl loc) {
        locations.remove(loc);
    }
}
