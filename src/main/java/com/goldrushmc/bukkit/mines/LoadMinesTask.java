class LoadMinesTask implements Runnable{
  	JavaPlugin p;
		LoadMinesTask(JavaPlugin plug) {
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
				mineList = loadMines.parseMinesStrings();
				Bukkit.getServer().broadcastMessage("Successfully loaded " + mineList.size() + " mines!");
			}
		}
  }
