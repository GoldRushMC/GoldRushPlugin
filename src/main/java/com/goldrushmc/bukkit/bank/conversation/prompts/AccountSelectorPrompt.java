package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.Bank;
import com.goldrushmc.bukkit.bank.accounts.Account;
import com.goldrushmc.bukkit.bank.accounts.AccountType;
import com.goldrushmc.bukkit.defaults.conversation.SessionConstants;
import com.goldrushmc.bukkit.defaults.conversation.TryAgainPrompt;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * User: Diremonsoon
 * Date: 6/20/13
 */
public class AccountSelectorPrompt extends AccountPrompt{

    public AccountSelectorPrompt(Bank bank, NPC teller, Player customer) {
        super(bank, teller, customer);
    }

    @Override
    public String getPromptText(ConversationContext context) {

        String select = "Which account would you like to choose from? \n";

        int i = 1;
        AccountType type = null;
        for(Account a : accounts) {
            //
            if(type == null || !a.getAccountType().equals(type)) {
                type = a.getAccountType();
                select += "\n" + ChatColor.UNDERLINE + type.toString().toUpperCase() + ":" + ChatColor.RESET + "\n";
            }
            select += "[" + i + "] " + a.accountName() + "\n";
            i++;
        }
        return select;
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return true;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        for(Account a : accounts) {
            if(a.accountName().equalsIgnoreCase(input)) {
                context.setSessionData(SessionConstants.ACCOUNT, a);
                return (Prompt) context.getSessionData(SessionConstants.NEXT_PROMPT);
            }
        }

        context.setSessionData(SessionConstants.ERROR, errorPrompt());
        return new TryAgainPrompt(this);
    }

    @Override
    public String errorPrompt() {
        return "There is no account by that name. Please try another.";
    }
}
