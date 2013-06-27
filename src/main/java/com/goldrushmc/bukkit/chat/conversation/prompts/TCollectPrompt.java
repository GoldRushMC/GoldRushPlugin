package com.goldrushmc.bukkit.chat.conversation.prompts;

import com.goldrushmc.bukkit.defaults.conversation.TryAgainPrompt;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * User: Diremonsoon
 * Date: 6/26/13
 */
public class TCollectPrompt extends TDefaultPrompt{

    @Override
    public String getPromptText(ConversationContext context) {
        return "Please type 'collect' to retrieve your oldest unread letter.";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return true;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        switch (input.toLowerCase()) {
            case "collect":
                return new TCheckMessagePrompt();
            case "exit":
                return END_OF_CONVERSATION;
            default:
                return new TryAgainPrompt(this);
        }
    }
}
