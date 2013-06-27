package com.goldrushmc.bukkit.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.bank.Bank;
import com.goldrushmc.bukkit.bank.InventoryLis;
import com.goldrushmc.bukkit.commands.BuildModeCommand;
import com.goldrushmc.bukkit.commands.BuildWand;
import com.goldrushmc.bukkit.commands.CreateBankCommand;
import com.goldrushmc.bukkit.commands.CreateTrainStation;
import com.goldrushmc.bukkit.commands.MineCommands;
import com.goldrushmc.bukkit.commands.RemoveTrainStation;
import com.goldrushmc.bukkit.commands.ShowVisitorsCommand;
import com.goldrushmc.bukkit.commands.TrainStationListCommand;
import com.goldrushmc.bukkit.db.access.DBBanksAccess;
import com.goldrushmc.bukkit.db.access.IAccountAccessible;
import com.goldrushmc.bukkit.db.tables.AccountTbl;
import com.goldrushmc.bukkit.db.tables.BankLocationTbl;
import com.goldrushmc.bukkit.db.tables.BankTbl;
import com.goldrushmc.bukkit.db.tables.MineLocationTbl;
import com.goldrushmc.bukkit.db.tables.MinesTbl;
import com.goldrushmc.bukkit.db.tables.PlayerTbl;
import com.goldrushmc.bukkit.db.tables.StationLocationTbl;
import com.goldrushmc.bukkit.db.tables.StationTbl;
import com.goldrushmc.bukkit.db.tables.TownTbl;
import com.goldrushmc.bukkit.guns.GunTool;
import com.goldrushmc.bukkit.guns.weapons.GunLis;
import com.goldrushmc.bukkit.mines.Mine;
import com.goldrushmc.bukkit.mines.MineLis;
import com.goldrushmc.bukkit.panning.PanningLis;
import com.goldrushmc.bukkit.panning.PanningTool;
import com.goldrushmc.bukkit.player.TradeLis;
import com.goldrushmc.bukkit.trainstation.TrainStation;
import com.goldrushmc.bukkit.trainstation.listeners.TrainLis;
import com.goldrushmc.bukkit.trainstation.listeners.TrainStationLis;
import com.goldrushmc.bukkit.trainstation.listeners.WandLis;
import com.goldrushmc.bukkit.trainstation.npc.CartTradeable;
import com.goldrushmc.bukkit.trainstation.scheduling.TimeCounter;
import com.goldrushmc.bukkit.tunnelcollapse.TunnelCollapseCommand;
import com.goldrushmc.bukkit.tunnelcollapse.TunnelsListener;


public final class Main extends JavaPlugin {

    //So the class may reference itself for the DB access requirement.
    public static Main instance;

    public final TrainLis tl = new TrainLis(this);
    public final WandLis wl = new WandLis(this);
    public final TrainStationLis tsl = new TrainStationLis(this);
    public final TunnelsListener tunnel = new TunnelsListener(this);
    public final GunLis gl = new GunLis(this);
    public final PanningLis pl = new PanningLis(this);
    public final InventoryLis il = new InventoryLis(this);
    public final MineLis ml = new MineLis(this);
    public final TradeLis trl = new TradeLis(this);

    public static IAccountAccessible db;
    public static List<Player> playerList = new ArrayList<>();    
    private static Map<String, Player> linkToPlayerInstance = new HashMap<>();

