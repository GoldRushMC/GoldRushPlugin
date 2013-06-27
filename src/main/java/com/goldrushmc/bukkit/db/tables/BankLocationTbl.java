package com.goldrushmc.bukkit.db.tables;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * User: Diremonsoon
 * Date: 6/24/13
 */
@Entity
public class BankLocationTbl extends DefaultLocationTbl {

    @ManyToOne
    private BankTbl bank;

    public BankTbl getBank() {
        return bank;
    }

    public void setBank(BankTbl bank) {
        this.bank = bank;
    }
}
