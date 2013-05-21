package com.goldrushmc.bukkit.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "station_location_tbl")
public class TrainStationLocationTbl {

	@Id @GeneratedValue private int id;
	@Column(name = "CORNER") @NotEmpty private String corner;
	@Column(name = "X") @NotNull private double x;
	@Column(name = "Y") @NotNull private double y;
	@Column(name = "Z") @NotNull private double z;
	@OneToOne private TrainStationTbl station;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCorner() {
		return corner;
	}
	public void setCorner(String corner) {
		this.corner = corner;
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
	public TrainStationTbl getStation() {
		return station;
	}
	public void setStation(TrainStationTbl station) {
		this.station = station;
	}
}
