package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.accounts.Account;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * User: Diremonsoon
 * Date: 6/18/13
 */
public class AccountTypeSelectorPrompt extends AccountPrompt {

    @Override
    public String getPromptText(ConversationContext context) {
        return "Which account would you like to " + " [ "+ context.getSessionData("SELECTION") + " ] " + "?";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return context.getSessionData("ACCOUNT") != null;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Account.AccountType type = null;

        switch(input) {
            case "checking":
                type = Account.AccountType.CHECKING;
            case "credit":
                type = Account.AccountType.CREDIT;
            case "loan":
                type = Account.AccountType.LOAN;
        }

        if(type == null) return new TryAgainPrompt(this);


        for(Account a : accounts) {
            if(a.getAccountType().equals(type)) context.setSessionData("ACCOUNT", a);
        }

        return null;
    }
}
