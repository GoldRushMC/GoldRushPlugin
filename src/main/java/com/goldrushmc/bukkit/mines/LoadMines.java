package com.goldrushmc.bukkit.mines;

import com.goldrushmc.bukkit.train.exceptions.MarkerNumberException;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoadMines {
<<<<<<< HEAD
    List<String> mineString = new ArrayList<String>();
    Plugin plugin;
    JavaPlugin jPlugin;

    public LoadMines(Plugin p, JavaPlugin pl) {
        plugin = p;
        jPlugin = pl;
        File file = new File(p.getDataFolder(), "mines.txt");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));

            String line = null;
            try {
                while ((line = in.readLine()) != null) {
                    mineString.add(line);
                }
            } catch (IOException e) {
                p.getLogger().info("GOLDRUSHMC: error reading mines file");
            }
        } catch (FileNotFoundException e) {
            p.getLogger().info("GOLDRUSHMC: mines file not found, creating now");

            File newFile = new File(p.getDataFolder(), "mines.txt");
            try {
                newFile.createNewFile();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                p.getLogger().info("GOLDRUSHMC: error creating mines file");
            }
        }
    }

    public List<Mine> parseMinesStrings() {
        List<Mine> mines = new ArrayList<Mine>();
        for (String mine : mineString) {
            String[] mineParse = mine.split(":");
            //get world name
            String world = mineParse[1];

            //get double list of both locations needed
            String[] loc1 = mineParse[2].split(",");
            String[] loc2 = mineParse[3].split(",");

            //list for storing both locations
            List<Location> locList = new ArrayList<Location>();

            //create first location at given world
            locList.add(new Location(plugin.getServer().getWorld(world),
                    Double.valueOf(loc1[0]),
                    Double.valueOf(loc1[1]),
                    Double.valueOf(loc1[2])));
            //create secon location at given world
            locList.add(new Location(plugin.getServer().getWorld(world),
                    Double.valueOf(loc2[0]),
                    Double.valueOf(loc2[1]),
                    Double.valueOf(loc2[2])));

            String[] vec = mineParse[4].split(",");
            Vector pos = new Vector(Integer.valueOf(vec[0]), Integer.valueOf(vec[1]), Integer.valueOf(vec[2]));
            Boolean gened = Boolean.valueOf(mineParse[6]);
            try {
                mines.add(new Mine(mineParse[0], plugin.getServer().getWorld(world), locList, jPlugin, pos, Integer.valueOf(mineParse[5]), gened));
            } catch (NumberFormatException e) {
                plugin.getLogger().info("GOLDRUSHMC: error loading mine - NumberFormatException");
            } catch (MarkerNumberException e) {
                plugin.getLogger().info("GOLDRUSHMC: error loading mine - MarkedNumberException");
            }
        }
        return mines;
    }
=======
	List<String> mineString = new ArrayList<>();
	Plugin plugin;
	JavaPlugin jPlugin;
	public LoadMines(Plugin p, JavaPlugin pl){
		plugin = p;
		jPlugin = pl;
		File file = new File(p.getDataFolder(), "mines.txt");
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(file));
			
			String line;
			try {
				while((line = in.readLine())!=null){
				  mineString.add(line);
				}
			} catch (IOException e) {
				p.getLogger().info("error reading mines file");
			}
		} catch (FileNotFoundException e) {
			p.getLogger().info("mines file not found, creating now");
			
			File newFile = new File(p.getDataFolder(), "mines.txt");
			try {
				newFile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				p.getLogger().info("error creating mines file");
			}
		}		
	}
	
	public List<Mine> parseMinesStrings() {
		List<Mine> mines = new ArrayList<>();
		for(String mine : mineString) {
			String[] mineParse = mine.split(":");
			//get world name
			String world = mineParse[1];
			
			//get double list of both locations needed
			String[] loc1 = mineParse[2].split(",");
			String[] loc2 = mineParse[3].split(",");
			
			//list for storing both locations
			List<Location> locList= new ArrayList<>();
			
			//create first location at given world
			locList.add(new Location(plugin.getServer().getWorld(world), 
					Double.valueOf(loc1[0]), 
					Double.valueOf(loc1[1]), 
					Double.valueOf(loc1[2])));
			//create secon location at given world
			locList.add(new Location(plugin.getServer().getWorld(world), 
					Double.valueOf(loc2[0]), 
					Double.valueOf(loc2[1]), 
					Double.valueOf(loc2[2]))); 
			
			String[] vec = mineParse[4].split(",");
			Vector pos = new Vector(Integer.valueOf(vec[0]), Integer.valueOf(vec[1]), Integer.valueOf(vec[2]));
			Boolean gened = Boolean.valueOf(mineParse[6]);
			try {
				mines.add(new Mine(mineParse[0], plugin.getServer().getWorld(world), locList, jPlugin, pos, Integer.valueOf(mineParse[5]), gened));
			} catch (NumberFormatException e) {
				plugin.getLogger().info("GOLDRUSHMC: error loading mine - NumberFormatException");
			} catch (MarkerNumberException e) {
				plugin.getLogger().info("GOLDRUSHMC: error loading mine - MarkedNumberException");
			}
		}
		return mines;		
	}
>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
}
