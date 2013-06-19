package com.goldrushmc.bukkit.bank.conversation.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * Created with IntelliJ IDEA.
 * User: Lucas
 * Date: 6/18/13
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContinuePrompt extends DefaultPrompt {
    @Override
    public String getPromptText(ConversationContext context) {
        return "Would you like to do something else?";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return true;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        if(input.equalsIgnoreCase("Yes")) return new WelcomePrompt();
        else return Prompt.END_OF_CONVERSATION;
    }
}
