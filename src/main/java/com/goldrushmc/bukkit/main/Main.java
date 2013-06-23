package com.goldrushmc.bukkit.main;

import com.goldrushmc.bukkit.bank.InventoryLis;
import com.goldrushmc.bukkit.chat.ChatLis;
import com.goldrushmc.bukkit.commands.*;
import com.goldrushmc.bukkit.db.tables.MineLocationTbl;
import com.goldrushmc.bukkit.db.tables.MinesTbl;
import com.goldrushmc.bukkit.db.tables.StationLocationTbl;
import com.goldrushmc.bukkit.db.tables.StationTbl;
import com.goldrushmc.bukkit.guns.GunTool;
import com.goldrushmc.bukkit.guns.weapons.GunLis;
import com.goldrushmc.bukkit.mines.MineLis;
import com.goldrushmc.bukkit.panning.PanningLis;
import com.goldrushmc.bukkit.panning.PanningTool;
import com.goldrushmc.bukkit.trainstation.TrainStation;
import com.goldrushmc.bukkit.trainstation.listeners.TrainLis;
import com.goldrushmc.bukkit.trainstation.listeners.TrainStationLis;
import com.goldrushmc.bukkit.trainstation.listeners.WandLis;
import com.goldrushmc.bukkit.trainstation.npc.CartTradeable;
import com.goldrushmc.bukkit.trainstation.scheduling.TimeCounter;
import com.goldrushmc.bukkit.tunnelcollapse.TunnelCollapseCommand;
import com.goldrushmc.bukkit.tunnelcollapse.TunnelsListener;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.persistence.PersistenceException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public final class Main extends JavaPlugin {

    public final TrainLis tl = new TrainLis(this);
    public final WandLis wl = new WandLis(this);
    public final TrainStationLis tsl = new TrainStationLis(this);
    public final TunnelsListener tunnel = new TunnelsListener(this);
    public final GunLis gl = new GunLis(this);
    public final PanningLis pl = new PanningLis(this);
    public final InventoryLis il = new InventoryLis(this);
    public final MineLis ml = new MineLis(this);
    public final ChatLis cl = new ChatLis(this);

    public static List<Player> playerList = new ArrayList<>();

    @Override
    public void onEnable() {
		setupDB();

        /* TODO Centralize ALL commands into a general function. We need to make all commands begin with something,
         * TODO as to differentiate our commands from other plugins, which could potentially have the same command names.
         */

        //Add commands
        getCommand("StationWand").setExecutor(new StationWand(this));
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




        //Register listeners
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(tl, this);
        pm.registerEvents(wl, this);
        pm.registerEvents(tsl, this);
        pm.registerEvents(gl, this);
        pm.registerEvents(pl, this);
        pm.registerEvents(il, this);
        pm.registerEvents(ml, this);
        pm.registerEvents(cl, this);


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

        //Enabling time increase
        Bukkit.getServer().getScheduler().runTaskLater(this, new TimeManager(this), 2);

        //run load task later once world has loaded
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new LoaderClass(this), 40);

        getLogger().info(getDescription().getName() + " " + getDescription().getVersion() + " Enabled!");
    }

    ///*
    private void setupDB() {
        try {
            getDatabase().find(StationTbl.class).findRowCount();
            getDatabase().find(StationLocationTbl.class).findRowCount();
            getDatabase().find(MineLocationTbl.class).findRowCount();
            getDatabase().find(MinesTbl.class).findRowCount();
//            getDatabase().find(PlayerTbl.class).findRowCount();
//            getDatabase().find(TownTbl.class).findRowCount();
//            getDatabase().find(BankTbl.class).findRowCount();
//            getDatabase().find(JobTbl.class).findRowCount();
//            getDatabase().find(ItemForeignKeyTbl.class).findRowCount();
//            getDatabase().find(ItemTbl.class).findRowCount();
        } catch (PersistenceException | NullPointerException e) {
            getLogger().info("Installing database for " + getDescription().getName() + " due to first time use.");
            installDDL();
        }
    }
    //*/

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(StationTbl.class);
		list.add(StationLocationTbl.class);
        list.add(MinesTbl.class);
        list.add(MineLocationTbl.class);
//		list.add(PlayerTbl.class);
//		list.add(TownTbl.class);
//		list.add(BankTbl.class);
//		list.add(JobTbl.class);
//		list.add(ItemForeignKeyTbl.class);
//		list.add(ItemTbl.class);
		return list;
	}

    @Override
    public void onDisable() {
        //Clear all of the trains out of all the mappings, to free up memory.
        TrainStation.getTrainStations().clear();

//        SaveMines saveMines = new SaveMines(this);
//        int count = 0;
//        Boolean saved = false;
//        while(saved == false) {
//            //Bukkit.getScheduler().runTask(this, new SaveMinesObject(this));
//            count++;
//            if(count==5) {
//                this.getLogger().info("Could not save mines after 5 retry's! Exiting..");
//                break;
//            }
//        }

        getLogger().info("GoldRush Plugin Disabled!");
    }
}
