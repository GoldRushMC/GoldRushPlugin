package com.goldrushmc.bukkit.defaults;

/**
 * User: Diremonsoon
 * Date: 6/20/13
 */
public class NotInTownException extends Exception {

    private String type;

    public NotInTownException(String type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return "You cannot create a " + type + " here. There must be a town in place first.";
    }
}
