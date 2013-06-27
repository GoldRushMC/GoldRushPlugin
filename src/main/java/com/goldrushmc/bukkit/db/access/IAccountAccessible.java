package com.goldrushmc.bukkit.db.access;

import com.avaje.ebean.Query;
import com.goldrushmc.bukkit.db.tables.PlayerTbl;

import java.util.List;

/**
 * User: Diremonsoon
 * Date: 6/27/13
 */
public interface IAccountAccessible extends DBAccessible{

    public PlayerTbl getPlayer(String name);

    public Query<PlayerTbl> queryPlayers();

    public List<PlayerTbl> getPlayerList();
}
