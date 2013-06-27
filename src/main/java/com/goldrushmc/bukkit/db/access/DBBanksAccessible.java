package com.goldrushmc.bukkit.db.access;

import com.avaje.ebean.Query;
import com.goldrushmc.bukkit.db.tables.BankTbl;

import java.util.List;

/**
 * User: Diremonsoon
 * Date: 6/24/13
 */
public interface DBBanksAccessible extends DBAccessible {

    public BankTbl getBank(String name);

    public Query<BankTbl> queryBanks();

    public List<BankTbl> getBankList();
}
