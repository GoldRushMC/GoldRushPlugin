package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.accounts.Account;
import com.goldrushmc.bukkit.bank.accounts.CheckingAccount;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * User: Diremonsoon
 * Date: 6/20/13
 */
public class OpenAccountPrompt extends DefaultPrompt{

    private Account newAccount;

    @Override
    public String getPromptText(ConversationContext context) {

        if(context.getSessionData("ACCOUNT_TYPE").equals(Account.AccountType.CHECKING)) {
            context.setSessionData("AUTO_NAME", false);
            return "What would you like to name your account?";
        } else {
            context.setSessionData("AUTO_NAME", true);
            return "Creating your account...";
        }
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return context.getSessionData("ACCOUNT") == null || context.getSessionData("AUTO_NAME").equals(false);
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        newAccount = new CheckingAccount(0, customer, bank, bank.getCheckingInterest());
        bank.openAccount(customer, newAccount);

        return new ContinuePrompt();
    }

    @Override
    public String errorPrompt() {
        return "That is not possible.";
    }
}
