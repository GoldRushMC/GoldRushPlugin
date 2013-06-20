package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.accounts.Account;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Diremonsoon
 * Date: 6/20/13
 */
public class AccountSelectorPrompt extends AccountPrompt{

    List<Account> sameTypes = new ArrayList<>();

    @Override
    public String getPromptText(ConversationContext context) {

        String select = "Which account would you like to choose from? \n";

        int i = 1;
        for(Account a : accounts) {
            if(a.getAccountType().equals(context.getSessionData("ACCOUNT_TYPE"))) {
                sameTypes.add(a);
                select += i + ". " + a.accountName() + "\n";
                i++;
            }
        }

        return select;
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return context.getSessionData("ACCOUNT") == null;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        for(Account a : sameTypes) {
            if(a.accountName().equalsIgnoreCase(input)) {
                context.setSessionData("ACCOUNT", a);
                return (Prompt) context.getSessionData("NEXT_PROMPT");
            }
        }

        context.setSessionData("ERROR", errorPrompt());
        return new TryAgainPrompt(this);
    }

    @Override
    public String errorPrompt() {
        return "There is no account by that name. Please try another.";
    }
}
