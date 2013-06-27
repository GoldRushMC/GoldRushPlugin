package com.goldrushmc.bukkit.db.access;

import com.avaje.ebean.EbeanServer;

/**
 * User: Diremonsoon
 * Date: 6/17/13
 * Time: 2:40 PM
 */
public interface DBAccessible {

    /**
     * Gets the Database instance of the plugin.
     *
     * @return
     */
    EbeanServer getDB();

    public void save(Object o);

    public void delete(Object o);
}
