package com.goldrushmc.bukkit.bank.accounts;


import com.goldrushmc.bukkit.bank.Bank;
import org.bukkit.entity.HumanEntity;

public interface Account {

    /**
     * Gets the name of the account
     *
     * @return The name of the account
     */
    public String accountName();

    /**
     * Sets the name of the account.
     *
     * @param name Name to be set.
     */
    public void setAccountName(String name);

    /**
     * Withdraws a specified amount of gold nuggets, if the amount exists.
     * <p/>
     * If the amount does not exist, do not draw any funds out. Instead, display the current balance.
     *
     * @param amount The amount specified.
     * @return true if the withdrawal works, false if it doesn't.
     */
    public boolean withdraw(int amount);

    /**
     * Deposits a specified amount of gold nuggets, if the amount exists.
     * <p/>
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
    public boolean hasEnoughGold(int cost);

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
     * Gets the holder of the account.
     * @return
     */
    public HumanEntity getAccountHolder();

    /**
     * Sets the account holder
     *
     * @param person the new account holder.
     */
    public void setAccountHolder(HumanEntity person);

    /**
     * Moves ALL existing funds into the holder's pockets, if possible.
     * In the event this is not possible, It will deposit the funds onto the ground within the bank.
     * TODO The holder can then gather it up at their leisure, because it should be owner-protected gold.
     */
    public void deleteAccount();

    /**
     * Gets the bank associated with this account.
     *
     * @return The bank in question.
     */
    public Bank getBank();

    /**
     * Sets the bank to be associated with this account.
     *
     * @param bank The bank to be set.
     */
    public void setBank(Bank bank);

}
