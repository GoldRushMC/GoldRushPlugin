package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.accounts.Account;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * User: Diremonsoon
 * Date: 6/18/13
 * Time: 2:01 PM
 */
public class CheckBalancePrompt extends AccountPrompt {

    @Override
    public String getPromptText(ConversationContext context) {

        Account account = (Account) context.getSessionData("ACCOUNT");

        return ChatColor.GREEN + "Your balance is: " + ChatColor.AQUA + "( " + account.checkBalance() + " )" + ChatColor.GOLD + " Gold Nugget(s)";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return false;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        //Ask if the user wishes to continue.
        return new ContinuePrompt();
    }
}
