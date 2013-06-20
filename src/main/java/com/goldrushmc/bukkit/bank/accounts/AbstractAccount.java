package com.goldrushmc.bukkit.bank.accounts;

import com.goldrushmc.bukkit.bank.Bank;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * User: Diremonsoon
 * Date: 6/18/13
 * Time: 8:11 PM
 */
public abstract class AbstractAccount implements Account {

    protected int balance;
    protected HumanEntity owner;
    protected AccountType type;
    protected Bank bank;
    protected int interest;
    protected String name;

    /**
     * The standard constructor for any account type.
     *
     * @param startingBalance the balance the holder wants to put into the account. There can be minimums, depending on the bank.
     * @param owner the account holder.
     * @param bank the bank which this account resides within.
     * @param interest the amount of interest which will accrue on this account.
     */
    public AbstractAccount(int startingBalance, HumanEntity owner, Bank bank, int interest) {
        this.balance = startingBalance;
        this.owner = owner;
        this.bank = bank;
        this.interest = interest;
    }

    @Override
    public AccountType getAccountType() {
        return type;
    }

    @Override
    public void setAccountType(AccountType type) {
        this.type = type;
    }

    @Override
    public int getInterest() {
        return interest;
    }

    @Override
    public HumanEntity getAccountHolder() {
        return owner;
    }

    @Override
    public void setAccountHolder(HumanEntity person) {
        this.owner = person;
    }

    @Override
    public void deleteAccount() {

        //Get the gold to drop/give
        ItemStack[] golds;
        if(balance % 64 != 1) {
            //If the balance mod 64 isn't one, we need an extra item stack to pickup the remainder.
            golds = new ItemStack[(balance / 64) + 1];
        } else {
            //Get the size of the array, by doing the balance divided 64 (should come up with how many stacks we need)
            golds = new ItemStack[balance / 64];
        }

        Inventory inv = owner.getInventory();
        int size = inv.getSize();
        ItemStack[] items = inv.getContents();
        //If the player's inventory is full, drop the gold on the ground!
        if(items.length == size) {
            for(ItemStack gold : golds) owner.getWorld().dropItem(owner.getLocation(), gold);
        }
        //If both the current items and gold to add arrays are still smaller/equal to the size of the inventory, add it all!
        else if((items.length + golds.length) <= size) {
            inv.addItem(golds);
        }
        else if((items.length + golds.length) > size) {
            //Determine the amount of gold we can shove into the account holder's pockets!
            int canPut = size - items.length;
            //Add the gold stacks that are possible to put into the account holder's inventory.
            int i = 0;
            do {
                inv.addItem(golds[i]);
                i++;
            } while((i + 1) < canPut);

            //Drop the rest of the gold, if there is any to drop.
            while(i < golds.length) owner.getWorld().dropItem(owner.getLocation(), golds[i]);
        }

        Bank.getMasterList().get(owner).remove(this);

        balance = 0;
        interest = 0;
        bank = null;
        owner = null;
        type = null;
    }

    @Override
    public Bank getBank() {
        return bank;
    }

    @Override
    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public void setAccountName(String name) {
        this.name = name;
    }

    @Override
    public String accountName() {
        return name;
    }
}
