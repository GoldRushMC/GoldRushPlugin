package com.goldrushmc.bukkit.chat.conversation.prompts;

import com.goldrushmc.bukkit.defaults.conversation.TryAgainPrompt;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * User: Diremonsoon
 * Date: 6/26/13
 */
public class TSendMessagePrompt extends TDefaultPrompt{

    @Override
    public String getPromptText(ConversationContext context) {

            return "Ready your book in your hand, and type 'send' to send your message. \n" +
                    "If your book message is validated, we will ask to confirm the send. \n" +
                    "If your book message is invalid, we will try to tell you why, and ask to try again.";
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return true;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {

        switch (input) {
            case "send":
            case "end":
                return END_OF_CONVERSATION;
            default:
                return new TryAgainPrompt(this);

        }
    }
}
