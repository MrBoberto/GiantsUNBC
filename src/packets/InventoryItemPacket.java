package packets;

import java.io.Serializable;

public class InventoryItemPacket implements Serializable {
    private final int playerToBeAffected;
    private final int indexToRemove;

    public InventoryItemPacket(int playerToBeAffected, int indexToRemove) {
        this.playerToBeAffected = playerToBeAffected;
        this.indexToRemove = indexToRemove;
    }
    public int getPlayerToBeAffected() {
        return playerToBeAffected;
    }

    public int getIndexToRemove() {
        return indexToRemove;
    }
}
