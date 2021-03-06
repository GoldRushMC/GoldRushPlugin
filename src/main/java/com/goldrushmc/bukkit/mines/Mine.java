package com.goldrushmc.bukkit.mines;

import com.goldrushmc.bukkit.db.access.DBMinesAccess;
import com.goldrushmc.bukkit.db.access.DBMinesAccessible;
import com.goldrushmc.bukkit.db.tables.MineLocationTbl;
import com.goldrushmc.bukkit.db.tables.MinesTbl;
import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.trainstation.exceptions.MarkerNumberException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Mine extends BlockFinder{
	
	/*The reason this is private is because we don't want anyone to be able to add/remove from it.
	 *The mine class itself can facilitate this.
	 */
	private static List<Mine> mines = new ArrayList<>();
    public volatile List<Player> players = new ArrayList<>();
    public static DBMinesAccessible db;
	
	public Vector mineMin, mineMax, mineEntrance;
	Location recCoordOne, recCoordTwo;
	public int length, width, height;
	int goldLeft, density;
	Boolean isGenerated = false;
    Boolean closed = false;
	String name;
	World w;
	
	public Mine(String name, World world, List<Location> coords, JavaPlugin plugin, 
			Vector entrance, Integer dense, Boolean isGen)
			throws MarkerNumberException {
		super(world, coords, plugin);

        if(db == null) db = new DBMinesAccess(plugin);

        if(coords.size() < 2) throw new MarkerNumberException();

		this.name = name;
		mineEntrance = entrance;
		recCoordOne = coords.get(0);
//		plugin.getServer().broadcastMessage(recCoordOne.toVector().toString());
		recCoordTwo = coords.get(1);
//		plugin.getServer().broadcastMessage(recCoordTwo.toVector().toString());
		
		mineMax = findMaxBlock();
		mineMin = findMinBlock();
		
		density = dense;
		w = world;
		length = mineMax.getBlockZ() - mineMin.getBlockZ();
		width = mineMax.getBlockX() - mineMin.getBlockX();
		height = mineMax.getBlockY() - mineMin.getBlockY();
		isGenerated = isGen;
		add(); //Call the add method, which will add the mine to the list.
	}

    private void saveToDB() {
        //Instantiate necessary objects for saving
        MinesTbl mineEnt = new MinesTbl();
        MineLocationTbl tbl1 = new MineLocationTbl(),
                tbl2 = new MineLocationTbl(),
                tbl3 = new MineLocationTbl();

        //Initiate the objects with the proper location/vector data (replaces constructor)
        tbl1.initLocation(recCoordOne);
        tbl2.initLocation(recCoordTwo);
        //Mark the entrance as such.
        tbl3.initVector(mineEntrance);
        tbl3.setEntrance(true);

        //Mine Data
        mineEnt.setName(name);
        mineEnt.setDensity(density);
        mineEnt.setGenerated(isGenerated);
        Set<MineLocationTbl> locs = new HashSet<>();
        locs.add(tbl1);
        locs.add(tbl2);
        locs.add(tbl3);
        mineEnt.setLocations(locs);

        //Location - Mine References
        tbl1.setMine(mineEnt);
        tbl2.setMine(mineEnt);
        tbl3.setMine(mineEnt);

        Bukkit.getLogger().info("Saving the mine and locations!!");
        db.getDB().save(mineEnt);
        db.getDB().save(locs);
    }

    public void removeFromDB() {
        MinesTbl mine = db.queryMines().where().ieq("name", name).findUnique();
        if(mine != null) {
            for(MineLocationTbl loc : mine.getLocations()) {
                db.getDB().delete(loc);
            }
            db.getDB().delete(mine);
        }
    }

	private Vector findMaxBlock() {
		//get first max - first block in the array
		Vector max = this.selectedArea.get(0).getLocation().toVector();
		
		for(Block b : this.selectedArea) {
			if((b.getX() <= recCoordOne.getBlockX() && b.getY() <= recCoordOne.getBlockY() && b.getZ() <= recCoordOne.getBlockZ())
			|| (b.getX() <= recCoordTwo.getBlockX() && b.getY() <= recCoordTwo.getBlockY() && b.getZ() <= recCoordTwo.getBlockZ())) {
                        	Vector check = new Vector(b.getX(), b.getY(), b.getZ());
                        	max = Vector.getMaximum(max, check);
                    }
		}
		return max;
	}
	
	private Vector findMinBlock() {
		//get first min - first block in the array
		Vector min = this.selectedArea.get(0).getLocation().toVector();
		
		for(Block b : this.selectedArea) {
			if((b.getX() <= recCoordOne.getBlockX() && b.getY() <= recCoordOne.getBlockY() && b.getZ() <= recCoordOne.getBlockZ())
			|| (b.getX() <= recCoordTwo.getBlockX() && b.getY() <= recCoordTwo.getBlockY() && b.getZ() <= recCoordTwo.getBlockZ())) {
                        	Vector check = new Vector(b.getX(), b.getY(), b.getZ());
                        	min = Vector.getMinimum(min, check);
                    }
                }		
		return min;
	}

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        //We look for the blocks to and from
        Block from = event.getFrom().getBlock(), to = event.getTo().getBlock();

        //Has left the types!
        if (this.getSelectedArea().contains(from) && !this.getSelectedArea().contains(to)) {
            ExitMineEvent exit = new ExitMineEvent(this, event.getPlayer());
            Bukkit.getServer().getPluginManager().callEvent(exit);
        }

        //Has entered the types!
        else if (this.getSelectedArea().contains(to) && !this.getSelectedArea().contains(from)) {
            EnterMineEvent enter = new EnterMineEvent(this, event.getPlayer());
            Bukkit.getServer().getPluginManager().callEvent(enter);
        }

    }

    @EventHandler
    public void onMineEnter(EnterMineEvent e){
        players.add(e.getPlayer());
        e.getPlayer().sendMessage("You have just entered " + this.name + ".");
    }

    @EventHandler
    public void onMineExit(ExitMineEvent e){
        players.remove(e.getPlayer());
        e.getPlayer().sendMessage("You have just left " + this.name + ".");
    }
	@Override
	public void remove() {
        removeFromDB(); //Remove from DB.
        PlayerMoveEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockDamageEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        mines.remove(this);
	}

	@Override
	public void add() { 
		if(!isGenerated) {
			reGenerate();
			isGenerated = true;
		}
		mines.add(this); //Add the mine to the list.
        if(db.getMine(name) == null) saveToDB(); //Save to the DB.
        Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	/**
	 * The static method which can retrieve the Static {@link Mine} list.
	 * 
	 * @return the {@code List<Mine>} of mines.
	 */
	public static List<Mine> getMines() {
		return mines;
	}

    public static void setMines(List<Mine> mineList) {
        mines = mineList;
    }

    public int getGoldLeft() {
        int gold = 0;
        for (Block b : this.selectedArea) {
            if (b.getType() == Material.GOLD_ORE) {
                gold++;
            }
        }
        return gold;
    }

    public void reGenerate() {
        MineGenerator mineGen = new MineGenerator(w, mineMin, mineMax, mineEntrance);
        mineGen.generate(density);
    }

    public void closeForGenerate() {
        for(Player player : players){
            player.sendMessage("Mine closed for re-generation!");
            player.teleport(this.mineEntrance.toLocation(this.world));
        }
        reGenerate();
    }

    @Override
    public List<Block> findNonAirBlocks() {
        // TODO Auto-generated method stub
        return null;
    }

    public static DBMinesAccessible getDB() {
        return db;
    }
	
	public String getName() { return name; }

    @Override
    @EventHandler
    public void onPlayerPlaceAttempt(BlockPlaceEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @EventHandler
    public void onPlayerDamageAttempt(BlockDamageEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    @EventHandler
    public void onPlayerBreakAttempt(BlockBreakEvent event) {
        if(event.getBlock().getType() == Material.GOLD_ORE){
            goldLeft = getGoldLeft();
            event.getPlayer().sendMessage(String.valueOf(goldLeft));
        }
        if(goldLeft <= 5){
            closeForGenerate();
        }
    }
}
