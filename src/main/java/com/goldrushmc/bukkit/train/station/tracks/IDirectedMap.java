package com.goldrushmc.bukkit.train.station.tracks;

<<<<<<< HEAD
=======
import java.util.List;

>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

<<<<<<< HEAD
import java.util.List;

public interface IDirectedMap {

    public BlockFace getDirection();

    public Block getNext();

    public Block getLast();

    public Block getCurrent();

    public Block peekNext();

    public void setDirection(BlockFace dir);

    public boolean hasNext();

    public Block getBlockAt(BlockFace dir);

    public List<Block> getBlockList();

    public List<Material> getSearchTypes();

    public void setSearchTypes(List<Material> types);

    public boolean isDone();

    public void goOtherWay();
=======
public interface IDirectedMap {
	
	public BlockFace getDirection();
	
	public Block getNext();
	
	public Block getLast();
	
	public Block getCurrent();
	
	public Block peekNext();
	
	public void setDirection(BlockFace dir);
	
	public boolean hasNext();
	
	public Block getBlockAt(BlockFace dir);
	
	public List<Block> getBlockList();

	public List<Material> getSearchTypes();
	
	public void setSearchTypes(List<Material> types);
	
	public boolean isDone();
	
	public void goOtherWay();
>>>>>>> 93bfc5d008ddd8eda936225ffba6512a35afac98
}
