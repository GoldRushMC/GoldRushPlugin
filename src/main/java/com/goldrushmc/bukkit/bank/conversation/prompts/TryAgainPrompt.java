package com.goldrushmc.bukkit.bank.conversation.prompts;

import com.goldrushmc.bukkit.chat.conversation.TSession;
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

        if(context.getSessionData(SessionConstants.ERROR) == null) {
            if(context.getSessionData(TSession.ERROR) == null) {
                //Default message.
                return "That is not an option. Please try again.";
            }
            return ChatColor.RED + "" + context.getSessionData(TSession.ERROR);
        }
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
