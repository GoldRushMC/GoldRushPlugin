package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.Bank;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Lucas
 * Date: 6/17/13
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class DefaultPrompt implements Prompt {

    protected Bank bank;
    protected NPC teller;
    protected Player customer;
    protected static final String WAIT = "WAIT";
    protected static final String CONTINUE = "CONTINUE";
    protected static final String DO_ANOTHER = "CARRY_ON";

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public NPC getTeller() {
        return teller;
    }

    public void setTeller(NPC teller) {
        this.teller = teller;
    }

    public Player getCustomer() {
        return customer;
    }

    public void setCustomer(Player customer) {
        this.customer = customer;
    }
}
