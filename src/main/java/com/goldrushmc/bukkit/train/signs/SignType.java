package com.goldrushmc.bukkit.train.signs;


/**
 * This {@code enum} is for the different signs planned for the server.
 * 
 *
 * 
 * @author Diremonsoon
 *
 */
public enum SignType {
	TOWN("Town"),
	TRAIN_STATION("train station"),
	TRAIN_STATION_DIRECTION("direction"),
	TRAIN_STATION_TIME("time remaining:"),
	TRAIN_STATION_CART_COUNT("carts available"),
	ADD_STORAGE_CART("buy storage"),
	REMOVE_STORAGE_CART("sell storage"),
	ADD_RIDE_CART("buy ride"),
	REMOVE_RIDE_CART("sell ride"),
	FIX_BRIDGE("fix bridge"),
	PAY_TAX("pay taxes"),
	UPGRADE_HOUSE("upgrade home"),
	BUY_HOUSE("buy house"),
	SELL_HOUSE("sell house"),
	REPAIR_HOUSE("repair home");
	
	final String type;
	
	private SignType(String type) {
		this.type = type;
	}
}
