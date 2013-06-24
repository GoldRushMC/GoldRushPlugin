package com.goldrushmc.bukkit.bank.conversation.prompts;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * User: Diremonsoon
 * Date: 6/18/13
 */
public class TryAgainPrompt implements Prompt {

    private Prompt toTry;

    public TryAgainPrompt(Prompt toTry) {
        this.toTry = toTry;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return ChatColor.RED + "" + context.getSessionData(SessionConstants.ERROR);
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return false;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        return toTry;
    }
}
