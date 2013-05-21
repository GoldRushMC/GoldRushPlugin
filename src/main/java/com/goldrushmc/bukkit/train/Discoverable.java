package com.goldrushmc.bukkit.train;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;

/**
 * Will be used to determine each cardinal direction of a {@link Block}, and whether or not each cardinal {@link Block} is a rail.
 * 
 * @author Lucas
 *
 */
public interface Discoverable {

	/**
	 * Gets the {@link Block} east of the main {@link Block}
	 * 
	 * @return The east {@link Block}
	 */
	public Block getEast();
	
	/**
	 * Gets the {@link Block} west of the main {@link Block}
	 * 
	 * @return The west {@link Block}
	 */
	public Block getWest();
	
	/**
	 * Gets the {@link Block} south of the main {@link Block}
	 * 
	 * @return The south {@link Block}
	 */
	public Block getNorth();
	
	/**
	 * Gets the {@link Block} south of the main {@link Block}
	 * 
	 * @return The south {@link Block}
	 */
	public Block getSouth();

	/**
	 * Gets the main {@link Block} of the class.
	 *  
	 * @return The main {@link Block}
	 */
	public Block getMainBlock();

	/**
	 * Calculates the {@link Block} east of the main {@link Block}
	 * 
	 * @return The east {@link Block}
	 */
	public Block calcEastBlock();

	/**
	 * Calculates the {@link Block} west of the main {@link Block}
	 * 
	 * @return The west {@link Block}
	 */
	public Block calcWestBlock();

	/**
	 * Calculates the {@link Block} north of the main {@link Block}
	 * 
	 * @return The north {@link Block}
	 */
	public Block calcNorthBlock();

	/**
	 * Calculates the {@link Block} south of the main {@link Block}
	 * 
	 * @return The south {@link Block}
	 */
	public Block calcSouthBlock();

	/**
	 * Checks to see if the {@link Block} has another connected {@link Block} that is a rail.
	 * 
	 * @return {@code true} if there is a rail connection, {@code false} if not.
	 * 
	 */
	public boolean hasRailConnection();

	/**
	 * Determines, if any, which direction the railway is going.
	 * 
	 * @return The {@link Direction} the railway is heading
	 */
	public BlockFace getBearing();

	/**
	 * <p>Returns the amount of rails that are around the main {@link Block}.</p>
	 * <p>This does <i>NOT DETERMINE</i> which rails are connected to the main one. </p>
	 * 
	 * @return The {@code int} amount of rails around the main {@link Block}.
	 */
	public int potentialConnections();

	/**
	 * Returns a {@link Block} {@link List} of each {@link Block} that is a rail.
	 * 
	 * @return The {@link Block} {@link List}
	 */
	public List<Block> connectedRails();

	/**
	 * <p>Determines if the {@link Block} in question is a rail {@link Block}.</p>
	 * <p><h2>Can be one of the following:</h2><ul>
	 * 	<li>Rail</li>
	 * 	<li>Detector Rail</li>
	 *  <li>Powered Rail</li>
	 *  <li>Activator Rail</li>
	 * </ul></p>
	 * @param block the {@link Block} in question
	 * @return {@code true} if it is a rail, {@code false} if not.
	 */
	public boolean isRail(Block block);

	/**
	 * Sees if the {@link Block} selected is the final rail in the track.
	 * 
	 * @return {@code true} if the {@link Block} is the last rail in the track, {@code false} if not.
	 */
	public boolean isEnd();

	/**
	 * Checks above and below the selected location.
	 * 
	 * @param loc The {@link Location} to be analyzed
	 * @return The {@link Block}, above or below, which is not air. <i><b>Will come back </i></b>{@code null}<i><b> if it is</b></i> {@link Material}.Air
	 */
	public Block checkUpDown(Location loc);

}
