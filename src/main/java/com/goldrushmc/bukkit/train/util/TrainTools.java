package com.goldrushmc.bukkit.train.util;

import com.goldrushmc.bukkit.train.station.tracks.SmallBlockMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

<<<<<<< HEAD
=======
import com.goldrushmc.bukkit.train.station.tracks.SmallBlockMap;

>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
public class TrainTools {

    public static EntityType getTrainType(String trainType) {
        switch (trainType) {
            case "ride":
                return EntityType.MINECART;
            case "store":
                return EntityType.MINECART_CHEST;
            default:
                return null;
        }
    }

    public static BlockFace getDirection(String direction) {
        switch (direction.toLowerCase()) {
            case "north":
                return BlockFace.NORTH;
            case "south":
                return BlockFace.SOUTH;
            case "east":
                return BlockFace.EAST;
            case "west":
                return BlockFace.WEST;
            default:
                return null;
        }
    }

    /**
     * Gets the BlockFace direction, based on the Yaw
     *
     * @param yaw The direction in {@code double} form, to be translated
     * @return The BlockFace direction
     */
    public static BlockFace getDirection(Player player) {
        double yaw = (player.getLocation().getYaw() - 90) % 360;
        if (yaw < 0) {
            yaw += 360.0;
        }

        if (0 <= yaw && yaw < 22.5) {
            return BlockFace.NORTH;
        } else if (22.5 <= yaw && yaw < 67.5) {
            return BlockFace.NORTH_EAST;
        } else if (67.5 <= yaw && yaw < 112.5) {
            return BlockFace.EAST;
        } else if (112.5 <= yaw && yaw < 157.5) {
            return BlockFace.SOUTH_EAST;
        } else if (157.5 <= yaw && yaw < 202.5) {
            return BlockFace.SOUTH;
        } else if (202.5 <= yaw && yaw < 247.5) {
            return BlockFace.SOUTH_WEST;
        } else if (247.5 <= yaw && yaw < 292.5) {
            return BlockFace.WEST;
        } else if (292.5 <= yaw && yaw < 337.5) {
            return BlockFace.NORTH_WEST;
        } else if (337.5 <= yaw && yaw < 360.0) {
            return BlockFace.NORTH;
        } else {
            return null;
        }
    }

    /**
     * Gets the blockface to the left of the specified block face.
     *
     * @param face The {@link BlockFace} selected.
     * @return The {@link BlockFace} to the left.
     */
    public static BlockFace toTheLeft(BlockFace face) {
        BlockFace left;
        switch (face) {
            case EAST:
            case EAST_NORTH_EAST:
            case EAST_SOUTH_EAST:
                left = BlockFace.NORTH;
                break;
            case NORTH:
            case NORTH_EAST:
            case NORTH_NORTH_EAST:
            case NORTH_NORTH_WEST:
            case NORTH_WEST:
                left = BlockFace.WEST;
                break;
            case SOUTH:
            case SOUTH_EAST:
            case SOUTH_SOUTH_EAST:
            case SOUTH_SOUTH_WEST:
            case SOUTH_WEST:
                left = BlockFace.EAST;
                break;
            case WEST:
            case WEST_NORTH_WEST:
            case WEST_SOUTH_WEST:
                left = BlockFace.SOUTH;
                break;
            default:
                return null;
        }
        return left;
    }

    /**
     * Gets the distance between two locations, in a linear direction.
     *
     * @param loc1 The first {@link Location}.
     * @param loc2 The second {@link Location}.
     * @return The {@code Integer} distance.
     */
    public static int getDistance(Location loc1, Location loc2) {

        if (loc1.equals(loc2)) {
            return 0;
        } else if (loc1.getX() != loc2.getX()) {

            if (loc1.getX() > loc2.getX()) {
                return loc1.getBlockX() - loc2.getBlockX();
            } else {
                return loc2.getBlockX() - loc1.getBlockX();
            }
        } else if (loc1.getZ() != loc2.getZ()) {
            if (loc1.getZ() > loc2.getZ()) {
                return loc1.getBlockZ() - loc2.getBlockZ();
            } else {
                return loc2.getBlockZ() - loc1.getBlockZ();
            }
        }
        return -1;
    }

    /**
     * Finds the middle of two points. Not too useful yet.
     *
     * @param loc1
     * @param loc2
     * @return
     */
    public static Location getMiddle(Location loc1, Location loc2) {

        if (!loc1.equals(loc2)) {

            if (loc1.getX() != loc2.getX()) {
                return new Location(loc1.getWorld(), (loc1.getX() + loc2.getX()) / 2, loc1.getY(), loc1.getZ());
            } else if (loc1.getZ() != loc2.getZ()) {
                return new Location(loc1.getWorld(), loc1.getX(), loc1.getY(), (loc1.getZ() + loc2.getZ()) / 2);
            }
        }
        return loc1;
    }

    /**
     * Checks whether or not the {@link Block} is a rail {@link Block}.
     *
     * @param block The {@link Block} in question.
     * @return {@code true} if it is a rail, {@code false} otherwise.
     */
    public static boolean isRail(Block block) {
        return block.getType() == Material.RAILS || block.getType() == Material.POWERED_RAIL || block.getType() == Material.ACTIVATOR_RAIL;
    }

    /**
     * Checks to see if the rail has rails attached to it, and if they go north-to-south or east-to-west
     *
     * @param trainSpawn The Location in question.
     * @return
     */
    public static boolean singleRailCheck(Location trainSpawn) {
        if (!isRail(trainSpawn.getBlock())) return false; //If not a rail, fail silently

        SmallBlockMap sbm = new SmallBlockMap(trainSpawn.getBlock());

        //Makes sure that the location selected has more than one track connected.
        if (sbm.potentialConnections() <= 1) {
            return false;
        }

        return true;
    }

    /**
     * An {@code enum} used to determine the type of train.
     *
     * @author Diremonsoon
     */
    public enum TrainType {
        DEFAULT("Default"),
        PUBLIC("Public"),
        PRIVATE("Private"),
        TOWN("Town"),
        TRANSPORT("Transport");

        final String type;

        TrainType(String type) {
            this.type = type;
        }

        public static TrainType getType(String type) {
            for (TrainType t : TrainType.values()) {
                if (t.type.equalsIgnoreCase(type)) {
                    return t;
                }
            }
            return DEFAULT;

        }
    }
}
