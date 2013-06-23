package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.bank.Bank;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * User: Diremonsoon
 * Date: 6/18/13
 * Time: 2:07 PM
 */
public class ContinuePrompt extends DefaultPrompt {

    public ContinuePrompt(Bank bank, Player customer, NPC teller) {
        this.bank = bank;
        this.customer = customer;
        this.teller = teller;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "Would you like to do something else today?";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return true;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        if(input.equalsIgnoreCase("Yes")) {
            context.setSessionData(CONTINUE, true);
            context.setSessionData(WAIT, true);
            return new WelcomePrompt(bank, teller, customer);
        }
        else if(input.equalsIgnoreCase("No")) return Prompt.END_OF_CONVERSATION;
        else {
            context.setSessionData("ERROR", errorPrompt());
            return new TryAgainPrompt(this);
        }
    }

    @Override
    public String errorPrompt() {
        return "That is not an option. Please try entering 'Yes' or 'No'.";
    }
}
