package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.Bank;
import com.goldrushmc.bukkit.bank.accounts.Account;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * User: Diremonsoon
 * Date: 6/18/13
 * Time: 2:03 PM
 */
public abstract class AccountPrompt extends DefaultPrompt{

    protected List<Account> accounts;

    public AccountPrompt(Bank bank, NPC teller, Player customer) {
        super(bank, teller, customer);
        accounts = bank.getAccounts(customer);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }


}
