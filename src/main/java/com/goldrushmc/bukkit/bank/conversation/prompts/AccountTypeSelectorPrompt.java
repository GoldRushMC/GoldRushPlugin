package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.Bank;
import com.goldrushmc.bukkit.bank.accounts.Account;
import com.goldrushmc.bukkit.bank.accounts.AccountType;
import com.goldrushmc.bukkit.defaults.conversation.SessionConstants;
import com.goldrushmc.bukkit.defaults.conversation.TryAgainPrompt;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * User: Diremonsoon
 * Date: 6/18/13
 */
public class AccountTypeSelectorPrompt extends AccountPrompt {

    public AccountTypeSelectorPrompt(Bank bank, NPC teller, Player customer) {
        super(bank, teller, customer);
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "Which account type would you like to " + " [ "+ ((String) context.getSessionData(SessionConstants.SELECTION)).toUpperCase() + " ] " + "? \n" +
                "[1] checking \n" +
                "[2] credit \n" +
                "[3] loan";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return context.getSessionData(SessionConstants.ACCOUNT_TYPE) == null;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        AccountType type = null;

        switch(input) {
            case "checking":
                type = AccountType.CHECKING;
                break;
            case "credit":
                type = AccountType.CREDIT;
                break;
            case "loan":
                type = AccountType.LOAN;
                break;
        }

        if(type == null) {
            context.setSessionData(SessionConstants.ERROR, errorPrompt());
            return new TryAgainPrompt(this);
        }

        if(context.getSessionData(SessionConstants.SELECTION).equals("open account")) {
            context.setSessionData(SessionConstants.ACCOUNT_TYPE, type);
            return new OpenAccountPrompt(bank, teller, customer);
        }

        for(Account a : accounts) {
            if(a.getAccountType().equals(type)) {
                context.setSessionData(SessionConstants.ACCOUNT_TYPE, type);
                break;
            }
        }

        if(context.getSessionData(SessionConstants.ACCOUNT_TYPE) != null) {
            return new AccountSelectorPrompt(bank, teller, customer);
        }
        else  {
            context.setSessionData(SessionConstants.ERROR, "You do not have an account of this type.");
            return new TryAgainPrompt(this);
        }
    }

    @Override
    public String errorPrompt() {
        return "That is not a valid account type. Please try again.";
    }
}
