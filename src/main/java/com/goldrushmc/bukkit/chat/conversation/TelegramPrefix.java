package com.goldrushmc.bukkit.chat.conversation;

import com.goldrushmc.bukkit.chat.Telegram;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

/**
 * User: Diremonsoon
 * Date: 6/26/13
 */
public class TelegramPrefix implements ConversationPrefix {

    private Telegram t;

    public TelegramPrefix(Telegram t) {
        this.t = t;
    }

    @Override
    public String getPrefix(ConversationContext context) {
        return "[- " + " -]";
    }
}
