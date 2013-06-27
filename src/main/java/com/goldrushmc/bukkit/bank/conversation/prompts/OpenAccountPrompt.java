package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.Bank;
import com.goldrushmc.bukkit.bank.accounts.Account;
import com.goldrushmc.bukkit.bank.accounts.AccountType;
import com.goldrushmc.bukkit.bank.accounts.CheckingAccount;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * User: Diremonsoon
 * Date: 6/20/13
 */
public class OpenAccountPrompt extends DefaultPrompt{

    public OpenAccountPrompt(Bank bank, NPC teller, Player customer) {
        super(bank, teller, customer);
    }

    @Override
    public String getPromptText(ConversationContext context) {
        if(context.getSessionData(SessionConstants.ACCOUNT_TYPE).equals(AccountType.CHECKING)) {
            context.setSessionData(SessionConstants.AUTO_NAME, false);
            return "What would you like to name your account?";
        } else {
            context.setSessionData(SessionConstants.AUTO_NAME, true);
            return "Creating your account...";
        }
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return context.getSessionData(SessionConstants.AUTO_NAME).equals(false);
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        if(input.equals("")) {
            context.setSessionData(SessionConstants.ERROR, errorPrompt());
            return new TryAgainPrompt(this);
        }

        if(context.getSessionData(SessionConstants.AUTO_NAME).equals(false)) {
            Account newAccount = new CheckingAccount(input, 0, customer, bank, bank.getCheckingInterest());
            bank.openAccount(customer, newAccount);
            context.setSessionData(SessionConstants.OUTCOME, "Created account " + newAccount.accountName() + " with bank " + bank.getName());
        }
        return new OutcomePrompt(bank, teller, customer);
    }

    @Override
    public String errorPrompt() {
        return "That is not possible.";
    }
}
