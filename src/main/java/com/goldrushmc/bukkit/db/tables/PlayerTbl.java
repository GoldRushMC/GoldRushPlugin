package com.goldrushmc.bukkit.db.tables;

import com.avaje.ebean.validation.NotEmpty;

import javax.persistence.*;

@Entity
@Table(name = "player_tbl")
public class PlayerTbl {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "NAME")
    @NotEmpty
    private String name;
    @Column(name = "POCKET_GOLD")
    private float pocketGold;
    @Column(name = "BANK_GOLD")
    private float bankGold;
    @ManyToOne
    private BankTbl bank;
    @ManyToOne
    private TownTbl town;


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

    public float getPocketGold() {
        return pocketGold;
    }

    public void setPocketGold(float pocketGold) {
        this.pocketGold = pocketGold;
    }

    public float getBankGold() {
        return bankGold;
    }

    public void setBankGold(float bankGold) {
        this.bankGold = bankGold;
    }

    @Transient
    public float getTotalWealth() {
        return pocketGold + bankGold;
    }

    public BankTbl getBank() {
        return bank;
    }

    public void setBank(BankTbl bank) {
        this.bank = bank;
    }

    public TownTbl getTown() {
        return town;
    }

    public void setTown(TownTbl town) {
        this.town = town;
    }

}
