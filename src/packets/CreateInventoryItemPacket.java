package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs a player of the creation of an InventoryItem
 *
 * @author The Boyz
 * @version 1
 */

import inventory_items.InventoryItem;

import java.io.Serializable;

public record CreateInventoryItemPacket(int x, int y,
                                        InventoryItem.InventoryItemType inventoryItemType) implements Serializable {}
