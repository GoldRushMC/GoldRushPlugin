package com.goldrushmc.bukkit.bank.conversation.prompts;

/**
 * User: Diremonsoon
 * Date: 6/24/13
 */
public enum SessionConstants {

    /**
     * For determining if the conversation should continue. boolean in nature.
     */
    CONTINUE,
    /**
     * Whether or not the prompt should wait. boolean in nature.
     */
    WAIT,
    /**
     * Contains the account for the session.
     */
    ACCOUNT,
    /**
     * Contains the account type for the session.
     */
    ACCOUNT_TYPE,
    /**
     * Contains the error message from the most recent prompt.
     */
    ERROR,
    /**
     * Used as a boolean, to see whether or not the account should be assigned a name, or named by the user.
     */
    AUTO_NAME,
    /**
     * The choice made by the user, as in what branch they want to pick.
     */
    SELECTION,
    /**
     * The next prompt object to be used.
     */
    NEXT_PROMPT,
    /**
     * The amount (integer) in gold nuggets to withdraw/deposit.
     */
    AMOUNT,
    /**
     * Marking outcome of the event. (So, success or failure)
     */
    OUTCOME
}
