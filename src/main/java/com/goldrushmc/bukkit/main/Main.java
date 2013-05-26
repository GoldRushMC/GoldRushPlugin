package com.goldrushmc.bukkit.main;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.goldrushmc.bukkit.bank.InventoryLis;
import com.goldrushmc.bukkit.commands.CreateTrainStation;
import com.goldrushmc.bukkit.commands.RemoveTrainStation;
import com.goldrushmc.bukkit.commands.ShowVisitorsCommand;
import com.goldrushmc.bukkit.commands.StationWand;
import com.goldrushmc.bukkit.commands.TrainCycleCommand;
import com.goldrushmc.bukkit.commands.TrainStationListCommand;
import com.goldrushmc.bukkit.db.BankTbl;
import com.goldrushmc.bukkit.db.CartListTbl;
import com.goldrushmc.bukkit.db.ItemForeignKeyTbl;
import com.goldrushmc.bukkit.db.ItemTbl;
import com.goldrushmc.bukkit.db.JobTbl;
import com.goldrushmc.bukkit.db.PlayerTbl;
import com.goldrushmc.bukkit.db.TownTbl;
import com.goldrushmc.bukkit.db.TrainScheduleTbl;
import com.goldrushmc.bukkit.db.TrainStationLocationTbl;
import com.goldrushmc.bukkit.db.TrainStationTbl;
import com.goldrushmc.bukkit.db.TrainStatusTbl;
import com.goldrushmc.bukkit.db.TrainTbl;
import com.goldrushmc.bukkit.guns.GunLis;
import com.goldrushmc.bukkit.guns.GunTool;
import com.goldrushmc.bukkit.mines.LoadMines;
import com.goldrushmc.bukkit.mines.Mine;
import com.goldrushmc.bukkit.mines.MineLis;
import com.goldrushmc.bukkit.mines.MineCommands;
import com.goldrushmc.bukkit.mines.SaveMines;
import com.goldrushmc.bukkit.panning.PanningLis;
import com.goldrushmc.bukkit.panning.PanningTool;
import com.goldrushmc.bukkit.train.listeners.TrainLis;
import com.goldrushmc.bukkit.train.listeners.TrainStationLis;
import com.goldrushmc.bukkit.train.listeners.WandLis;
import com.goldrushmc.bukkit.train.station.TrainStation;
import com.goldrushmc.bukkit.train.station.npc.CartTradeable;
import com.goldrushmc.bukkit.tunnelcollapse.SettingsManager;
import com.goldrushmc.bukkit.tunnelcollapse.TunnelCollapseCommand;
import com.goldrushmc.bukkit.tunnelcollapse.TunnelsListener;



public final class Main extends JavaPlugin{
	
	public final TrainLis tl = new TrainLis(this);
	public final WandLis wl = new WandLis(this);
	public final TrainStationLis tsl = new TrainStationLis(this);
	public final TunnelsListener tunnel = new TunnelsListener(this);
	public final GunLis gl = new GunLis(this);
	public final PanningLis pl = new PanningLis(this);
	public final InventoryLis il = new InventoryLis(this);
	public final MineLis ml = new MineLis(this);
	
	public static List<Mine> mineList;

	@Override
	public void onEnable() {
//		setupDB();
		
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
		SettingsManager settings = SettingsManager.getInstance();
		settings.setup(this);

		//Register traits for the NPCs.
		if(getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
			getLogger().severe("Citizens 2.0 not found or not enabled");
			getServer().getPluginManager().disablePlugin(this);	
			return;
		}	
 
		//Register your trait with Citizens.        
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(CartTradeable.class).withName("CartTrader"));
		
		//Populate the train station listener maps
		//This only works if the database has data to make train stations with....
//		tsl.populate();
		
		//load mines
		try {
		LoadMines loadMines = new LoadMines(this, this);
		mineList = loadMines.parseMinesStrings();
		} catch (Exception e) {
			mineList = new ArrayList<Mine>();
		}
		
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
		} catch (PersistenceException | NullPointerException e) {
			getLogger().info("Installing database for " + getDescription().getName() + " due to first time use.");
			installDDL();
		}
	}
	
//	@Override
//	public List<Class<?>> getDatabaseClasses() {
//		List<Class<?>> list = new ArrayList<Class<?>>();
//		list.add(TrainTbl.class);
//		list.add(TrainScheduleTbl.class);
//		list.add(TrainStatusTbl.class);
//		list.add(TrainStationTbl.class);
//		list.add(TrainStationLocationTbl.class);
//		list.add(PlayerTbl.class);
//		list.add(TownTbl.class);
//		list.add(BankTbl.class);
//		list.add(JobTbl.class);
//		list.add(CartListTbl.class);
//		list.add(ItemForeignKeyTbl.class);
//		list.add(ItemTbl.class);
//		return list;
//	}
	
	@Override
	public void onDisable() {
		//Clear all of the trains out of all the mappings, to free up memory.
		TrainStation.getTrainStations().clear();
		SaveMines saveMines = new SaveMines(this, mineList);
		int count = 0;
		while(!saveMines.save()) {
			count++;
			if(count==5) { 
				this.getLogger().info("GOLDRUSHMC: Could not save mines after 5 retrys! Exiting..");
				break; 
			}
		}
		this.getLogger().info("GoldRush Plugin Disabled!");
	}

}
