package com.goldrushmc.bukkit.chat.conversation.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * User: Diremonsoon
 * Date: 6/27/13
 */
public class TContinuePrompt implements Prompt {

    @Override
    public String getPromptText(ConversationContext context) {
        return "Continue? (Yes/No)";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return true;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        switch (input.toLowerCase()) {
            case "yes":
            case "no":
            default:
                return END_OF_CONVERSATION;
        }
    }
}
