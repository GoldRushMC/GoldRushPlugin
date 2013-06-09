package com.goldrushmc.bukkit.train;


public enum CardinalMarker {
    NORTH_EAST_CORNER("North_East_Corner"),
    SOUTH_WEST_CORNER("South_West_Corner"),
    SOUTH_EAST_CORNER("South_East_Corner"),
    NORTH_WEST_CORNER("North_West_Corner");

    final String card;

    CardinalMarker(String card) {
        this.card = card;
    }
}
