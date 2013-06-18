package com.goldrushmc.bukkit.trainstation.tracks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DirectedMap implements IDirectedMap {

    private List<Block> mapped = new LinkedList<>();
    private List<Material> types = new ArrayList<>();
    private Block last;
    private Block focus;
    private BlockFace toSearch;
    private BlockFace originalDir;
    private Block block;
    private boolean done = false;
    private List<Block> nodes;

    public DirectedMap(BlockFace dir, Block start) {
        this.block = start;
        this.focus = this.block;
        this.toSearch = dir;
        /*Saves the original direction, for later
		 *reversing and getting the other part of the track.
		 */
        this.originalDir = dir;
    }

    @Override
    public BlockFace getDirection() {
        return toSearch;
    }

    @Override
    public Block getNext() {
        if (types.contains(focus.getRelative(toSearch).getType())) {
            last = focus;
            focus = focus.getRelative(toSearch);
        } else {
            //Check the top and bottom blocks of the relative block.
            Block check = check(focus.getRelative(toSearch));
        if (types.contains(check.getType())) {
                last = focus;
                focus = check;
            }
        //Add the new block to the focus list.
        mapped.add(focus);
        return focus;
    }
        return null;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public Block peekNext() {
        if (focus == block) return block.getRelative(toSearch);
        else return focus.getRelative(toSearch);
    }

    /**
     * Checks the block above and below to see if we have a decline or incline.
     *
     * @param b
     * @return
     */
    public Block check(Block b) {
        if (types.contains(b.getRelative(BlockFace.UP).getType())) {
            return b.getRelative(BlockFace.UP);
        } else if (types.contains(b.getRelative(BlockFace.DOWN).getType())) {
            return b.getRelative(BlockFace.DOWN);
        } else return null;

    }

    @Override
    public Block getLast() {
        return last;
    }

    @Override
    public void setDirection(BlockFace dir) {
        this.toSearch = dir;
    }

    @Override
    public boolean hasNext() {
        if (!types.contains(focus.getRelative(toSearch).getType())) {
            Block check = check(focus.getRelative(toSearch));
            if (check == null) return false;
            else return types.contains(check.getType());
        } else return types.contains(focus.getRelative(toSearch).getType());
    }

    @Override
    public Block getBlockAt(BlockFace dir) {
        return block.getRelative(dir);
    }

    @Override
    public List<Block> getBlockList() {
        return mapped;
    }

    @Override
    public List<Material> getSearchTypes() {
        return types;
    }

    @Override
    public void setSearchTypes(List<Material> types) {
        this.types = types;
    }

    @Override
    public Block getCurrent() {
        return focus;
    }

    @Override
    public void goOtherWay() {
        this.focus = block;
        setDirection(originalDir.getOppositeFace());
        done = false;
    }
}