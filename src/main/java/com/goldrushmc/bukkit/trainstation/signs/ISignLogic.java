package com.goldrushmc.bukkit.trainstation.signs;

import com.goldrushmc.bukkit.trainstation.exceptions.MissingSignException;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.List;
import java.util.Map;

/**
 * Controls and remembers the signs within a given {@link Chunk}.
 *
 * @author Diremonsoon
 */
public interface ISignLogic {

    /**
     * Gets the stored {@code Map<?,?>} of {@link Sign} and {@link SignType}
     *
     * @return The set of signs, {@code Set<Sign>}.
     */
    public Map<Sign, SignType> getSignTypes();

    /**
     * Add a {@link Sign} to the permission mapping.
     *
     * @param sign The {@link Sign} to add.
     */
    public void addSign(Sign sign, SignType type);

    /**
     * Adds a {@link Sign} to the set of signs.
     *
     * @param signName The {@link Sign}'s name
     * @param sign     The {@link Sign}
     */
    public boolean addSign(String signName, Sign sign);

    /**
     * Removes all references to the specified sign.
     *
     * @param signName The name of the {@link Sign} to remove.
     */
    public void removeSign(String signName);

    /**
     * Gets a {@link Sign} with the specified name.
     *
     * @param signName The {@link Sign} name.
     * @return The {@link Sign}
     */
    public Sign getSign(String signName);


    /**
     * Gets the {@link Sign}s with the specified {@link SignType}
     *
     * @param type The {@link SignType}.
     * @return The {@link Sign}
     */
    public List<Sign> getSigns(SignType type);

    /**
     * Gets the type with the specified sign.
     *
     * @param sign
     * @return
     */
    SignType getSignType(Sign sign);

    /**
     * Finds all of the relevant {@link Sign}s to Gold Rush MC, within a given chunk.
     *
     * @param blocks The {@link Chunk} to search.
     */
    public void findRelevantSigns(List<Block> blocks) throws MissingSignException;

    /**
     * Gets the {@link Chunk} associated with this logic.
     *
     * @return
     */
    public List<Sign> getSigns();

    /**
     * Updates all the signs with the right trainstation name, so players can buy and sell properly from that trainstation.
     *
     * @param trainName
     */
    public void updateTrain(String trainName);

}
