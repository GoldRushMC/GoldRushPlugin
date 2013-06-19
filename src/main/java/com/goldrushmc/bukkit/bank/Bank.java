package com.goldrushmc.bukkit.bank;

import com.goldrushmc.bukkit.bank.accounts.Account;
import com.goldrushmc.bukkit.bank.conversation.BankPrefix;
import com.goldrushmc.bukkit.bank.conversation.prompts.WelcomePrompt;
import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.town.Town;
import com.goldrushmc.bukkit.trainstation.exceptions.MarkerNumberException;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Will be used to facilitate an economy.
 *
 * @author Lucas
 */
public class Bank extends BlockFinder {

    //This is to be used CROSS-BANK to ensure that people can have many accounts with many banks.
    private static Map<HumanEntity, List<Account>> masterList = new HashMap<>();
    private static List<Bank> banks = new ArrayList<>();

    private Map<HumanEntity, List<Account>> accountHolders = new HashMap<>();
    private Map<Player, Conversation> conversations = new HashMap<>();
    private ConversationFactory teller;
    private List<NPC> employees = new ArrayList<>();
    private volatile Map<Player, NPC> helpedBy = new HashMap<>();
    private Map<NPC, List<Block>> tellerAreas = new HashMap<>();
    private String name;
    private Town town;
    private final List<Block> bankArea;
    private int tellerDiameter;

    public Bank(World world, List<Location> coords, JavaPlugin plugin) throws MarkerNumberException {
        super(world, coords, plugin);
        Location loc1 = coords.get(0), loc2 = coords.get(1);

        if(loc1.getBlockY() > loc2.getBlockY()) bankArea = getSelectiveArea(loc1, loc2, (loc2.getBlockY() - 10), (loc2.getBlockY() + 50));
        else bankArea = getSelectiveArea(loc1, loc2, (loc1.getBlockY() - 10), (loc1.getBlockY() + 50));

        teller = new ConversationFactory(plugin);

        //This has the potential to be 0, in which case, the default is 3 blocks.
        tellerDiameter = plugin.getConfig().getInt("bank.teller.diameter");
        if(tellerDiameter == 0) tellerDiameter = 3;
    }

    public void startTransaction(Player p, NPC n) {
        //Set parameters of the conversation and begin it.
        teller.withFirstPrompt(new WelcomePrompt());
        teller.withEscapeSequence("//");
        teller.withLocalEcho(false);
        teller.withPrefix(new BankPrefix(name));
        Conversation talk = teller.buildConversation(p);

        teller.addConversationAbandonedListener(new ConversationAbandonedListener() {
            @Override
            public void conversationAbandoned(ConversationAbandonedEvent e) {
                //Sends the player a message wishing them well (hopefully this works...)
                e.getContext().getForWhom().sendRawMessage("Have a nice day!");
            }
        });

        conversations.put(p, talk);
        helpedBy.put(p, n);
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

    public static List<Bank> getBanks() {
        return banks;
    }


    @Override
    public void remove() {
        banks.remove(this);
    }

    @Override
    public void add() {
        banks.add(this);
    }

    @Override
    public List<Block> findNonAirBlocks() {
        List<Block> blocks = new ArrayList<>();
        for(Block b : bankArea) {
            if(!b.getType().equals(Material.AIR)) blocks.add(b);
        }
        return blocks;
    }

    public List<NPC> findTellers() {
        List<NPC> tellers = new ArrayList<>();
        for(Chunk c : chunks) {
            Entity[] entities = c.getEntities();
            for(Entity e : entities) {
                //We don't care about entities that are not within the bank area, and that aren't human.
                if(!bankArea.contains(e.getLocation().getBlock()) || !(e instanceof HumanEntity)) continue;

                if(CitizensAPI.getNPCRegistry().isNPC(e)) {
                    NPC n = CitizensAPI.getNPCRegistry().getNPC(e);
                    if(n != null) tellers.add(n);
                }
            }
        }

        return tellers;
    }

    public void initializeTellers() {
        for(NPC n : employees) {
            tellerAreas.put(n, generateTellerBlocks(n));
        }
    }

    /**
     * Gets a perimeter around the NPC teller which will be used to figure transaction interactions.
     *
     * @param teller the employee to generate the blocks for.
     * @return The list of blocks which are around the employee, in a cube form.
     */
    public List<Block> generateTellerBlocks(NPC teller) {
        //This location is the CENTER of the teller blocks. we expand outwards the specified amount (set in config.yml)
        Location telLoc = teller.getStoredLocation();
        List<Block> tellerBlocks = new ArrayList<>();

        //Move in a forward direction.
        for(int x = telLoc.getBlockX(); x < telLoc.getBlockX() + tellerDiameter; x++) {
            for(int y = telLoc.getBlockY(); x < telLoc.getBlockY() + tellerDiameter; y++) {
                for(int z = telLoc.getBlockZ(); z < telLoc.getBlockZ() + tellerDiameter; z++) {
                    tellerBlocks.add(world.getBlockAt(x, y, z));
                }
            }
        }

        //Move in a backwards direction.
        for(int x = telLoc.getBlockX(); x < telLoc.getBlockX() - tellerDiameter; x--) {
            for(int y = telLoc.getBlockY(); x < telLoc.getBlockY() - tellerDiameter; y--) {
                for(int z = telLoc.getBlockZ(); z < telLoc.getBlockZ() - tellerDiameter; z--) {
                    //Check to make sure the block doesn't already exist, because it is possible for overlap to occur.
                    Block b = world.getBlockAt(x, y, z);
                    if(!tellerBlocks.contains(b)) tellerBlocks.add(b);

                }
            }
        }
        return tellerBlocks;
    }

    /**
     * In charge of monitoring where the player is during conversations.
     * <p>
     * Determines whether or not a conversation should start, continue, or stop.
     *
     * @param pme The event of Player Movement.
     */
    @EventHandler
    public void dialogMonitor(PlayerMoveEvent pme) {
        Location from = pme.getFrom(), to = pme.getTo();
        Player p = pme.getPlayer();

        //This should only be true if the conversation is already initiated.
        if(helpedBy.containsKey(p)) {
            List<Block> telBlocks = tellerAreas.get(helpedBy.get(p));
            if(!telBlocks.contains(to) && telBlocks.contains(from)) {
                Conversation c = conversations.get(p);
                c.abandon();
                conversations.remove(p);
                helpedBy.remove(p);
            }
        } else {
            /* Logic for finding the teller, based on the lists.
               It is a backwards way of finding the NPC,
               but it is only for starting new conversations, not monitoring them.
             */
            Collection<List<Block>> telBlocks  = tellerAreas.values();
            for(List<Block> list : telBlocks) {
                if(list.contains(to)) {
                    for(NPC n : tellerAreas.keySet()) {
                        if(tellerAreas.get(n).equals(list)) {
                            startTransaction(p, n);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }


    @Override
    @EventHandler
    public void onPlayerDamageAttempt(BlockDamageEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlock();

        if(p.hasPermission("goldrushmc.bank.edit")) return;

        if(bankArea.contains(b)) event.setCancelled(true);
    }

    @Override
    @EventHandler
    public void onPlayerBreakAttempt(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlock();

        if(p.hasPermission("goldrushmc.bank.edit")) return;

        if(bankArea.contains(b)) event.setCancelled(true);
    }

    @Override
    @EventHandler
    public void onPlayerPlaceAttempt(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlockAgainst();

        if(p.hasPermission("goldrushmc.bank.edit")) return;

        if(bankArea.contains(b)) event.setCancelled(true);
    }
}
