package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.Bank;
import com.goldrushmc.bukkit.bank.accounts.Account;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * User: Diremonsoon
 * Date: 6/20/13
 */
public class DepositPrompt extends AccountPrompt {

    public DepositPrompt(Bank bank, NPC teller, Player customer) {
        super(bank, teller, customer);
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "How much would you like to deposit? (type cancel to end transaction)";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return context.getSessionData("AMOUNT") == null;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        int amount = 0;

        if(input.equalsIgnoreCase("cancel")) return Prompt.END_OF_CONVERSATION;

        try {
            amount = Integer.valueOf(input);

            //You can't "subtract" using the deposit function!
            if(amount <= 0) throw new NumberFormatException();

        } catch (NumberFormatException e) {
            context.setSessionData("ERROR", errorPrompt());
            return new TryAgainPrompt(this);
        }

        Account a = (Account) context.getSessionData("ACCOUNT");
        if(a.withdraw(amount)) {
            customer.sendMessage(ChatColor.GREEN + "You have deposited " + "( " + amount + " )" +
                    ChatColor.GOLD + " Gold Nugget(s)" + ChatColor.RESET + "into your account.");
        } else {
            customer.sendMessage("You are not able to deposit funds.");
        }

        return new ContinuePrompt(bank, customer, teller);
    }

    @Override
    public String errorPrompt() {
        return "That is not a valid amount. Please try again.";
    }
}
