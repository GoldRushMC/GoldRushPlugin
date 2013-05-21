package com.goldrushmc.bukkit.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "train_schedule_tbl")
public class TrainScheduleTbl {

	@Id @GeneratedValue private int id;
	@Column(name = "DEPART_TIME") @NotNull private long timeToDepart;
	@ManyToOne TrainStationTbl destination;
	@ManyToOne TrainStationTbl origin;
	@ManyToOne @Column(name = "NEXT_STOP") TrainStationTbl station;
	@Column(name = "IS_NEXT") private boolean isNext;
	@OneToOne private TrainTbl train;
	
	
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
	public TrainStationTbl getStation() {
		return station;
	}
	public void setStation(TrainStationTbl station) {
		this.station = station;
	}
	public TrainStationTbl getDestination() {
		return destination;
	}
	public void setDestination(TrainStationTbl destination) {
		this.destination = destination;
	}
	public TrainStationTbl getOrigin() {
		return origin;
	}
	public void setOrigin(TrainStationTbl origin) {
		this.origin = origin;
	}
}
