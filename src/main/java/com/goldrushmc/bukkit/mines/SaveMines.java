package com.goldrushmc.bukkit.mines;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.bukkit.plugin.Plugin;

import com.goldrushmc.bukkit.main.Main;

public class SaveMines {
	
	File file;
	List<Mine> mineList;
	Plugin p;
	
	public SaveMines(Plugin p) {
		file = new File(p.getDataFolder(), "mines.txt");
		this.mineList = Mine.getMines();
		this.p = p;
	}
	
	public Boolean save() {
		try {
			if(!mineList.isEmpty()) {
			p.getLogger().info("GOLDRUSHMC: Attempting to save mines.");
			file.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			int count = 0;
			
			for(Mine mine : mineList) {
				String name = mine.name;
				String world = mine.w.getName();
				String gened = mine.isGenerated.toString();
				String loc1 = mine.selectedArea.get(0).getX()
						+ "," +mine.selectedArea.get(0).getY()
						+ "," + mine.selectedArea.get(0).getZ();
				String loc2 = mine.selectedArea.get(1).getX()
						+ "," +mine.selectedArea.get(1).getY()
						+ "," + mine.selectedArea.get(1).getZ();
				String vec = mine.mineEntrance.getBlockX()
						+ "," + mine.mineEntrance.getBlockY()
						+ "," + mine.mineEntrance.getBlockZ();
				out.write(name + ":"
						+ world + ":"
						+ loc1 + ":"
						+ loc2 + ":"
						+ vec + ":"
						+ mine.density + ":"
						+ gened);
				out.newLine();
				count++;
			}
			p.getLogger().info("GOLDRUSHMC: Successfully saved all " + count + " mines!");
			out.close();
			}
			return true;			
		} catch (IOException e) {
			p.getLogger().info("GOLDRUSHMC: error saving mines to file, trying again.");
			return false;
		} catch (Exception e) {
			p.getLogger().info("No mines to save");
			return true;
		}
	}
}
