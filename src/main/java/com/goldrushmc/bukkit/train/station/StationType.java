package com.goldrushmc.bukkit.train.station;


public enum StationType {
<<<<<<< HEAD
    DEFAULT("Default"),
    STORAGE_TRANS("Storage"),
    PASSENGER_TRANS("Passenger"),
    HYBRID_TRANS("Hybrid");
=======
	DEFAULT("Default"),
	STORAGE_TRANS("Storage"),
	PASSENGER_TRANS("Passenger"),
	HYBRID_TRANS("Hybrid");
>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98

    final String type;

    private StationType(String type) {
        this.type = type;
    }

    public static StationType findType(String type) {
        for (StationType t : StationType.values()) {
            if (t.type.equalsIgnoreCase(type)) {
                return t;
            }
        }
        return DEFAULT;
    }
}
