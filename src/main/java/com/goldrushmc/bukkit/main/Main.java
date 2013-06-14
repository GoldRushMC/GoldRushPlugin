package com.goldrushmc.bukkit.main;

import com.goldrushmc.bukkit.bank.InventoryLis;
import com.goldrushmc.bukkit.commands.*;
import com.goldrushmc.bukkit.db.*;
import com.goldrushmc.bukkit.guns.GunLis;
import com.goldrushmc.bukkit.mines.*;
import com.goldrushmc.bukkit.panning.PanningLis;
import com.goldrushmc.bukkit.panning.PanningTool;
import com.goldrushmc.bukkit.train.listeners.TrainLis;
import com.goldrushmc.bukkit.train.listeners.TrainStationLis;
import com.goldrushmc.bukkit.train.listeners.WandLis;
import com.goldrushmc.bukkit.train.scheduling.TimeCounter;
import com.goldrushmc.bukkit.train.station.TrainStation;
import com.goldrushmc.bukkit.train.station.npc.CartTradeable;
import com.goldrushmc.bukkit.tunnelcollapse.TunnelCollapseCommand;
import com.goldrushmc.bukkit.tunnelcollapse.TunnelsListener;
import com.goldrushmc.bukkit.weapons.GunTool;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
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

    @Override
    public void onEnable() {
		setupDB();

        //Add commands
        getCommand("StationWand").setExecutor(new StationWand(this));
        getCommand("Station").setExecutor(new CreateTrainStation(this));
        getCommand("Fall").setExecutor(new TunnelCollapseCommand(this));
        getCommand("Gun").setExecutor(new GunTool(this));
        getCommand("PanningTool").setExecutor(new PanningTool(this));
        getCommand("Mine").setExecutor(new MineCommands(this));
        getCommand("ShowVisitors").setExecutor(new ShowVisitorsCommand(this));
        getCommand("TrainCycle").setExecutor(new TrainCycleCommand(this));
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


        //Add settings for Tunnel Collapse
        FileConfiguration fc = this.getConfig();
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

        //Populate the train station listener maps
        //This only works if the database has data to make train stations with....
//		tsl.populate();

        //run load task later once world has loaded
        Bukkit.getServer().broadcastMessage("Loading Mines.. Prepare for Lag..");
        //Bukkit.getServer().getScheduler().runTaskLater(this, new LoadMinesObject(this), 100);

        //Just get a list. this way, every world that is "normal" has the capability of scheduling.
        List<World> worlds = this.getServer().getWorlds();
        for(World world : worlds) {
            //Start the time counter, so that we can measure the time remaining for each train.
            //We need to get the worlds from the config file. ONLY CARES ABOUT NORMAL TYPE WORLDS
            if(world.getWorldType().equals(WorldType.NORMAL)) Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new TimeCounter(this, world), 0, 1);
        }



        //run load task later once world has loaded
        Bukkit.getServer().broadcastMessage("Loading Mines.. Prepare for Lag..");
        //Bukkit.getServer().getScheduler().runTaskLater(this, new LoadMinesTask(this), 100);

        getLogger().info(getDescription().getName() + " " + getDescription().getVersion() + " Enabled!");
    }

    private void setupDB() {
        try {
            getDatabase().find(TrainTbl.class).findRowCount();
            getDatabase().find(TrainScheduleTbl.class).findRowCount();
            getDatabase().find(TrainStatusTbl.class).findRowCount();
            getDatabase().find(TrainStationTbl.class).findRowCount();
            getDatabase().find(TrainStationLocationTbl.class).findRowCount();
            getDatabase().find(PlayerTbl.class).findRowCount();
            getDatabase().find(TownTbl.class).findRowCount();
            getDatabase().find(BankTbl.class).findRowCount();
            getDatabase().find(JobTbl.class).findRowCount();
            getDatabase().find(CartListTbl.class).findRowCount();
            getDatabase().find(ItemForeignKeyTbl.class).findRowCount();
            getDatabase().find(ItemTbl.class).findRowCount();
            getDatabase().find(MinesTbl.class).findRowCount();
        } catch (PersistenceException | NullPointerException e) {
            getLogger().info("Installing database for " + getDescription().getName() + " due to first time use.");
            installDDL();
        }
    }

	@Override
	public List<Class<?>> getDatabaseClasses() {
		List<Class<?>> list = new ArrayList<Class<?>>();

		list.add(TrainTbl.class);
		list.add(TrainScheduleTbl.class);
		list.add(TrainStatusTbl.class);
		list.add(TrainStationTbl.class);
		list.add(TrainStationLocationTbl.class);
		list.add(PlayerTbl.class);
		list.add(TownTbl.class);
		list.add(BankTbl.class);
		list.add(JobTbl.class);
		list.add(CartListTbl.class);
		list.add(ItemForeignKeyTbl.class);
		list.add(ItemTbl.class);
		return list;
	}

    @Override
    public void onDisable() {
        //Clear all of the trains out of all the mappings, to free up memory.
        TrainStation.getTrainStations().clear();

        SaveMines saveMines = new SaveMines(this);
        int count = 0;
        Boolean saved = false;
        while(saved == false) {
            //Bukkit.getScheduler().runTask(this, new SaveMinesObject(this));
            count++;
            if(count==5) {
                this.getLogger().info("Could not save mines after 5 retry's! Exiting..");
                break;
            }
        }

        getLogger().info("GoldRush Plugin Disabled!");
    }
}
