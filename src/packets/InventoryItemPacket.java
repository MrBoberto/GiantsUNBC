package packets;

import java.io.Serializable;

public record InventoryItemPacket(int playerToBeAffected, int indexToRemove, int serial) implements Serializable {}
