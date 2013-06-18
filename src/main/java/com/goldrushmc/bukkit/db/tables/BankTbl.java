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
    @Column(name = "INTEREST")
    @NotNull
    private float interest;
    @OneToMany(mappedBy = "bank")
    private Set<PlayerTbl> customers;
    @OneToOne
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

    public float getInterest() {
        return interest;
    }

    public void setInterest(float interest) {
        this.interest = interest;
    }

    @Transient
    public float getTotalGold() {
        float totalGold = 0;
        if (customers != null) {
            for (PlayerTbl player : customers) {
                totalGold += player.getBankGold();
            }
        }
        if (town != null) totalGold += town.getGoldHeld();

        return totalGold;
    }

    @Transient
    public int getSumOfAccounts() {
        return customers.size();
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

}
