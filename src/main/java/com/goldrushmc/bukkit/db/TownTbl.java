package com.goldrushmc.bukkit.db;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "town_tbl")
public class TownTbl {

	@Id @GeneratedValue private int id;
	@Column(name = "TOWN_NAME") @NotEmpty private String name;
	@Column(name = "TAX") private int tax;
	@Column(name = "LEVEL") @NotNull private int level;
	@Column(name = "GOLD_HELD") private float goldHeld;
	@OneToOne private BankTbl bank;
	@OneToMany(mappedBy = "town") private Set<PlayerTbl> citizens;
	
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
	public int getTax() {
		return tax;
	}
	public void setTax(int tax) {
		this.tax = tax;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public float getGoldHeld() {
		return goldHeld;
	}
	public void setGoldHeld(float goldHeld) {
		this.goldHeld = goldHeld;
	}
	public int getPopulation() {
		return citizens.size();
	}
	public BankTbl getBank() {
		return bank;
	}
	public void setBank(BankTbl bank) {
		this.bank = bank;
	}
	public Set<PlayerTbl> getCitizens() {
		return citizens;
	}

	public void addCitizen(PlayerTbl player) {
		this.citizens.add(player);
	}
	public void removeCitizen(PlayerTbl player) {
		this.citizens.remove(player);
	}
	public void setCitizens(Set<PlayerTbl> citizens) {
		this.citizens = citizens;
	}
}
