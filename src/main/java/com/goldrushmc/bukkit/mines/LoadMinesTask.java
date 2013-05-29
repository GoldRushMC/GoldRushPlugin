package com.goldrushmc.bukkit.mines;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 257bbab534d278b0a66ed7c032c85252b06c98f2
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LoadMinesTask implements Runnable{
  	JavaPlugin p;
		public LoadMinesTask(JavaPlugin plug) {
			p = plug;
		}

		@Override
		public void run() {
			LoadMines loadMines = new LoadMines(p, p);
			Bukkit.getServer().getScheduler().runTaskLater(p, new GenTask(loadMines), 20);
		}

		class GenTask implements Runnable{
			LoadMines loadMines;
			GenTask(LoadMines lm) {
				loadMines = lm;
			}

			@Override
			public void run() {
<<<<<<< HEAD
				List<Mine> mines = loadMines.parseMinesStrings();
				Bukkit.getServer().broadcastMessage("Successfully loaded " + mines.size() + " mines!");
=======
				Mine.setMines(loadMines.parseMinesStrings());
				Bukkit.getServer().broadcastMessage("Successfully loaded " + Mine.getMines().size() + " mines!");
>>>>>>> 257bbab534d278b0a66ed7c032c85252b06c98f2
			}
		}
  }
