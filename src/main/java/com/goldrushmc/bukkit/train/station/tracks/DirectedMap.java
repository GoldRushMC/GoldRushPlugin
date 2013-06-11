package com.goldrushmc.bukkit.train.station.tracks;

<<<<<<< HEAD
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

=======
>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

<<<<<<< HEAD
public class DirectedMap implements IDirectedMap {

    private List<Block> mapped = new LinkedList<Block>();
    private List<Material> types = new ArrayList<Material>();
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
<<<<<<< HEAD
=======
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class DirectedMap implements IDirectedMap {

	private List<Block> mapped = new LinkedList<Block>();
	private List<Material> types = new ArrayList<Material>();
	private Block last;
	private Block focus;
	private BlockFace toSearch;
	private BlockFace originalDir;
	private Block block;
	private boolean done = false;

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
		if(types.contains(focus.getRelative(toSearch).getType())){
			last = focus;
			focus = focus.getRelative(toSearch);
		}
		else {
			//Check the top and bottom blocks of the relative block.
			Block check = check(focus.getRelative(toSearch));
			//If it comes back null, we know that a direction change is in order.
			if(check == null) { 
				//We try to find a new direction, and if it succeeds, we run getNext() again.
				if(newDirection()) {
					//Recursive, not sure if this will work. it should though, in theory :P
					return getNext();
				}
				//This should only happen once. ever.
				else { done = true; return null; }
			}
			else if(types.contains(check.getType())) {
				last = focus;
				focus = check;
			}
		}
		//Add the new block to the focus list.
		mapped.add(focus);
		return focus;
	}

	public boolean isDone() {
		return done;
	}

	@Override
	public Block peekNext() {
		if(focus == block) return block.getRelative(toSearch);
		else return focus.getRelative(toSearch);
	}

	/**
	 * Checks the block above and below to see if we have a decline or incline.
	 * 
	 * @param b
	 * @return
	 */
	public Block check(Block b) {
		if(types.contains(b.getRelative(BlockFace.UP).getType())) {
			return b.getRelative(BlockFace.UP);
		}
		else if(types.contains(b.getRelative(BlockFace.DOWN).getType())) {
			return b.getRelative(BlockFace.DOWN);
		}
		else return null;

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
		if(!types.contains(focus.getRelative(toSearch).getType())) {
			Block check = check(focus.getRelative(toSearch));
			if(check == null) return false;
			else return types.contains(check);
		}
		else return types.contains(focus.getRelative(toSearch).getType());
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

	public boolean newDirection() {
		switch(toSearch) {
		case NORTH:
		case SOUTH: {
			if(this.types.contains(focus.getRelative(BlockFace.WEST).getType())) {
				setDirection(BlockFace.WEST);
			}
			else if(this.types.contains(focus.getRelative(BlockFace.EAST).getType())) {
				setDirection(BlockFace.EAST);
			}
			else if(!this.types.contains(focus.getRelative(BlockFace.WEST).getType()) 
					&& !this.types.contains(focus.getRelative(BlockFace.EAST).getType())) {
				Block west = check(focus.getRelative(BlockFace.WEST)),
					  east = check(focus.getRelative(BlockFace.EAST));
				if(this.types.contains(west.getType())) {
					setDirection(BlockFace.WEST);
				}	
				else if(this.types.contains(east.getType())) {
					setDirection(BlockFace.EAST);
				}
			}
			break;
		}
		case EAST:
		case WEST: {
			if(this.types.contains(focus.getRelative(BlockFace.NORTH).getType())) {
				setDirection(BlockFace.NORTH);
			}
			else if(this.types.contains(focus.getRelative(BlockFace.SOUTH).getType())) {
				setDirection(BlockFace.SOUTH);
			}
			else if(!this.types.contains(focus.getRelative(BlockFace.NORTH).getType()) 
					&& !this.types.contains(focus.getRelative(BlockFace.NORTH).getType())) {
				Block north = check(focus.getRelative(BlockFace.NORTH)),
					  south = check(focus.getRelative(BlockFace.SOUTH));
				if(this.types.contains(north.getType())) {
					setDirection(BlockFace.WEST);
				}	
				else if(this.types.contains(south.getType())) {
					setDirection(BlockFace.EAST);
				}
			}
			break;
		}
		default: break;
		}
		//If the new direction has a block of the type we want, yay!
		if(hasNext()) return true;
		//If not, not yay....
		else return false;
	}
	
	@Override
	public void goOtherWay() {
		this.focus = block;
		setDirection(originalDir.getOppositeFace());
		done = false;
	}
>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
=======

    @Override
    public List<Block> getNodes() {
        return nodes;
    }
>>>>>>> master
}
