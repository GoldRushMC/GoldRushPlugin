package com.goldrushmc.bukkit.bank;

import com.goldrushmc.bukkit.bank.accounts.Account;
import com.goldrushmc.bukkit.bank.conversation.prompts.BankPrefix;
import com.goldrushmc.bukkit.bank.conversation.prompts.WelcomePrompt;
import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.town.Town;
import com.goldrushmc.bukkit.trainstation.exceptions.MarkerNumberException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Will be used to facilitate an economy.
 *
 * @author Lucas
 */
public abstract class Bank extends BlockFinder {

    //This is to be used CROSS-BANK to ensure that people can have many accounts with many banks.
    private static Map<HumanEntity, List<Account>> masterList = new HashMap<>();

    private Map<HumanEntity, List<Account>> accountHolders = new HashMap<>();
    private Map<Player, Conversation> conversations = new HashMap<>();
    private ConversationFactory teller;
    private String name;
    private Town town;

    public Bank(World world, List<Location> coords, JavaPlugin plugin) throws MarkerNumberException {
        super(world, coords, plugin);
        teller = new ConversationFactory(plugin);
    }

    public void startTransaction(Player p) {
        //Set parameters of the conversation and begin it.
        teller.withFirstPrompt(new WelcomePrompt());
        teller.withEscapeSequence("//");
        teller.withLocalEcho(false);
        teller.withPrefix(new BankPrefix(name));
        Conversation talk = teller.buildConversation(p);
        conversations.put(p, talk);
        talk.begin();


    }

    public Map<HumanEntity, List<Account>> getAccountHolders() {
        return accountHolders;

    }

    /**
     * Checks to see if there are any accounts tied to this player within the bank.
     *
     * @param p The player in question
     * @return {@code true} if they have at least one account.
     */
    public boolean hasAccount(HumanEntity p) {
        return accountHolders.containsKey(p);
    }

    public void setAccountHolders(Map<HumanEntity, List<Account>> accountHolders) {
        this.accountHolders = accountHolders;
    }

    //TODO This is the method for opening accounts.
    public void openAccount(HumanEntity customer, Account.AccountType type) {
        boolean newAccount = false;
        if(accountHolders.containsKey(customer)) newAccount = true;

        switch (type) {
            case CHECKING:
                break;
            case LOAN:
                break;
            case CREDIT:
                break;
        }
    }

    public void openAccount(HumanEntity customer, Account account) {
        accountHolders.put(customer, new ArrayList<Account>());
        accountHolders.get(customer).add(account);
    }

    public void closeAccount(HumanEntity customer, Account account) {
        accountHolders.get(customer).remove(account);
        account.deleteAccount();
    }

    public void transferAccount(HumanEntity customer, Account account, Bank newBank) {
        accountHolders.get(customer).remove(account);
        newBank.openAccount(customer, account);
    }



    public ConversationFactory getTeller() {
        return teller;
    }

    public void setTeller(ConversationFactory teller) {
        this.teller = teller;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Account getAccount(HumanEntity p, Account.AccountType type) {
        if(accountHolders.containsKey(p)) {
            for(Account a : accountHolders.get(p)) {
                if(a.getAccountType().equals(type)) {
                    return a;
                }
            }
        }
        return null;
    }

    public List<Account> getAccounts(HumanEntity p) {
        return accountHolders.get(p);
    }

    public static Map<HumanEntity, List<Account>> getMasterList() {
            return masterList;
    }
}
