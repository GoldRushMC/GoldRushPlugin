package com.goldrushmc.bukkit.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cart_list_tbl")
public class CartListTbl {

	private enum CartType {STORAGE, RIDE}
	
	@Id @GeneratedValue private int id;
	@ManyToOne private PlayerTbl owner;
	@ManyToOne private TrainTbl train;
	@Enumerated @Column(name = "CART_TYPE") CartType type;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the owner
	 */
	public PlayerTbl getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(PlayerTbl owner) {
		this.owner = owner;
	}
	/**
	 * @return the train
	 */
	public TrainTbl getTrain() {
		return train;
	}
	/**
	 * @param train the train to set
	 */
	public void setTrain(TrainTbl train) {
		this.train = train;
	}
	/**
	 * @return the type
	 */
	public CartType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(CartType type) {
		this.type = type;
	}
	
}
