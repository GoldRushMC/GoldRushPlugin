package com.goldrushmc.bukkit.bank;

import com.goldrushmc.bukkit.bank.accounts.Account;
import com.goldrushmc.bukkit.bank.accounts.AccountType;
import com.goldrushmc.bukkit.bank.conversation.BankPrefix;
import com.goldrushmc.bukkit.bank.conversation.prompts.WelcomePrompt;
import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.defaults.NotInTownException;
import com.goldrushmc.bukkit.town.Town;
import com.goldrushmc.bukkit.trainstation.exceptions.MarkerNumberException;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private volatile Map<Player, Conversation> conversations = new HashMap<>();
    private ConversationFactory teller;
    private List<NPC> employees = new ArrayList<>();
    private volatile Map<Player, NPC> helpedBy = new HashMap<>();
    private Map<Block, NPC> tellerAreas = new HashMap<>();
    private String name;
    private Town town;
    private List<Block> bankArea;
    private int tellerDiameter;
    private int checkingInterest, loanInterest, creditInterest;

    public Bank(World world, List<Location> coords, JavaPlugin plugin, String name) throws MarkerNumberException, NotInTownException {
        super(world, coords, plugin);
        this.name = name;
        Location loc1 = coords.get(0), loc2 = coords.get(1);

        Bukkit.getLogger().info("Getting Bank Area...");
        if(loc1.getBlockY() > loc2.getBlockY()) bankArea = getSelectiveArea(loc1, loc2, (loc2.getBlockY() - 10), (loc2.getBlockY() + 50));
        else bankArea = getSelectiveArea(loc1, loc2, (loc1.getBlockY() - 10), (loc1.getBlockY() + 50));

//        Town town = null;
//        for(Block b : bankArea) {
//            if(Town.getTownAtBlock(b) != null) {
//                town = Town.getTownAtBlock(b);
//                break;
//            }
//        }
//        //You can only make a Bank if it is COMPLETELY within a town.
//        if(town == null) throw new NotInTownException("bank");
//        else if(!town.getTownArea().containsAll(bankArea)) throw new NotInTownException("bank");
//        else this.town = town;

        //Find the tellers and initialize the conversation factory.
        Bukkit.getLogger().info("Finding Tellers...");
        employees = findTellers();

        Bukkit.getLogger().info("Tellers Counted: " + employees.size());

        teller = new ConversationFactory(plugin);

        Bukkit.getLogger().info("Getting Config Parameters...");
        //This has the potential to be 0, in which case, the default is 3 blocks.
        tellerDiameter = plugin.getConfig().getInt("bank.teller.diameter");
        if(tellerDiameter == 0) tellerDiameter = 3;

        Bukkit.getLogger().info("Creating Conversation Areas...");
        //Create the areas where conversations can take place.
        initializeTellers();

        checkingInterest = 3;
        loanInterest = 3;
        creditInterest = 3;

        //Register Events and add to Static Master-List Map
        add();
    }

    public Conversation startTransaction(Player p, NPC n) {
        //Set parameters of the conversation and begin it.
        teller.withFirstPrompt(new WelcomePrompt(this, n, p));
        teller.withEscapeSequence("//");
        teller.withLocalEcho(true);
        teller.withPrefix(new BankPrefix(name));
        Conversation talk = teller.buildConversation(p);

        talk.begin();
        return talk;
    }

    public int getCheckingInterest() {
        return checkingInterest;
    }

    public int getLoanInterest() {
        return loanInterest;
    }

    public int getCreditInterest() {
        return creditInterest;
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
    public void openAccount(HumanEntity customer, AccountType type) {
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
        if(!accountHolders.containsKey(customer)) {
            accountHolders.put(customer, new ArrayList<Account>());
        }
        accountHolders.get(customer).add(account);
        if(masterList.containsKey(customer))  masterList.get(customer).add(account);
        else {
            List<Account> add = new ArrayList<>();
            add.add(account);
            masterList.put(customer, add);
        }
    }

    public void closeAccount(HumanEntity customer, Account account) {
        accountHolders.get(customer).remove(account);
        masterList.get(customer).remove(account);
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

    public Account getAccount(HumanEntity p, AccountType type) {
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

        //Abandon all conversations, and nullify map.
        for(Conversation c : conversations.values()) {
            c.abandon();
        }
        conversations = null;

        //Clear all employees, and nullify list.
        for(NPC e : employees) {
            e.destroy();
        }
        employees = null;

        //Nullify the bank area. This may not matter.
        bankArea = null;



        //Unregister all related events to this.
        PlayerMoveEvent.getHandlerList().unregister(this);
        //BlockFinder Class stuff.
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockDamageEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);

        //Remove bank from the master list of banks.
        banks.remove(this);
    }

    @Override
    public void add() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
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
            for(Block b : generateTellerBlocks(n)) {
                tellerAreas.put(b, n);
            }
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
        Location telLoc = teller.getBukkitEntity().getLocation();
        List<Block> tellerBlocks = new ArrayList<>();

        //Move in a backwards direction.
        for(int x = telLoc.getBlockX(); x > telLoc.getBlockX() - tellerDiameter; x--) {
            for(int y = telLoc.getBlockY(); y < telLoc.getBlockY() + tellerDiameter; y++) {
                for(int z = telLoc.getBlockZ(); z > telLoc.getBlockZ() - tellerDiameter; z--) {
                    tellerBlocks.add(world.getBlockAt(x, y, z));
                }
            }
        }

        //Move in a forward direction.
        for(int x = telLoc.getBlockX(); x < telLoc.getBlockX() + tellerDiameter; x++) {
            for(int y = telLoc.getBlockY(); y < telLoc.getBlockY() + tellerDiameter; y++) {
                for(int z = telLoc.getBlockZ(); z < telLoc.getBlockZ() + tellerDiameter; z++) {
                    //Check to make sure the block doesn't already exist, because it is possible for overlap to occur.
                    Block b = world.getBlockAt(x, y, z);
                    if(!tellerBlocks.contains(b)) tellerBlocks.add(b);
                }
            }
        }
        //TODO Testing mapping of convos.
        for(Block b : tellerBlocks) {
            b.setType(Material.GOLD_BLOCK);
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
        Block from = pme.getFrom().getBlock(), to = pme.getTo().getBlock();
        Player p = pme.getPlayer();

        if(helpedBy.containsKey(p)) {
            //Leaving Conversation
            if(!tellerAreas.containsKey(to) && tellerAreas.containsKey(from)) {
                if(tellerAreas.get(from).equals(helpedBy.get(p))) {
                    Conversation c = conversations.get(p);
                    if(c.getState().equals(Conversation.ConversationState.STARTED)) c.abandon();
                    conversations.remove(p);
                    helpedBy.remove(p);
                }
            }
        }
        //Entering Conversation
        if(tellerAreas.containsKey(to) && !tellerAreas.containsKey(from)) {
            if(helpedBy.get(p) == null) {
                NPC employee = tellerAreas.get(to);
                helpedBy.put(p, employee);
                conversations.put(p, startTransaction(p, employee));
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
