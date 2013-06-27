package com.goldrushmc.bukkit.chat.conversation.prompts;

import com.goldrushmc.bukkit.chat.Telegram;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * User: Diremonsoon
 * Date: 6/27/13
 */
public class TConfirmSendPrompt extends TDefaultPrompt {

    @Override
    public String getPromptText(ConversationContext context) {

        if(contents.hasDisplayName()) {
            return "Send the book message " + contents.getDisplayName() + "? " +  "Type 'confirm' to do so, or 'end' to leave this conversation.";
        } else {
            return "Send your message? Type 'confirm' to do so, or 'end' to leave this conversation.";
        }
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return true;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        if(input.equalsIgnoreCase("confirm")) {
            Telegram t = new Telegram();
            t.sendTelegram(sender.getName(), recipient.getName(), contents.getPage(0));
        }

        return null;
    }
}
