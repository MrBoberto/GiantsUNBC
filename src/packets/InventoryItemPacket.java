package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs a player of an inventory item pickup
 *
 * @author The Boyz
 * @version 1
 */

import java.io.Serializable;

public record InventoryItemPacket(int playerToBeAffected, int indexToRemove, int serial) implements Serializable {}
