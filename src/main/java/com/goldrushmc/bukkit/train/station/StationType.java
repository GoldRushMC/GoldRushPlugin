package com.goldrushmc.bukkit.train.station;


public enum StationType {
	DEFAULT("Default"),
	STORAGE_TRANS("Storage"),
	PASSENGER_TRANS("Passenger"),
	HYBRID_TRANS("Hybrid");

	final String type;

	private StationType(String type) {
		this.type = type;
	}

	public static StationType findType(String type) {
		for(StationType t : StationType.values()) {
			if(t.type.equalsIgnoreCase(type)) {
				return t;
			}
		}
		return DEFAULT;
	}
}
