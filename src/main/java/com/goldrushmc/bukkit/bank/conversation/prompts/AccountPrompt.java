package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.accounts.Account;

import java.util.List;

/**
 * User: Lucas
 * Date: 6/18/13
 * Time: 2:03 PM
 */
public abstract class AccountPrompt extends DefaultPrompt{

    protected List<Account> accounts;

    public AccountPrompt() {
        accounts = bank.getAccounts(customer);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
