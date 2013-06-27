package com.goldrushmc.bukkit.db.tables;

import com.goldrushmc.bukkit.bank.accounts.AccountType;

import javax.persistence.*;

/**
 * User: Diremonsoon
 * Date: 6/27/13
 */
@Entity
public class AccountTbl {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "ACCOUNT_NAME")
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_TYPE")
    private AccountType type;
    @Column(name = "BALANCE")
    private int balance;
    @Column(name = "INTEREST")
    private int interest;
    @ManyToOne
    private PlayerTbl holder;
    @ManyToOne
    private BankTbl bank;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public PlayerTbl getHolder() {
        return holder;
    }

    public void setHolder(PlayerTbl holder) {
        this.holder = holder;
    }

    public BankTbl getBank() {
        return bank;
    }

    public void setBank(BankTbl bank) {
        this.bank = bank;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }
}
