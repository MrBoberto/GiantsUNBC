package packets;

import java.io.Serializable;

public record ClientUpdatePacket(double x, double y, double angle, boolean isWalking,
                                 int weaponSerial) implements Serializable {}
