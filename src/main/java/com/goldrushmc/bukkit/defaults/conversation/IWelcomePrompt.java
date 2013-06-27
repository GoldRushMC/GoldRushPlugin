package com.goldrushmc.bukkit.defaults.conversation;

import java.util.List;

import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public interface IWelcomePrompt extends Prompt {

	public String getWelcomeText();
	
	public List<String> getOptions();
	
	public boolean isReturningVisitor(Player p);
	
	public Prompt getSelection(String input);
}
