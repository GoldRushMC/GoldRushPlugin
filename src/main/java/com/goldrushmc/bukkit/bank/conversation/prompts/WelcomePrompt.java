package com.goldrushmc.bukkit.bank.conversation.prompts;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * User: Diremonsoon
 * Date: 6/17/13
 * Time: 11:13 PM
 */
public class WelcomePrompt extends DefaultPrompt {

    @Override
    public String getPromptText(ConversationContext context) {

        String dialog = "Welcome to bank " + bank.getName() + ". How may I assist you? \n";

        //Are they a customer at the bank? If so, say this, and add the session data.
        if(bank.hasAccount(customer)) {

            //Add in numbers for options:

            dialog += "Welcome back " + customer.getName() + " to bank " + bank.getName() + "\n" +
                    "Would you like to: \n" +
                    "*: Check Balance \n" +
                    "*: Withdraw \n" +
                    "*: Deposit \n" +
                    "*: Transfer \n" +
                    "*: Transfer Account To New Bank (type 'transfer account')\n" +
                    "*: Close Account \n" +
                    "*: End Conversation \n" +
                    ChatColor.AQUA + "TO CANCEL THE CONVERSATION AT ANY TIME, TYPE //, END OR STOP. \n" +
                    ChatColor.RESET + "Please choose an option. To pick one, merely type in the option as it is spelt, unless specified otherwise.";

        } else {
            dialog += "*: Open an Account \n" +
                      "*: End Conversation (type end or stop)";
        }

        return dialog;
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return context.getSessionData(WAIT).equals(true);
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        switch (input) {
            case "check balance":
                context.setSessionData("SELECTION", "check balance");
                context.setSessionData("NEXT_PROMPT", new CheckBalancePrompt());
                break;
            case "withdraw":
                context.setSessionData("SELECTION", "withdraw");
                break;
            case "deposit":
                context.setSessionData("SELECTION", "deposit");
                break;
            case "transfer":
                context.setSessionData("SELECTION", "transfer");
                break;
            case "end":
            case "stop":
            case "//":
            default:
                return Prompt.END_OF_CONVERSATION;
        }

        return new AccountTypeSelectorPrompt();
    }

}
