package packets;

import inventory_items.InventoryItem;

import java.io.Serializable;

public class CreateInventoryItemPacket implements Serializable {
    private final int x;
    private final int y;
    private final InventoryItem.InventoryItemType inventoryItemType;

    public CreateInventoryItemPacket(int x, int y, InventoryItem.InventoryItemType inventoryItemType) {
        this.x = x;
        this.y = y;
        this.inventoryItemType = inventoryItemType;
    }

    public InventoryItem.InventoryItemType getPowerUpType() {
        return inventoryItemType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
