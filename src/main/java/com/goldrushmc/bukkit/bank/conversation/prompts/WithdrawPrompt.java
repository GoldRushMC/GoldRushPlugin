package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.Bank;
import com.goldrushmc.bukkit.bank.accounts.Account;
import com.goldrushmc.bukkit.defaults.conversation.OutcomePrompt;
import com.goldrushmc.bukkit.defaults.conversation.SessionConstants;
import com.goldrushmc.bukkit.defaults.conversation.TryAgainPrompt;

import net.citizensnpcs.api.npc.NPC;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * User: Diremonsoon
 * Date: 6/20/13
 */
public class WithdrawPrompt extends AccountPrompt{

    public WithdrawPrompt(Bank bank, NPC teller, Player customer) {
        super(bank, teller, customer);
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "How much would you like to withdraw? (type cancel to end transaction)";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return true;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        int amount = 0;

        if(input.equalsIgnoreCase("cancel")) return Prompt.END_OF_CONVERSATION;

        if(NumberUtils.isNumber(input)) {
            amount = Integer.valueOf(input);
            if(amount <= 0) {
                context.setSessionData(SessionConstants.ERROR, errorPrompt());
                return new TryAgainPrompt(this);
            }
        } else {
            context.setSessionData(SessionConstants.ERROR, errorPrompt());
            return new TryAgainPrompt(this);
        }

        Account a = (Account) context.getSessionData(SessionConstants.ACCOUNT);
        if(a.withdraw(amount)) {
            context.setSessionData(SessionConstants.OUTCOME, ChatColor.GREEN + "You have withdrawn " + "( " + amount + " )" +
                    ChatColor.GOLD + " Gold Nugget(s)" + ChatColor.RESET + "from your account.");
            return new OutcomePrompt(bank, teller, customer);
        } else {
           context.setSessionData(SessionConstants.ERROR, "You do not have sufficient funds to complete this transaction.");
            return new TryAgainPrompt(this);
        }
    }

    @Override
    public String errorPrompt() {
        return "That is not a valid number. Please try again.";
    }
}
