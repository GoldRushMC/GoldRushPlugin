package com.goldrushmc.bukkit.db.tables;

import com.avaje.ebean.validation.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class PlayerTbl {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "NAME")
    @NotEmpty
    private String name;
    @Column(name = "ONLINE")
    private boolean online;
    @ManyToOne
    private TownTbl town;
    @OneToMany(mappedBy = "holder")
    private Set<AccountTbl> accounts;


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

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public TownTbl getTown() {
        return town;
    }

    public void setTown(TownTbl town) {
        this.town = town;
    }

    public Set<AccountTbl> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountTbl> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(AccountTbl account) {
        if(this.accounts == null) this.accounts = new HashSet<>();
        this.accounts.add(account);
    }

    public void removeAccount(AccountTbl account) {
        if(this.accounts == null) return;
        this.accounts.remove(account);
    }

    public int getTotalWorth() {
        int worth = 0;
        if(accounts != null) {
            for(AccountTbl a : accounts) {
                worth += a.getBalance();
            }
        }
        return worth;
    }
}
