package com.goldrushmc.bukkit.defaults;

import com.goldrushmc.bukkit.trainstation.TrainStation;
import com.goldrushmc.bukkit.trainstation.exceptions.MarkerNumberException;
import com.goldrushmc.bukkit.trainstation.exceptions.TooLowException;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class BlockFinder extends DefaultListener implements Serializable{

    public final World world;
    public final List<Block> selectedArea;
    public final List<Block> area;
    public final List<Block> surface;
    public final List<Block> partialArea;
    public final List<Chunk> chunks;

    public BlockFinder(World world, List<Location> coords, JavaPlugin plugin) throws MarkerNumberException {
        super(plugin);
        this.world = world;

        if (!(coords.size() == 2)) throw new MarkerNumberException();

        Location loc1 = coords.get(0), loc2 = coords.get(1);
        //Generate areas
        this.selectedArea = this.generateArea(loc1, loc2);
        this.surface = this.generateSurface(loc1, loc2);
        this.partialArea = this.getSelectiveArea(loc1, loc2, 30, 60);
        this.area = this.getFullArea(loc1, loc2);
        //Add chunks
        List<Chunk> chunks = new ArrayList<>();
        for (Block b : this.surface) {
            if (!chunks.contains(b.getChunk())) {
                chunks.add(b.getChunk());
            }
        }
        this.chunks = chunks;
    }

    /**
     * Handles when the class is deleted.
     */
    public abstract void remove();

    /**
     * Handles when the class is added.
     */
    public abstract void add();


    protected final boolean isGreaterThan(final int x1, final int x2) {

        return (x1 > x2);
    }

    /**
     * Creates an selectedArea {@link List} of {@link Block}s
     *
     * @return the {@code List<Block>} of {@link Block}s
     */
    protected final List<Block> generateArea(Location loc1, Location loc2) {

        //Iterate through each line of locations, and add them to the perimeter. This should make a rectangle.
        List<Block> area = new ArrayList<>();
        //Set min as loc1 and max as loc2 by default.
        int maxX = loc2.getBlockX(), maxY = loc2.getBlockY(), maxZ = loc2.getBlockZ();
        int minX = loc1.getBlockX(), minY = loc1.getBlockY(), minZ = loc1.getBlockZ();
        //Figure out which values should change as min and max.
        if (loc1.getBlockX() > maxX) {
            maxX = loc1.getBlockX();
            minX = loc2.getBlockX();
        }
        if (loc1.getBlockY() > maxY) {
            maxY = loc1.getBlockY();
            minY = loc2.getBlockY();
        }
        if (loc1.getBlockZ() > maxZ) {
            maxZ = loc1.getBlockZ();
            minZ = loc2.getBlockZ();
        }

        //Iterate through every block in the selectedArea, and store them in a list of blocks.
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    area.add(world.getBlockAt(x, y, z));
                }
            }
        }

        return area;
    }

    /**
     * Gets the surface of the {@link TrainStation}. (FLAT) Uses the lowest Y value of the two.
     *
     * @return The {@code List}<{@link Block}> that contains all surface blocks.
     * @throws TooLowException
     */
    protected final List<Block> generateSurface(Location loc1, Location loc2) {

        //Iterate through each line of locations, and add them to the perimeter. This should make a rectangle.
        List<Block> surface = new ArrayList<>();
        //Set min as loc1 and max as loc2 by default.
        int maxX = loc2.getBlockX(), maxY = loc2.getBlockY(), maxZ = loc2.getBlockZ();
        int minX = loc1.getBlockX(), minY = loc1.getBlockY(), minZ = loc1.getBlockZ();
        //Figure out which values should change as min and max.
        if (loc1.getBlockX() > maxX) {
            maxX = loc1.getBlockX();
            minX = loc2.getBlockX();
        }
        if (loc1.getBlockY() > maxY) {
            maxY = loc1.getBlockY();
            minY = loc2.getBlockY();
        }
        if (loc1.getBlockZ() > maxZ) {
            maxZ = loc1.getBlockZ();
            minZ = loc2.getBlockZ();
        }

        //Iterate through every block in the surface, and store them in a list of blocks.
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                surface.add(world.getBlockAt(x, minY, z));
            }
        }

        return surface;
    }

    /**
     * Gets all of the blocks of some Y coordinates for each X,Z position.
     * <p/>
     * This is to put a limit on how high and low the selectedArea is.
     *
     * @return
     */
    protected final List<Block> getSelectiveArea(Location loc1, Location loc2, int minY, int maxY) {

        //Iterate through each line of locations, and add them to the perimeter. This should make a rectangle.
        List<Block> area = new ArrayList<>();
        //Set min as loc1 and max as loc2 by default.
        int maxX = loc2.getBlockX(), maxZ = loc2.getBlockZ();
        int minX = loc1.getBlockX(), minZ = loc1.getBlockZ();
        //Figure out which values should change as min and max.
        if (loc1.getBlockX() > maxX) {
            maxX = loc1.getBlockX();
            minX = loc2.getBlockX();
        }
        if (loc1.getBlockZ() > maxZ) {
            maxZ = loc1.getBlockZ();
            minZ = loc2.getBlockZ();
        }

        //Iterate through every block in the selectedArea, and store them in a list of blocks.
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    area.add(world.getBlockAt(x, y, z));
                }
            }
        }

        return area;
    }

    /**
     * Gets all of the blocks of some Y coordinates for each X,Z position.
     * <p/>
     * This is to put a limit on how high and low the selectedArea is.
     *
     * @return
     */
    protected final List<Block> getFullArea(Location loc1, Location loc2) {
        return getSelectiveArea(loc1, loc2, 0, 126);
    }

    /**
     * @return the world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Will find the blocks which make up a structure, excluding all air blocks.
     * <p/>
     * This method is meant for grief-prevention.
     *
     * @return
     */
    public abstract List<Block> findNonAirBlocks();

    /**
     * @return the selectedArea
     */
    public List<Block> getSelectedArea() {
        return selectedArea;
    }

    /**
     * @return the area
     */
    public List<Block> getArea() {
        return area;
    }

    /**
     * @return the surface
     */
    public List<Block> getSurface() {
        return surface;
    }

    /**
     * @return the partialArea
     */
    public List<Block> getPartialArea() {
        return partialArea;
    }

    //TODO Listener Stuff

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (this.chunks.contains(event.getChunk())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public abstract void onPlayerDamageAttempt(BlockDamageEvent event);

    @EventHandler
    public abstract void onPlayerBreakAttempt(BlockBreakEvent event);

    @EventHandler
    public abstract void onPlayerPlaceAttempt(BlockPlaceEvent event);
}
