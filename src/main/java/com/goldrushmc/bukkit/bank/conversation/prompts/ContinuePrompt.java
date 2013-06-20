package com.goldrushmc.bukkit.bank.conversation.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * User: Diremonsoon
 * Date: 6/18/13
 * Time: 2:07 PM
 */
public class ContinuePrompt extends DefaultPrompt {
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
        if(input.equalsIgnoreCase("Yes")) return new WelcomePrompt();
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
