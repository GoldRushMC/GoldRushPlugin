package com.goldrushmc.bukkit.chat.conversation;

/**
 * User: Diremonsoon
 * Date: 6/26/13
 */
public enum TSession {

    /**
     * The menu option selected. This contains a Prompt object.
     */
    SELECTION,

    /**
     * The message the user wants to send. This is a string.
     */
    MESSAGE,

    /**
     * A boolean saying whether or not the message is valid.
     */
    VALIDATED,

    /**
     * The message displayed with the outcome of the conversation.
     */
    CONFIRMATION,

    /**
     * The person sending the message. This is a Player object.
     */
    SENDER,

    /**
     * The person to send the message to. This is a Player object.
     */
    RECIPIENT,

    /**
     * A boolean saying whether or not the user has messages.
     */
    HAS_MESSAGES,

    /**
     * For the TryAgain prompt message.
     */
    ERROR

}
