package com.goldrushmc.bukkit.bank;


public interface Account {

	/**
	 * Withdraws a specified amount of gold nuggets, if the amount exists.
	 * <p>
	 * If the amount does not exist, do not draw any funds out. Instead, display the current balance.
	 * 
	 * @param amount The amount specified.
	 * @return true if the withdrawal works, false if it doesn't.
	 */
	public boolean withdraw(int amount);
	
	/**
	 * Deposits a specified amount of gold nuggets, if the amount exists.
	 * <p>
	 * If the amount does not exist, do not deposit any funds. Instead, tell the player what have to deposit.
	 * 
	 * @param amount The amount specified.
	 * @return true if the deposit works, false if it doesn't.
	 */
	public boolean deposit(int amount);
	
	/**
	 * Checks the balance of the account.
	 * 
	 * @return The amount, in gold nuggets, that the account holds.
	 */
	public int checkBalance();
	
	/**
	 * Checks if the account has enough gold to buy an item.
	 * 
	 * @return true if yes, false if no.
	 */
	public boolean hasEnoughGold();
	
	/**
	 * The account type of the account.
	 * 
	 * @return
	 */
	public AccountType getAccountType();
	
	/**
	 * Sets the account's type.
	 * 
	 * @param type
	 */
	public void setAccountType(AccountType type);
	
	/**
	 * Gets the amount of interest collected in this account, whether for the player or bank.
	 * 
	 * @return
	 */
	public int getInterest();
	
	/**
	 * The different types of accounts
	 * 
	 * @author Diremonsoon
	 *
	 */
	public enum AccountType {
		REGULAR,
		LOAN;
	}
}
