package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.Bank;
import com.goldrushmc.bukkit.defaults.conversation.SessionConstants;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * User: Diremonsoon
 * Date: 6/17/13
 * Time: 11:13 PM
 */
public class WelcomePrompt extends DefaultPrompt {

    public boolean startOver = false;

    public WelcomePrompt(Bank bank, NPC teller, Player customer) {
        super(bank, teller, customer);
    }

    @Override
    public String getPromptText(ConversationContext context) {

        String dialog = "";
        //For the first welcome prompt in the conversation.
        if(context.getSessionData(SessionConstants.CONTINUE) == null) context.setSessionData(SessionConstants.CONTINUE, false);
        //If this prompt is called because of a continue prompt, we want to not say this again.
        if(context.getSessionData(SessionConstants.CONTINUE).equals(false)) {
            dialog = "Welcome to bank " + bank.getName() + ". How may I assist you? \n";
        } else {
            //This is for if there is a continuation. We need to clear out ALL session data, so that we can "start fresh".
            for(SessionConstants sc : SessionConstants.values()) {
                context.setSessionData(sc, null);
            }
            //We want to keep this data, because it is supposed to be set to false at ALL TIMES unless there is a continuation at hand.
            context.setSessionData(SessionConstants.CONTINUE, false);
        }

        //Are they a customer at the bank? If so, say this, and add the session data.
        if(bank.hasAccount(customer)) {

            //Add in numbers for options:
            dialog += "Welcome back " + customer.getName() + " to bank " + bank.getName() + "\n" +
                    "Would you like to: \n" +
                    "[1]: Check Balance \n" +
                    "[2]: Withdraw \n" +
                    "[3]: Deposit \n" +
                    "[4]: Transfer \n" +
                    "[5]: Transfer Account To New Bank (type 'transfer account')\n" +
                    "[6]: Close Account \n" +
                    "[7]: Open Account \n" +
                    "[8]: End Conversation \n" +
                    ChatColor.AQUA + "TO CANCEL THE CONVERSATION AT ANY TIME, TYPE //, END OR STOP. \n" +
                    ChatColor.RESET + "Please choose an option. To pick one, merely type in the option as it is spelt, unless specified otherwise.";

        } else {
            dialog += "[1]: Open an Account \n" +
                      "[2]: End Conversation (type end or stop)";
        }

        return dialog;
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return context.getSessionData(SessionConstants.WAIT) == null || context.getSessionData(SessionConstants.WAIT).equals(true);
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        switch (input) {
            case "check balance":
                context.setSessionData(SessionConstants.SELECTION, "check balance");
                context.setSessionData(SessionConstants.NEXT_PROMPT, new CheckBalancePrompt(bank, teller, customer));
                break;
            case "withdraw":
                context.setSessionData(SessionConstants.SELECTION, "withdraw");
                context.setSessionData(SessionConstants.NEXT_PROMPT, new WithdrawPrompt(bank, teller, customer));
                break;
            case "deposit":
                context.setSessionData(SessionConstants.SELECTION, "deposit");
                context.setSessionData(SessionConstants.NEXT_PROMPT, new DepositPrompt(bank, teller, customer));
                break;
            case "transfer":
                context.setSessionData(SessionConstants.SELECTION, "transfer");
                break;
            case "close account":
                context.setSessionData(SessionConstants.SELECTION, "close");
                break;
            case "transfer account":
                context.setSessionData(SessionConstants.SELECTION, "transfer account");
                break;
            case "open account":
                context.setSessionData(SessionConstants.SELECTION, "open account");
                context.setSessionData(SessionConstants.NEXT_PROMPT, new OpenAccountPrompt(bank, teller, customer));
                return new AccountTypeSelectorPrompt(bank, teller, customer);
            case "end":
            case "stop":
            case "//":
            default:
                return Prompt.END_OF_CONVERSATION;
        }

        return new AccountSelectorPrompt(bank, teller, customer);
    }

    @Override
    public String errorPrompt() {
        return "That is not a valid option. Please try again.";
    }
}
