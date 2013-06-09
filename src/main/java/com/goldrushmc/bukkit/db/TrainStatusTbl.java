package com.goldrushmc.bukkit.db;

import com.avaje.ebean.validation.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "status_tbl")
public class TrainStatusTbl {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "STATUS")
    @NotNull
    private String status;
    @OneToMany(mappedBy = "status")
    private Set<TrainTbl> trains;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<TrainTbl> getTrains() {
        return trains;
    }

    public void setTrains(Set<TrainTbl> trains) {
        this.trains = trains;
    }
}
