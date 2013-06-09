package com.goldrushmc.bukkit.panning;

import com.goldrushmc.bukkit.defaults.CommandDefault;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PanningTool extends CommandDefault {

    public PanningTool(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        ItemStack panningTool = new ItemStack(Material.BOWL);
        List<String> lore = new ArrayList<String>();
        lore.add("10 used left");
        ItemMeta meta = panningTool.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName("Panning Tool");
        panningTool.setItemMeta(meta);
        p.getInventory().addItem(panningTool);
        return true;
    }

}
