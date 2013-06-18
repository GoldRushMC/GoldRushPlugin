package com.goldrushmc.bukkit.trainstation.exceptions;

public class MarkerNumberException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -4973952891759037360L;

    @Override
    public String getMessage() {
        return "The amount of markers does not equal 2.";
    }
}
