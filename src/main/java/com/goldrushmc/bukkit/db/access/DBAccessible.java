package com.goldrushmc.bukkit.db.access;

import com.avaje.ebean.EbeanServer;

/**
 * Created with IntelliJ IDEA.
 * User: Lucas
 * Date: 6/17/13
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DBAccessible {

    /**
     * Gets the Database instance of the plugin.
     *
     * @return
     */
    EbeanServer getDB();

}
