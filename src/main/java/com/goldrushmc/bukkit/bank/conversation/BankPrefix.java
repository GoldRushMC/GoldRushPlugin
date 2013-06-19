package com.goldrushmc.bukkit.bank.conversation;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

/**
 * User: Diremonsoon
 * Date: 6/18/13
 */
public class BankPrefix implements ConversationPrefix {

    private String bankName;

    public BankPrefix(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String getPrefix(ConversationContext context) {
        return "[- " + bankName + " -]";
    }
}
