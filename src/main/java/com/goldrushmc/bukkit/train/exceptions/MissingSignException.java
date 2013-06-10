package com.goldrushmc.bukkit.train.exceptions;

import java.util.ArrayList;
import java.util.List;

import com.goldrushmc.bukkit.train.signs.ISignLogic;
import com.goldrushmc.bukkit.train.signs.SignType;

/**
 * Determines what signs are missing in the event of an exception.
 * 
 * @author Diremonsoon
 *
 */
public class MissingSignException extends Exception {

	private List<SignType> missing;
	/**
	 * 
	 */
	private static final long serialVersionUID = 850439204501209067L;

	public MissingSignException(ISignLogic logic) {
		missing = new ArrayList<SignType>();
		if(logic.getSigns(SignType.TRAIN_STATION_CART_COUNT).isEmpty()) {
			missing.add(SignType.TRAIN_STATION_CART_COUNT);
		}
		if(logic.getSigns(SignType.TRAIN_STATION_DIRECTION).isEmpty()) {
			missing.add(SignType.TRAIN_STATION_DIRECTION);
		}
		if(logic.getSigns(SignType.TRAIN_STATION_TIME).isEmpty()) {
			missing.add(SignType.TRAIN_STATION_TIME);
		}
		if(logic.getSigns(SignType.TRAIN_STATION).isEmpty()) {
			missing.add(SignType.TRAIN_STATION);			
		}
	}
	
	@Override
	public String getMessage() {
		String missingTypes = "";
		for(SignType type : missing) {
			switch(type) {
			case TRAIN_STATION_CART_COUNT: missingTypes += "Cart Counter, "; break;
			case TRAIN_STATION_DIRECTION: missingTypes += "Direction, "; break;
			case TRAIN_STATION_TIME:missingTypes += "Time Remaining, "; break;
			case TRAIN_STATION:missingTypes += "Departure"; break;
			default: break;
			}
		}
		
		return "The train station needs the following: \n" + missingTypes;
	}
}
