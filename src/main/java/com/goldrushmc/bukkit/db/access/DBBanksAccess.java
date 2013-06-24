package com.goldrushmc.bukkit.db.access;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.goldrushmc.bukkit.db.tables.BankTbl;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * User: Diremonsoon
 * Date: 6/24/13
 */
public class DBBanksAccess implements DBBanksAccessible {

    private JavaPlugin plugin;

    public DBBanksAccess(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public BankTbl getBank(String name) {
        return queryBanks().where().ieq("name", name).findUnique();
    }

    @Override
    public Query<BankTbl> queryBanks() {
        return getDB().find(BankTbl.class);
    }

    @Override
    public List<BankTbl> getBankList() {
        return queryBanks().findList();
    }

    @Override
    public EbeanServer getDB() {
        return plugin.getDatabase();
    }
}
