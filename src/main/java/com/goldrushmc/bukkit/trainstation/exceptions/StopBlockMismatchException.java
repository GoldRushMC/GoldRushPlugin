package com.goldrushmc.bukkit.trainstation.exceptions;

public class StopBlockMismatchException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -9045651067485797239L;


    @Override
    public String getMessage() {
        return "The amount of stop blocks is not right!";
    }

}
