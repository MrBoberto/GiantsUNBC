package packets;

import java.io.Serializable;

public record InventoryItemPacket(int playerToBeAffected, int indexToRemove, int serial) implements Serializable {

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
