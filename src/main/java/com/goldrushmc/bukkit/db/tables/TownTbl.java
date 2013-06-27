package com.goldrushmc.bukkit.db.tables;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "town_tbl")
public class TownTbl {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "TOWN_NAME")
    @NotEmpty
    private String name;
    @Column(name = "TAX")
    private int tax;
    @Column(name = "LEVEL")
    @NotNull
    private int level;
    @Column(name = "GOLD_HELD")
    private float goldHeld;
    @OneToMany(mappedBy = "town")
    private Set<BankTbl> banks;
    @OneToMany(mappedBy = "town")
    private Set<PlayerTbl> citizens;

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

    public Set<BankTbl> getBanks() {
        return banks;
    }

    public void setBanks(Set<BankTbl> banks) {
        this.banks = banks;
    }

    public void addBank(BankTbl bank) {
        if(this.banks == null) this.banks = new HashSet<>();
        this.banks.add(bank);
    }

    public void removeBank(BankTbl bank) {
        if(this.banks == null) return;
        this.banks.remove(bank);
    }

    public Set<PlayerTbl> getCitizens() {
        return citizens;
    }

    public void setCitizens(Set<PlayerTbl> citizens) {
        this.citizens = citizens;
    }

    public void addCitizen(PlayerTbl player) {
        if(this.citizens == null) this.citizens = new HashSet<>();
        this.citizens.add(player);
    }

    public void removeCitizen(PlayerTbl player) {
        if(this.citizens == null) return;
        this.citizens.remove(player);
    }

    public boolean hasBanks() {
        return !banks.isEmpty();
    }

    public boolean hasCitizens() {
        return !citizens.isEmpty();
    }
}
