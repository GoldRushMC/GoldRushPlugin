package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.Bank;
import com.goldrushmc.bukkit.bank.accounts.Account;
import com.goldrushmc.bukkit.bank.accounts.AccountType;
import com.goldrushmc.bukkit.bank.accounts.CheckingAccount;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
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
        Bukkit.getLogger().info("The Account Type is :" + context.getSessionData("ACCOUNT_TYPE"));
        if(context.getSessionData("ACCOUNT_TYPE").equals(AccountType.CHECKING)) {
            context.setSessionData("AUTO_NAME", false);
            return "What would you like to name your account?";
        } else {
            context.setSessionData("AUTO_NAME", true);
            return "Creating your account...";
        }
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return context.getSessionData("AUTO_NAME").equals(false);
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        if(context.getSessionData("AUTO_NAME").equals(false)) {
            Account newAccount = new CheckingAccount(input, 0, customer, bank, bank.getCheckingInterest());
            bank.openAccount(customer, newAccount);
            customer.sendMessage("Congratulations! You opened a checking account called: " + newAccount.accountName() + "\n" + "With bank: " + bank.getName());
        }
        return new ContinuePrompt(bank, customer, teller);
    }

    @Override
    public String errorPrompt() {
        return "That is not possible.";
    }
}
