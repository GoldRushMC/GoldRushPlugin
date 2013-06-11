package com.goldrushmc.bukkit.mines;

import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lex
 * Date: 06/06/13
 * Time: 14:20
 * To change this template use File | Settings | File Templates.
 */
public class LoadMinesObject implements Runnable {
    Plugin p;

    public LoadMinesObject(Plugin p) {
        this.p = p;
    }

    @Override
    public void run() {
        try {
            //use buffering
            InputStream file = new FileInputStream(p.getDataFolder() + "mines.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            try {
                //deserialize the List
                List<Mine> recoveredMines = (List<Mine>) input.readObject();

                Mine.setMines(recoveredMines);
            } finally {
                input.close();
                p.getLogger().info("Successfully loaded all " + Mine.getMines().size() + " mines!");
            }
        } catch (ClassNotFoundException ex) {
            p.getLogger().info("Failed to save mines - ClassNotFoundException");
        } catch (IOException ex) {
            p.getLogger().info("Failed to load mines - IOException");
        }
    }
}
