package com.goldrushmc.bukkit.mines;

<<<<<<< HEAD
import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.train.exceptions.MarkerNumberException;
=======
import java.util.ArrayList;
import java.util.List;

>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

public class Mine extends BlockFinder {

    /*The reason this is private is because we don't want anyone to be able to add/remove from it.
     *The mine class itself can facilitate this.
     */
    private static List<Mine> mines = new ArrayList<Mine>();

    public Vector mineMin, mineMax, mineEntrance;
    Location recCoordOne, recCoordTwo;
    public int length, width, height;
    int goldLeft, genedGold, density;
    Boolean isGenerated = false;
    String name;
    World w;

    public Mine(String name, World world, List<Location> coords, JavaPlugin plugin,
                Vector entrance, Integer dense, Boolean isGen)
            throws MarkerNumberException {
        super(world, coords, plugin);

        this.name = name;
        mineEntrance = entrance;
        recCoordOne = coords.get(0);
        plugin.getServer().broadcastMessage(recCoordOne.toVector().toString());
        recCoordTwo = coords.get(1);
        plugin.getServer().broadcastMessage(recCoordTwo.toVector().toString());

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

    private Vector findMaxBlock() {
        //get first max - first block in the array
        Vector max = this.selectedArea.get(0).getLocation().toVector();

        for (Block b : this.selectedArea) {
            if ((b.getX() <= recCoordOne.getBlockX() && b.getY() <= recCoordOne.getBlockY() && b.getZ() <= recCoordOne.getBlockZ())
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

        for (Block b : this.selectedArea) {
            if ((b.getX() <= recCoordOne.getBlockX() && b.getY() <= recCoordOne.getBlockY() && b.getZ() <= recCoordOne.getBlockZ())
                    || (b.getX() <= recCoordTwo.getBlockX() && b.getY() <= recCoordTwo.getBlockY() && b.getZ() <= recCoordTwo.getBlockZ())) {
                Vector check = new Vector(b.getX(), b.getY(), b.getZ());
                min = Vector.getMinimum(min, check);
            }
        }
        return min;
    }

    @Override
    public void remove() {
        // TODO remove RefreshEvent
        mines.remove(this); //TODO Need to remove ALL variable memory references first. This is the first step.

    }

    @Override
    public void add() {
        if (!isGenerated) {
            reGenerate();
            isGenerated = true;
        }
        mines.add(this); //Add the mine to the list.
    }

    /**
     * The static method which can retrieve the Static {@link Mine} list.
     *
     * @return the {@code List<Mine>} of mines.
     */
    public static List<Mine> getMines() {
        return mines;
    }
=======
import com.goldrushmc.bukkit.defaults.BlockFinder;
import com.goldrushmc.bukkit.main.Main;
import com.goldrushmc.bukkit.train.exceptions.MarkerNumberException;

public class Mine extends BlockFinder{
	
	/*The reason this is private is because we don't want anyone to be able to add/remove from it.
	 *The mine class itself can facilitate this.
	 */
	private static List<Mine> mines = new ArrayList<Mine>();
	
	public Vector mineMin, mineMax, mineEntrance;
	Location recCoordOne, recCoordTwo;
	public int length, width, height;
	int goldLeft, genedGold, density;
	Boolean isGenerated = false;
	String name;
	World w;
	
	public Mine(String name, World world, List<Location> coords, JavaPlugin plugin, 
			Vector entrance, Integer dense, Boolean isGen)
			throws MarkerNumberException {
		super(world, coords, plugin);
		
		this.name = name;
		mineEntrance = entrance;
		recCoordOne = coords.get(0);
		plugin.getServer().broadcastMessage(recCoordOne.toVector().toString());
		recCoordTwo = coords.get(1);
		plugin.getServer().broadcastMessage(recCoordTwo.toVector().toString());
		
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

	@Override
	public void remove() {
		// TODO remove RefreshEvent
		mines.remove(this); //TODO Need to remove ALL variable memory references first. This is the first step.
		
	}

	@Override
	public void add() { 
		if(!isGenerated) {
			reGenerate();
			isGenerated = true;
		}
		mines.add(this); //Add the mine to the list.
	}
	
	/**
	 * The static method which can retrieve the Static {@link Mine} list.
	 * 
	 * @return the {@code List<Mine>} of mines.
	 */
	public static List<Mine> getMines() {
		return mines;
	}
>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98

    public static void setMines(List<Mine> mineList) {
        mines = mineList;
    }

<<<<<<< HEAD
    public void getGoldLeft() {
        goldLeft = 0;
        for (Block b : this.selectedArea) {
            if (b.getType() == Material.GOLD_ORE) {
                goldLeft++;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void reGenerate() {
        plugin.getServer().broadcastMessage(mineMin.toString());
        plugin.getServer().broadcastMessage(mineMax.toString());
        MineGenerator mineGen = new MineGenerator(w, mineMin, mineMax, mineEntrance);
        mineGen.generate(density);
    }

    @Override
    public List<Block> findNonAirBlocks() {
        // TODO Auto-generated method stub
        return null;
    }
=======
	public void getGoldLeft() {
		goldLeft = 0;
		for(Block b : this.selectedArea) {
			if(b.getType() == Material.GOLD_ORE) {
				goldLeft++;
			}
		}
	}
	
	public String getName() { return name; }
	
	public void reGenerate() {
		plugin.getServer().broadcastMessage(mineMin.toString());
		plugin.getServer().broadcastMessage(mineMax.toString());
		MineGenerator mineGen = new MineGenerator(w, mineMin, mineMax, mineEntrance);
		mineGen.generate(density);
	}

	@Override
	public List<Block> findNonAirBlocks() {
		// TODO Auto-generated method stub
		return null;
	}
>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
}
