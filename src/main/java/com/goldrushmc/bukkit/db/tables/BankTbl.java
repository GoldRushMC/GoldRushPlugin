package com.goldrushmc.bukkit.db.tables;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "bank_tbl")
public class BankTbl {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "BANK_NAME")
    @NotEmpty
    private String name;
    @Column(name = "CHECKING_INTEREST")
    @NotNull
    private float checkingInterest;
    @Column(name = "CREDIT_INTEREST")
    @NotNull
    private float creditInterest;
    @Column(name = "LOAN_INTEREST")
    @NotNull
    private float loanInterest;
    @OneToMany(mappedBy = "bank")
    private Set<PlayerTbl> customers;
    @OneToOne
    private TownTbl town;
    @OneToMany(mappedBy = "bank")
    private Set<BankLocationTbl> locations;

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

    public float getCheckingInterest() {
        return checkingInterest;
    }

    public void setCheckingInterest(float checkingInterest) {
        this.checkingInterest = checkingInterest;
    }

    public float getCreditInterest() {
        return creditInterest;
    }

    public void setCreditInterest(float creditInterest) {
        this.creditInterest = creditInterest;
    }

    public float getLoanInterest() {
        return loanInterest;
    }

    public void setLoanInterest(float loanInterest) {
        this.loanInterest = loanInterest;
    }

    public Set<PlayerTbl> getCustomers() {
        return customers;
    }

    public void addCustomer(PlayerTbl player) {
        this.customers.add(player);
    }

    public void removeCustomer(PlayerTbl player) {
        this.customers.remove(player);
    }

    public TownTbl getTown() {
        return town;
    }

    public void setTown(TownTbl town) {
        this.town = town;
    }

    /**
     * @param customers the customers to set
     */
    public void setCustomers(Set<PlayerTbl> customers) {
        this.customers = customers;
    }

    public Set<BankLocationTbl> getLocations() {
        return locations;
    }

    public void setLocations(Set<BankLocationTbl> locations) {
        this.locations = locations;
    }
}
