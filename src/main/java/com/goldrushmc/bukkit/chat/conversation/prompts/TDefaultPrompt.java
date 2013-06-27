package com.goldrushmc.bukkit.chat.conversation.prompts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 * User: Diremonsoon
 * Date: 6/26/13
 */
public abstract class TDefaultPrompt implements Prompt {

    protected String message;
    protected Player sender, recipient;
    protected ItemStack book;
    protected BookMeta contents;

    public TDefaultPrompt() {}

    public TDefaultPrompt(ItemStack book) {
        if(book.getType().equals(Material.WRITTEN_BOOK)) {
            this.book = book;
            BookMeta bm = (BookMeta) book.getItemMeta();
            this.contents = bm;
            this.sender = Bukkit.getPlayerExact(bm.getAuthor());
            this.recipient = Bukkit.getPlayerExact(bm.getTitle());
        }
    }
}
