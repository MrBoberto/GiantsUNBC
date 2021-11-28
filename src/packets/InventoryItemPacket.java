package packets;

import weapons.guns.Weapon;

import java.io.Serializable;

public class InventoryItemPacket implements Serializable {
    private final int playerToBeAffected;
    private final int indexToRemove;
    private int serial;

    public InventoryItemPacket(int playerToBeAffected, int indexToRemove, int serial) {
        this.playerToBeAffected = playerToBeAffected;
        this.indexToRemove = indexToRemove;
        this.serial = serial;
    }
    public int getPlayerToBeAffected() {
        return playerToBeAffected;
    }

    public int getIndexToRemove() {
        return indexToRemove;
    }

    public int getSerial() {
        return serial;
    }
}
