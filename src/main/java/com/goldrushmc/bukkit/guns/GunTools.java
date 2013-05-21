package com.goldrushmc.bukkit.guns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GunTools {

	public Location getSpawnLoc(Player p) {
		if (getCardinalDirection(p) == "N") {
			Location loc = p.getEyeLocation().add(
					p.getEyeLocation().getDirection()
							.add(new Vector(0.5, 0, -0.5)));
			return loc;
		} else if (getCardinalDirection(p) == "NW") {
			Location loc = p.getEyeLocation().add(
					p.getEyeLocation().getDirection()
							.add(new Vector(-0.5, 0, -0.5)));
			return loc;
		} else if (getCardinalDirection(p) == "W") {
			Location loc = p.getEyeLocation().add(
					p.getEyeLocation().getDirection()
							.add(new Vector(-0.5, 0, 0.5)));
			return loc;
		} else if (getCardinalDirection(p) == "SW") {
			Location loc = p.getEyeLocation().add(
					p.getEyeLocation().getDirection()
							.add(new Vector(-0.5, 0, -0.5)));
			return loc;
		} else {
			Location loc = p.getEyeLocation().add(
					p.getEyeLocation().getDirection()
							.add(new Vector(0.5, 0, 0.5)));
			return loc;
		}
	}

	public static String getCardinalDirection(Player player) {
		double rotation = (player.getLocation().getYaw() - 90) % 360;
		if (rotation < 0) {
			rotation += 360.0;
		}
		if (0 <= rotation && rotation < 22.5) {
			return "N";
		} else if (22.5 <= rotation && rotation < 67.5) {
			return "NE";
		} else if (67.5 <= rotation && rotation < 112.5) {
			return "E";
		} else if (112.5 <= rotation && rotation < 157.5) {
			return "SE";
		} else if (157.5 <= rotation && rotation < 202.5) {
			return "S";
		} else if (202.5 <= rotation && rotation < 247.5) {
			return "SW";
		} else if (247.5 <= rotation && rotation < 292.5) {
			return "W";
		} else if (292.5 <= rotation && rotation < 337.5) {
			return "NW";
		} else if (337.5 <= rotation && rotation < 360.0) {
			return "N";
		} else {
			return null;
		}
	}
	
	public List<Player> getPlayersWithin(Player player, int distance) {
		List<Player> res = new ArrayList<Player>();
		int d2 = distance * distance;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2) {
				res.add(p);
			}
		}
		return res;
	}
}
