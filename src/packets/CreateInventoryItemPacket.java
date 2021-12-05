package packets;

import inventory_items.InventoryItem;

import java.io.Serializable;

public record CreateInventoryItemPacket(int x, int y,
                                        InventoryItem.InventoryItemType inventoryItemType) implements Serializable {}
