package com.goldrushmc.bukkit.db;

import com.avaje.ebean.validation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "train_schedule_tbl")
public class TrainScheduleTbl {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "DEPART_TIME")
    @NotNull
    private long timeToDepart;
    @ManyToOne
    BlockFinderTbl destination;
    @ManyToOne
    BlockFinderTbl origin;
    @ManyToOne
    @Column(name = "NEXT_STOP")
    BlockFinderTbl station;
    @Column(name = "IS_NEXT")
    private boolean isNext;
    @OneToOne
    private TrainTbl train;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimeToDepart() {
        return timeToDepart;
    }

    public void setTimeToDepart(long timeToDepart) {
        this.timeToDepart = timeToDepart;
    }

    public TrainTbl getTrain() {
        return train;
    }

    public void setTrain(TrainTbl train) {
        this.train = train;
    }

    public boolean isNext() {
        return isNext;
    }

    public void setNext(boolean isNext) {
        this.isNext = isNext;
    }

    public BlockFinderTbl getStation() {
        return station;
    }

    public void setStation(BlockFinderTbl station) {
        this.station = station;
    }

    public BlockFinderTbl getDestination() {
        return destination;
    }

    public void setDestination(BlockFinderTbl destination) {
        this.destination = destination;
    }

    public BlockFinderTbl getOrigin() {
        return origin;
    }

    public void setOrigin(BlockFinderTbl origin) {
        this.origin = origin;
    }
}
