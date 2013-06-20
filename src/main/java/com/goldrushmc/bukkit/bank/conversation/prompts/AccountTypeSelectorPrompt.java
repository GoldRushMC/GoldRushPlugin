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
        return "Which account type would you like to " + " [ "+ context.getSessionData("SELECTION") + " ] " + "?";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return context.getSessionData("ACCOUNT_TYPE") == null;
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

        if(type == null) {
            context.setSessionData("ERROR", errorPrompt());
            return new TryAgainPrompt(this);
        }

        if(context.getSessionData("SELECTION").equals("open account")) {
            context.setSessionData("ACCOUNT_TYPE", type);
            return new OpenAccountPrompt();
        }

        for(Account a : accounts) {
            if(a.getAccountType().equals(type)) {
                context.setSessionData("ACCOUNT_TYPE", type);
                break;
            }
        }

        if(context.getSessionData("ACCOUNT_TYPE") != null) {
            return new AccountSelectorPrompt();
        }
        else  {
            context.setSessionData("ERROR", "You do not have an account of this type.");
            return new TryAgainPrompt(this);
        }
    }

    @Override
    public String errorPrompt() {
        return "That is not a valid account type. Please try again.";
    }
}
