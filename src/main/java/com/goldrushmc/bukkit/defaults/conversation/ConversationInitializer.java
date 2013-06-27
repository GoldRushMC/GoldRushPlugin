package com.goldrushmc.bukkit.defaults.conversation;

import java.util.List;

public class ConversationInitializer {

	protected List<Object> toInitialize;
	
	public void storeObjects(List<Object> os) {
		toInitialize = os;
	}
	
	public void addObject(Object o) {
		toInitialize.add(o);
	}
}
