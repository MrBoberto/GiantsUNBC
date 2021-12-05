package packets;

import java.io.Serializable;

public record ServerUpdatePacket(double x, double y, double angle, int[] health, boolean isWalking, int weaponSerial,
                                 boolean[] isInvincible) implements Serializable {}
