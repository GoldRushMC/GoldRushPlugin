package com.goldrushmc.bukkit.defaults.conversation;

import com.goldrushmc.bukkit.bank.Bank;
import com.goldrushmc.bukkit.bank.conversation.prompts.DefaultPrompt;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * User: Diremonsoon
 * Date: 6/24/13
 */
public class OutcomePrompt extends DefaultPrompt {

    public OutcomePrompt(Bank bank, NPC teller, Player customer) {
        super(bank, teller, customer);
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return (String) context.getSessionData(SessionConstants.OUTCOME);
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return false;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        return new ContinuePrompt(bank, customer, teller);
    }

    @Override
    public String errorPrompt() {
        return null;
    }
}
