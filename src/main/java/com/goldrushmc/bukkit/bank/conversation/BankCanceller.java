package com.goldrushmc.bukkit.bank.conversation;

import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;

/**
 * User: Diremonsoon
 * Date: 6/19/13
 */
public class BankCanceller implements ConversationCanceller {

    private Conversation conv;

    @Override
    public void setConversation(Conversation conversation) {
        this.conv = conversation;
    }

    @Override
    public boolean cancelBasedOnInput(ConversationContext context, String input) {
        return input.equalsIgnoreCase("End") || input.equalsIgnoreCase("Stop");
    }

    @Override
    public ConversationCanceller clone() {
        BankCanceller bc = new BankCanceller();
        bc.setConversation(conv);
        return bc;
    }
}
