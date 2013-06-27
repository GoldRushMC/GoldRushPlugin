package com.goldrushmc.bukkit.chat.conversation.prompts;

import com.goldrushmc.bukkit.bank.conversation.prompts.SessionConstants;
import com.goldrushmc.bukkit.bank.conversation.prompts.TryAgainPrompt;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * User: Diremonsoon
 * Date: 6/26/13
 */
public class TWelcomePrompt extends TDefaultPrompt{

    @Override
    public String getPromptText(ConversationContext context) {

        return "Welcome to the Telegram Office. How may we assist you today? \n" +
                "[1] Check Messages \n" +
                "[2] Send Message \n" +
                "[3] Exit \n" +
                ChatColor.AQUA + "Type in the option of your choosing please...";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return true;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        switch (input.toLowerCase()) {
            case "check messages":
                return new TCheckMessagePrompt();
            case "send message":
                return new TSendMessagePrompt();
            case "exit":
                return END_OF_CONVERSATION;
            default: {
                context.setSessionData(SessionConstants.ERROR, "Sorry, that is not an option. Please try again.");
                return new TryAgainPrompt(this);
            }
        }
    }
}
