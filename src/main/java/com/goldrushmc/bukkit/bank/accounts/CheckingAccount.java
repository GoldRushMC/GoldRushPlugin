package com.goldrushmc.bukkit.bank.accounts;

import com.goldrushmc.bukkit.bank.Bank;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * User: Diremonsoon
 * Date: 6/18/13
 * Time: 9:25 PM
 * <p>
 * This account is to be used as a standard deposit/withdraw account.
 *
 */
public class CheckingAccount extends AbstractAccount {

    public CheckingAccount(String name ,int startingBalance, HumanEntity owner, Bank bank, int interest) {
        super(startingBalance, owner, bank, interest);
        this.type = AccountType.CHECKING;
        this.name = name;
    }

    @Override
    public boolean withdraw(int amount) {
        Inventory inv = owner.getInventory();
        ItemStack[] golds;

        //If the amount is less than a stack, we only need one.
        if(amount < 64) golds = new ItemStack[]{new ItemStack(Material.GOLD_NUGGET, amount)};
            //If it has a remainder, then we need to add one onto the amount of stacks needed.
        else if(amount % 64 != 1) golds = new ItemStack[(amount / 64) + 1];
            //Else, we just do the stack amount divided by 64.
        else golds = new ItemStack[amount / 64];

        //Get the slots available within the inventory
        int available = inv.getSize() - inv.getContents().length;

        //If there is enough room, add the gold to the account.
        if(golds.length <= available) inv.addItem(golds);
            //If there is not enough room, don't add it to their account.
        else return false;

        balance -= amount;
        return true;
    }

    @Override
    public boolean deposit(int amount) {

        Inventory inv = owner.getInventory();
        ItemStack[] items = inv.getContents();

        int countOfGold = 0;

        for(ItemStack item : items) {
            if(item.getType().equals(Material.GOLD_NUGGET)) {
                countOfGold += item.getAmount();
            }
        }

        //This signifies that the holder has enough gold nuggets to deposit the requested amount.
        if(countOfGold >= amount) {
            //Add the necessary balance amount to the account.
            balance += amount;

            //Subtract the necessary amount of gold nuggets from the holder's inventory.
            int toRemove = amount;
            for(ItemStack item : items) {
                if(toRemove == 0) break;
                if(item.getType().equals(Material.GOLD_NUGGET)) {
                    if(item.getAmount() > toRemove) {
                        item.setAmount(item.getAmount() - toRemove);
                        toRemove = 0;
                    } else if(item.getAmount() == toRemove){
                        inv.remove(item);
                        toRemove = 0;
                    } else if(item.getAmount() < toRemove) {
                        toRemove -= item.getAmount();
                        inv.remove(item);
                    }
                }
            }
            return  true;
        }
        return false;
    }

    @Override
    public int checkBalance() {
        return balance;
    }

    @Override
    public boolean hasEnoughGold(int cost) {
        return balance >= cost;
    }
}
