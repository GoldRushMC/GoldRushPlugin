package com.goldrushmc.bukkit.mines;

import com.goldrushmc.bukkit.defaults.CommandDefault;
import com.goldrushmc.bukkit.train.exceptions.MarkerNumberException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineCommands extends CommandDefault {

    public static Map<Player, List<Integer>> mineSize = new HashMap<Player, List<Integer>>();
    public static Map<Player, List<blockBackup>> backList = new HashMap<Player, List<blockBackup>>();
    public static Map<Player, String> nameList = new HashMap<Player, String>();

    public MineCommands(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("mine")) { // command label
            if (p instanceof Player) { // instance of player - checks if player
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("create")) {
                        if (args.length == 2) {
                            try {
                                p.sendMessage("Created mine outline");
                                p.sendMessage("Type " + ChatColor.GREEN + "/mine confirm " +
                                        ChatColor.WHITE + "to create the mine, " +
                                        ChatColor.RED + "/mine cancel " + ChatColor.WHITE + "to start again.");
                                makeMarkers(p.getWorld(), p, Material.WOOL); //makes a wool outline of the area selected
                                nameList.put(p, args[1]);
                                return true;
                            } catch (Exception ex) {
                                p.sendMessage(ChatColor.RED + "Invalid Paramters!");
                                return false;
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "Missing mine name!");
                            return false;
                        }
                    } else if (args[0].equalsIgnoreCase("tool")) {

                        ItemStack panningTool = new ItemStack(Material.CLAY_BALL);
                        List<String> lore = new ArrayList<String>();
                        lore.add("Right click to denote start of mine");
                        ItemMeta meta = panningTool.getItemMeta();
                        meta.setLore(lore);
                        meta.setDisplayName("Mine Creator");
                        panningTool.setItemMeta(meta);
                        p.getInventory().addItem(panningTool);
                        return true;

                    } else if (args[0].equalsIgnoreCase("confirm")) {
                        Location loc1 = MineLis.mineMin.get(p).toLocation(p.getWorld());
                        p.sendMessage(String.valueOf(loc1.getBlockY()));
                        Location loc2 = MineLis.mineMax.get(p).toLocation(p.getWorld());
                        p.sendMessage(String.valueOf(loc2.getBlockY()));

                        List<Location> locList = new ArrayList<Location>();
                        locList.add(loc1);
                        locList.add(loc2);
                        if (nameList.containsKey(p)) {
                            try {
                                Mine newMine = new Mine(nameList.get(p), p.getWorld(),
                                        locList, plugin, MineLis.mineLoc.get(p).toVector(), 2, false);
                            } catch (MarkerNumberException e) {
                                plugin.getLogger().info("GOLDRUSHMC: MarkerNumberException creating mine");
                            }

                            SaveMines saveMines = new SaveMines(plugin);
                            int count = 0;
                            Boolean save = false;
                            while (!save) {
                                save = saveMines.save();
                                count++;
                                if (count == 5) {
                                    plugin.getLogger().info("GOLDRUSHMC: Could not save mines after 5 retrys!");
                                    save = true;
                                }
                            }
                        }

                        return true;
                    } else if (args[0].equalsIgnoreCase("load")) {
                        if (args.length == 2) {
                            int i = 0;
                            for (Mine mine : Mine.getMines()) {
                                p.sendMessage(mine.name);
                                if (mine.name == args[1]) {
                                    LoadMines loadMines = new LoadMines(plugin, plugin);
                                    for (Mine mine2 : loadMines.parseMinesStrings()) {
                                        p.sendMessage(mine2.name);
                                        if (mine.name == mine2.name) {
                                            Mine.getMines().set(i, mine2);
                                        }
                                    }
                                }
                                i++;
                            }

                            return true;
                        } else {
                            return false;
                        }
                    } else if (args[0].equalsIgnoreCase("cancel")) {
                        undoMarkers(p.getWorld(), p); //undoes the placement of wool outline
                        return true;
                    } else {
                        p.sendMessage("Not a valid command!");
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void makeMarkers(World w, Player p, Material m) {

        int maxx = MineLis.mineMax.get(p).getBlockX();
        int minx = MineLis.mineMin.get(p).getBlockX();
        int maxz = MineLis.mineMax.get(p).getBlockZ();
        int minz = MineLis.mineMin.get(p).getBlockZ();
        int maxy = MineLis.mineMax.get(p).getBlockY();
        int miny = MineLis.mineMin.get(p).getBlockY();

        //Backup array
        List<blockBackup> temp = new ArrayList<blockBackup>();

        //if(backList.containsKey(p)){
        //if(backList.get(p).size() > 0) {
        //undoMarkers(w, p);
        //}
        //}

        //loops to only fill the edges of the cube
        backList.remove(p);
        for (int x = minx; x <= maxx; x++) {
            for (int z = minz; z <= maxz; z++) {
                for (int y = miny; y <= maxy; y++) {
                    if (x == minx && y == miny) {
                        temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
                        w.getBlockAt(x, y, z).setType(m);
                    } else if (x == maxx && y == maxy) {
                        temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
                        w.getBlockAt(x, y, z).setType(m);
                    } else if (x == maxx && y == miny) {
                        temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
                        w.getBlockAt(x, y, z).setType(m);
                    } else if (x == minx && y == maxy) {
                        temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
                        w.getBlockAt(x, y, z).setType(m);
                    } else if (z == minz && y == miny) {
                        temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
                        w.getBlockAt(x, y, z).setType(m);
                    } else if (z == maxz && y == maxy) {
                        temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
                        w.getBlockAt(x, y, z).setType(m);
                    } else if (z == maxz && y == miny) {
                        temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
                        w.getBlockAt(x, y, z).setType(m);
                    } else if (z == minz && y == maxy) {
                        temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
                        w.getBlockAt(x, y, z).setType(m);
                    } else if (x == minx && z == minz) {
                        temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
                        w.getBlockAt(x, y, z).setType(m);
                    } else if (x == maxx && z == maxz) {
                        temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
                        w.getBlockAt(x, y, z).setType(m);
                    } else if (x == maxx && z == minz) {
                        temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
                        w.getBlockAt(x, y, z).setType(m);
                    } else if (x == minx && z == maxz) {
                        temp.add(new blockBackup(new Location(w, x, y, z), w.getBlockAt(x, y, z).getType()));
                        w.getBlockAt(x, y, z).setType(m);
                    }
                }
            }
        }

        backList.put(p, temp);
    }

    private void undoMarkers(World w, Player p) {
        for (int i = 0; i < backList.get(p).size(); i++) {
            w.getBlockAt(backList.get(p).get(i).location).setType(backList.get(p).get(i).material);
        }
    }

    //class for storing the block backup data
    class blockBackup {
        public Location location;
        public Material material;

        public blockBackup(Location loc, Material mat) {
            location = loc;
            material = mat;
        }
    }
}
