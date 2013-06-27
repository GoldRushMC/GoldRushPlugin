package com.goldrushmc.bukkit.data;

import com.goldrushmc.bukkit.mines.Mine;
import org.bukkit.plugin.Plugin;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Lex
 * Date: 29/05/13
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */

public class SaveData implements Runnable {
    Plugin p;

    public SaveData(Plugin plug) {
        this.p = plug;

    }

    @Override
    public void run() {
        try {
            //use buffering
            OutputStream file = new FileOutputStream(p.getDataFolder() + "GoldRushMC/data.ser");
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try {
                output.writeObject(Mine.getMines());
            } finally {
                output.close();
                p.getLogger().info("Successfully saved all data!");
            }
        } catch (IOException ex) {
            p.getLogger().info("Failed to save data, IO Exception");
        }
    }
}