    @Override
    public void onEnable() {

		setupDB();

        db = new DBBanksAccess(this);

        /* TODO Centralize ALL commands into a general function. We need to make all commands begin with something,
         * TODO as to differentiate our commands from other plugins, which could potentially have the same command names.
         */

        //Add commands
        getCommand("BuildWand").setExecutor(new BuildWand(this));
        getCommand("BuildMode").setExecutor(new BuildModeCommand(this));
        getCommand("Station").setExecutor(new CreateTrainStation(this));
        getCommand("Fall").setExecutor(new TunnelCollapseCommand(this));
        getCommand("Gun").setExecutor(new GunTool(this));
        getCommand("PanningTool").setExecutor(new PanningTool(this));
        getCommand("Mine").setExecutor(new MineCommands(this));
        getCommand("ShowVisitors").setExecutor(new ShowVisitorsCommand(this));
//        getCommand("TrainCycle").setExecutor(new TrainCycleCommand(this));
        getCommand("RemoveStation").setExecutor(new RemoveTrainStation(this));
        getCommand("ListStations").setExecutor(new TrainStationListCommand(this));
        getCommand("Bank").setExecutor(new CreateBankCommand(this));


        //Register listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(tl, this);
        pm.registerEvents(wl, this);
        pm.registerEvents(tsl, this);
        pm.registerEvents(gl, this);
        pm.registerEvents(pl, this);
        pm.registerEvents(il, this);
        pm.registerEvents(ml, this);
        pm.registerEvents(trl, this);


        //Add settings for Tunnel Collapse
        FileConfiguration fc = getConfig();
        File f = new File("config.yml");
        if(!(f.exists())) {
            SettingsManager settings = SettingsManager.getInstance();
            settings.setup(this);
            fc = settings.getFileConfig();
        }

        //Register traits for the NPCs.
        if (getServer().getPluginManager().getPlugin("Citizens") == null || !getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            getLogger().severe("Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //Register your trait with Citizens.
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(CartTradeable.class).withName("CartTrader"));

        //Just get a list. this way, every world that is "normal" has the capability of scheduling.
        List<World> worlds = this.getServer().getWorlds();
        for(World world : worlds) {
            //Start the time counter, so that we can measure the time remaining for each trainstation.
            //We need to get the worlds from the config file. ONLY CARES ABOUT NON-NETHER AND NON-END WORLDS.
            if(world.getEnvironment().equals(World.Environment.NORMAL)) Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TimeCounter(this, world), 0, 1);
        }



        //run load task later once world has loaded
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new LoaderClass(this), 40);

        instance = this;

        initializePlayerTbl();

        getLogger().info(getDescription().getName() + " " + getDescription().getVersion() + " Enabled!");
    }

    private void setupDB() {
        try {
            getDatabase().find(StationTbl.class).findRowCount();
            getDatabase().find(StationLocationTbl.class).findRowCount();
            getDatabase().find(MineLocationTbl.class).findRowCount();
            getDatabase().find(MinesTbl.class).findRowCount();
            getDatabase().find(PlayerTbl.class).findRowCount();
            getDatabase().find(TownTbl.class).findRowCount();
            getDatabase().find(BankTbl.class).findRowCount();
            getDatabase().find(BankLocationTbl.class).findRowCount();
            getDatabase().find(AccountTbl.class).findRowCount();
//            getDatabase().find(JobTbl.class).findRowCount();
//            getDatabase().find(ItemForeignKeyTbl.class).findRowCount();
//            getDatabase().find(ItemTbl.class).findRowCount();
        } catch (PersistenceException | NullPointerException e) {
            getLogger().info("Installing database for " + getDescription().getName() + " due to first time use.");
            installDDL();
        }
    }

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(StationTbl.class);
		list.add(StationLocationTbl.class);
        list.add(MinesTbl.class);
        list.add(MineLocationTbl.class);
		list.add(PlayerTbl.class);
		list.add(TownTbl.class);
		list.add(BankTbl.class);
        list.add(BankLocationTbl.class);
        list.add(AccountTbl.class);
//		list.add(JobTbl.class);
//		list.add(ItemForeignKeyTbl.class);
//		list.add(ItemTbl.class);
		return list;
	}

    @Override
    public void onDisable() {
        //Clear all of the objects out of all the mappings, to free up memory (not that we need to...)
        TrainStation.getTrainStations().clear();
        Mine.getMines().clear();
        Bank.getBanks().clear();

        getLogger().info("GoldRush Plugin Disabled!");
    }

    /**
     * Resets all players to offline.
     */
    public void initializePlayerTbl() {
        List<PlayerTbl> players = db.getPlayerList();
        if(players == null) return;
        for(PlayerTbl p : players) {
            p.setOnline(false);
            db.save(p);
        }
    }
    
    public static void addPlayerInstance(Player p) {
        linkToPlayerInstance.put(p.getName(), p);
    }

    public static void removePlayerInstance(Player p) {
        linkToPlayerInstance.remove(p.getName());
    }
    
    public static Player getPlayerInstance(String name) {
    	return linkToPlayerInstance.get(name);
    }
    
    public static Collection<Player> getPlayerInstances() {
    	return linkToPlayerInstance.values();
    }
}
