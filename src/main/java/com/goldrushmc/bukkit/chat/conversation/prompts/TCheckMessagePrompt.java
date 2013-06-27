package com.goldrushmc.bukkit.chat.conversation.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * User: Diremonsoon
 * Date: 6/26/13
 */
public class TCheckMessagePrompt extends TDefaultPrompt {

    @Override
    public String getPromptText(ConversationContext context) {
        return null;
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
