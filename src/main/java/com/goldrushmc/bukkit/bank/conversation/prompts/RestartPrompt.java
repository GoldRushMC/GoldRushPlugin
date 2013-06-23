package com.goldrushmc.bukkit.bank.conversation.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * User: Diremonsoon
 * Date: 6/23/13
 */
public class RestartPrompt implements Prompt {

    @Override
    public String getPromptText(ConversationContext context) {
        return "";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return false;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        return null;
    }
}
