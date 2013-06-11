package com.goldrushmc.bukkit.train.exceptions;

import org.bukkit.Bukkit;

/**
 * Created with IntelliJ IDEA.
 * User: Diremonsoon
 * Date: 6/6/13
 * Time: 10:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoExitAssignedException extends Exception {

    public NoExitAssignedException() {
        Bukkit.getLogger().severe("A Player has failed to put up a sign to determine Passenger Exiting.");
    }

}
